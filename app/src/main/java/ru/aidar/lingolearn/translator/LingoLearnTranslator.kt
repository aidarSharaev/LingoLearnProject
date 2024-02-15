package ru.aidar.lingolearn.translator

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import ru.aidar.lingolearn.base.BaseNetworkMonitor
import ru.aidar.lingolearn.utils.AvailableLanguage
import ru.aidar.lingolearn.utils.Constants
import ru.aidar.lingolearn.utils.Constants.AVAILABLE_TRANSLATOR_LANGUAGES
import ru.aidar.lingolearn.utils.Constants.ENGLISH_IS_ALWAYS_DOWNLOADED
import ru.aidar.lingolearn.utils.Constants.LANGUAGE_MAP
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

data class LlTranslatorState(
    val downloadedLanguages: List<AvailableLanguage> = listOf(),
    val sourceText: String = "",
    val targetText: String = "",
    val sourceLanguage: AvailableLanguage = AvailableLanguage("RUSSIAN", "ru"),
    val targetLanguage: AvailableLanguage = AvailableLanguage("ENGLISH", "en"),
    val isSourceModelDownloaded: Boolean = false,
    val isTargetModelDownloaded: Boolean = false,
    val isModelsAreLoaded: Constants.LanguagesDownloadState = Constants.LanguagesDownloadState.Creating,
)

// todo чекнуть что будет при очистке кеша
class LingoLearnTranslator @Inject constructor(
    private val networkMonitor: BaseNetworkMonitor,
    private val modelManager: RemoteModelManager,
    private val llTranslatorFlowWrapper: LlTranslatorFlowWrapper,
) : CoroutineScope {

    companion object {
        private const val NUM_TRANSLATORS = 5
        private val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
    }

    var translatorState = llTranslatorFlowWrapper.getState()

    private var parentJob: Job = SupervisorJob()
    private var translateJob: Job = Job()
    private var getDownloadedJob: Job = Job()
    private var deleteJob: Job = Job()
    private var downloadJob: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + parentJob + CoroutineName("LingoLearnTranslator")

    private var translatorClient: Translator = Translation.getClient(
        TranslatorOptions.Builder()
            .setSourceLanguage(translatorState.value.sourceLanguage.alphaCode)
            .setTargetLanguage(translatorState.value.targetLanguage.alphaCode)
            .build()
    )

    init {
        myLog("LingoLearnTranslator init")
        parentJob.invokeOnCompletion {
            myLog(
                "${parentJob.children.count()} ${translateJob.children.count()} ${getDownloadedJob.children.count()} ${deleteJob.children.count()} ${downloadJob.children.count()}" +
                    "${parentJob.isActive} ${translateJob.isActive} ${getDownloadedJob.isActive} ${deleteJob.isActive} ${downloadJob.isActive}"
            )
        }

        getDownloadedJob = launch {
            try {
                val models = getAllDownloadedModels()
                llTranslatorFlowWrapper.updateDownloadedLanguages(downloadedLanguages = models)
                checkDownloadedOnInit()
                this.cancel()
            } catch(e: Exception) {
                /* TODO */
            }
        }
    }

    fun onCleared() {
        myLog("onCleared")
        llTranslatorFlowWrapper.clearAll()
        parentJob.cancelChildren()
        parentJob.cancel()
    }

    private fun myLog(comment: String) {
        Log.d("TranslatorViewModel", comment)
    }


    private fun refreshTranslatorClient() {
        myLog("refreshTranslatorClient")
        with(translatorState.value) {
            translatorClient = Translation.getClient(
                TranslatorOptions.Builder()
                    .setSourceLanguage(sourceLanguage.alphaCode)
                    .setTargetLanguage(targetLanguage.alphaCode)
                    .build()
            )
        }
    }

    // todo проверить при очистке кеша
    private fun checkDownloadedOnInit() {
        myLog("checkDownloadedOnInit")
        sourceModelIsDownloaded()
        targetModelIsDownloaded()
        // TODO мб это надо удалить ДУМАЮ ЭТО ВЕРНО просто даю ответ
        llTranslatorFlowWrapper.updateIsModelsAreLoaded()
    }

    private fun sourceModelIsDownloaded() {
        myLog("sourceModelIsDownloaded")
        val src: Boolean
        with(translatorState.value) { src = downloadedLanguages.contains(sourceLanguage) }
        llTranslatorFlowWrapper.updateIsSomeModelDownloaded(value = src, thatsTheTarget = false)
    }

    private fun targetModelIsDownloaded() {
        myLog("isTargetModelIsDownloaded")
        val trgt: Boolean
        with(translatorState.value) { trgt = downloadedLanguages.contains(targetLanguage) }
        llTranslatorFlowWrapper.updateIsSomeModelDownloaded(value = trgt, thatsTheTarget = true)
    }

    fun changeSourceText(newSourceText: String) {
        myLog("jobChildern -- ${translateJob.children.count()}")
        llTranslatorFlowWrapper.updateSourceText(text = newSourceText)
        translateJob = launch {
            try {
                if(translatorState.value.isModelsAreLoaded == Constants.LanguagesDownloadState.AllRight)
                    translate()
                else {
                    myLog("ПОШЕЛ НАХУЙ СОСИ ХУЙ")
                }
            } catch(e: Exception) {
                // TODO Toast?
            }
        }
    }

    private fun compare(
        language: AvailableLanguage,
        cmpLanguage: AvailableLanguage,
        thatsTheTarget: Boolean,
        isThisSwap: Boolean = false,
    ) {
        if(language == cmpLanguage) {
            Log.d("swapAidar", "ge == cmpLanguage")
            return
        }
        if(isThisSwap) {
            Log.d("swapAidar", "language == swapLanguage")
            llTranslatorFlowWrapper.swapLanguages()
        } else if(language != cmpLanguage) {
            Log.d("swapAidar", "language != cmpLanguage")
            llTranslatorFlowWrapper.updateSomeLanguage(
                language = language,
                thatsTheTarget = thatsTheTarget
            )
        }

        sourceModelIsDownloaded()
        targetModelIsDownloaded()
        refreshTranslatorClient()
        changeSourceText(newSourceText = if(isThisSwap) translatorState.value.targetText else translatorState.value.sourceText)
    }

    fun peekNewSourceLanguage(language: AvailableLanguage) {
        myLog("peekNewSourceLanguage")
        compare(
            language = language,
            cmpLanguage = translatorState.value.sourceLanguage,
            thatsTheTarget = false,
        )
    }

    // todo чекнуть смену языков по кнопке
    fun swapLanguages() {
        myLog("swapLanguages")
        compare(
            language = translatorState.value.targetLanguage,
            cmpLanguage = translatorState.value.sourceLanguage,
            thatsTheTarget = false,
            isThisSwap = true,
        )
    }

    fun peekNewTargetLanguage(language: AvailableLanguage) {
        myLog("peekNewTargetLanguage")
        compare(
            language = language,
            cmpLanguage = translatorState.value.targetLanguage,
            thatsTheTarget = true,
        )
    }

    fun launchDownload(language: AvailableLanguage, thatsTheTarget: Boolean) {
        downloadJob = launch {
            try {
                val result = downloadLanguage(language = language, thatsTheTarget = thatsTheTarget)
                if(result) llTranslatorFlowWrapper.updateDownloadedLanguages(getAllDownloadedModels())
            } catch(e: Exception) {
                // TODO
            }
        }
    }

    fun launchDelete(language: AvailableLanguage, thatsTheTarget: Boolean) {
        deleteJob = launch {
            try {
                deleteLanguage(language = language, thatsTheTarget = thatsTheTarget)
            } catch(e: Exception) {
                // TODO
            }
        }
    }

    private suspend fun getAllDownloadedModels(): List<AvailableLanguage> {
        myLog("getAllDownloadedModels started")
        return suspendCancellableCoroutine { continuation ->
            modelManager.getDownloadedModels(TranslateRemoteModel::class.java)
                .addOnSuccessListener { set ->
                    myLog("getAllDownloadedModels success")
                    set.forEach { myLog("language in Downloaded ${it.language}") }
                    continuation.resume(set.map {
                        AvailableLanguage(
                            LANGUAGE_MAP.getOrDefault(it!!.language, " "),
                            it.language
                        )
                    })
                }
                .addOnFailureListener {
                    myLog("getAllDownloadedModels addOnFailureListener $it")
                    // TODO проверка кеша
                    continuation.resume(listOf(AVAILABLE_TRANSLATOR_LANGUAGES[ENGLISH_IS_ALWAYS_DOWNLOADED]))
                }
            continuation.invokeOnCancellation {
                // TODO глянуть что это за хуйня
                myLog("getAllDownloadedModels invokeOnCancellation")
            }
        }
    }


    // попробовать отменить что будет
    private suspend fun downloadLanguage(
        language: AvailableLanguage,
        thatsTheTarget: Boolean
    ): Boolean {
        myLog("downloadLanguage called")
        return suspendCoroutine { continuation ->
            if(translatorState.value.downloadedLanguages.contains(language))
                return@suspendCoroutine
            myLog("downloadLanguage continue")
            val model = TranslateRemoteModel.Builder(language.alphaCode).build()
            modelManager.download(model, conditions)
                .addOnSuccessListener {
                    myLog("downloadLanguage success")

                    //llTranslatorFlowWrapper.addNewModelInDownloadedLanguages(language = language)
                    llTranslatorFlowWrapper.updateIsSomeModelDownloaded(
                        value = true,
                        thatsTheTarget = thatsTheTarget
                    )
                    llTranslatorFlowWrapper.updateIsModelsAreLoaded()
                    continuation.resume(true)
                }
                .addOnFailureListener { e ->
                    myLog("downloadLanguage fail")
                    throw e
                }
        }
    }


    private suspend fun deleteLanguage(
        language: AvailableLanguage,
        thatsTheTarget: Boolean
    ): Boolean {
        myLog("deleteLanguage called")
        return suspendCoroutine { continuation ->
            modelManager.deleteDownloadedModel(
                TranslateRemoteModel.Builder(language.alphaCode).build()
            )
                .addOnSuccessListener {
                    myLog("deleteLanguage success")
                    continuation.resume(true)
                }
                .addOnFailureListener {
                    myLog("deleteLanguage fail")
                    continuation.resume(false)
                }
        }
    }

    private fun translate() {
        translatorClient.translate(translatorState.value.sourceText)
            .addOnSuccessListener { translate ->
                myLog("translate success $translate $translatorClient")
                llTranslatorFlowWrapper.updateTargetText(text = translate ?: "")
            }
            .addOnFailureListener { e ->
                myLog("translate fail")
                throw e
            }
    }
}