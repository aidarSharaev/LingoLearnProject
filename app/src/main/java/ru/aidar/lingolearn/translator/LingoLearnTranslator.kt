package ru.aidar.lingolearn.translator

import android.util.Log
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.aidar.lingolearn.utils.AvailableLanguage
import ru.aidar.lingolearn.utils.Constants.AVAILABLE_TRANSLATOR_LANGUAGES

data class LlTranslatorState(
    val downloadedLanguages: MutableList<AvailableLanguage> = mutableListOf(),
    val sourceText: String = "",
    val targetText: String = "",
    val sourceLanguage: AvailableLanguage = AvailableLanguage("RUSSIAN", "ru"),
    val targetLanguage: AvailableLanguage = AvailableLanguage("ENGLISH", "en"),
    val isSourceModelDownloaded: Boolean = false,
    val isTargetModelDownloaded: Boolean = false,
)

class LingoLearnTranslator() {

    init {

    }

    private val _translatorState = MutableStateFlow(LlTranslatorState())
    var translatorState = _translatorState.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.IO)

    private fun isSourceModelIsDownloaded() {
        val src: Boolean
        with(_translatorState.value) { src = downloadedLanguages.contains(sourceLanguage) }
        _translatorState.update { it.copy(isSourceModelDownloaded = src) }

    }

    private fun isTargetModelIsDownloaded() {
        val trgt: Boolean
        with(_translatorState.value) { trgt = downloadedLanguages.contains(targetLanguage) }
        _translatorState.update { it.copy(isTargetModelDownloaded = trgt) }

    }


    fun changeSourceText(newSourceText: String) {
        _translatorState.update { it.copy(sourceText = newSourceText) }
    }

    fun peekNewSourceLanguage(language: AvailableLanguage) {
        _translatorState.update { it.copy(sourceLanguage = language) }
        isSourceModelIsDownloaded()
        Log.d("HAHA", _translatorState.value.sourceLanguage.toString())
    }

    fun peekNewTargetLanguage(language: AvailableLanguage) {
        _translatorState.update { it.copy(targetLanguage = language) }
        isTargetModelIsDownloaded()
        Log.d("HAHA", _translatorState.value.targetLanguage.toString())
    }

    init {
        val job = scope.launch {
            getAllDownloadedModels().collect {
                _translatorState.value.downloadedLanguages.add(it)
            }
        }
    }

    companion object {
        private const val NUM_TRANSLATORS = 4
        private val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
    }

    private val modelManager: RemoteModelManager = RemoteModelManager.getInstance()

    fun getAllDownloadedModels() = callbackFlow {
        repeat(AVAILABLE_TRANSLATOR_LANGUAGES.size) { int ->
            val model =
                TranslateRemoteModel.Builder(AVAILABLE_TRANSLATOR_LANGUAGES[int].alphaCode)
                    .build()
            modelManager.isModelDownloaded(model).addOnSuccessListener {
                if(it == true) trySend(AVAILABLE_TRANSLATOR_LANGUAGES[int])
            }
        }
        awaitClose()
    }
    /*
    suspend fun getAllDownloadedModels() {
            repeat(AVAILABLE_TRANSLATOR_LANGUAGES.size) { int ->
                withContext(Dispatchers.IO) {
                    val model =
                        TranslateRemoteModel.Builder(AVAILABLE_TRANSLATOR_LANGUAGES[int].alphaCode)
                            .build()
                    modelManager.isModelDownloaded(model).addOnSuccessListener {
                        if(it == true)
                    }
                }
            }
        }
        */


//

    /* private fun fetchDownloadedModels() {
         modelManager.getDownloadedModels(TranslateRemoteModel::class.java)
             .addOnSuccessListener { remoteModels ->
                 translatorState = translatorState.copy(availableModels = remoteModels
                     .sortedBy { it.language }
                     .map { it.language }
                     .toSet())
             }
             .addOnCanceledListener {
                 translatorState = translatorState.copy(availableModels = mutableSetOf())
             }
     }*/


    fun downloadLanguage(language: AvailableLanguage) {
        scope.launch {
            Log.d("HAHA", "start")
            if(_translatorState.value.downloadedLanguages.contains(language)) cancel()
            else {
                Log.d("HAHA", "after")
                if(conditions.isWifiRequired) {
                    Log.d("HAHA", "isWifiRequired")
                } else{
                    Log.d("HAHA", "loch pidor chmo0")
                }
                val model = TranslateRemoteModel.Builder(language.alphaCode).build()
                modelManager.download(model, conditions)
                    .addOnSuccessListener {
                        Log.d("HAHA", "success")
                        //getAllDownloadedModels()
                    }
                    .addOnFailureListener {
                        Log.d("HAHA", "fail")
                    }
                    .addOnCanceledListener {
                        Log.d("HAHA", "cancel")
                    }
                    .addOnCompleteListener {
                        Log.d("HAHA", "compolere")

                    }
            }
        }
    }

    /*private fun getModel(languageCode: String): TranslateRemoteModel {
        return TranslateRemoteModel.Builder(languageCode).build()
    }*/

    /*fun deleteLanguage(language: LingoLearnLanguage) {
        val model: TranslateRemoteModel =
            getModel(TranslateLanguage.fromLanguageTag(language.code())!!)
        modelManager.deleteDownloadedModel(model).addOnCompleteListener { fetchDownloadedModels() }
    }*/

    /*fun requiresModelDownload(
        lang: LingoLearnLanguage,
        downloadedModels: List<String?>?,
    ): Boolean {
        return if(downloadedModels == null) true
        else !downloadedModels.contains(lang.code())
    }*/

    /*suspend fun translate() {
        withContext(Dispatchers.Default) {
            val text = translatorState.sourceText
            val sourceLanguage = translatorState.sourceLanguage
            val targetLanguage = translatorState.targetLanguage
            // todo глянуть
            //            if(sourceLanguage. == null || targetLanguage == null || text.isNullOrEmpty()) {
            //                return@withContext
            //            }
            val sourceLangCode = TranslateLanguage.fromLanguageTag(sourceLanguage.alphaCode)!!
            val targetLangCode = TranslateLanguage.fromLanguageTag(targetLanguage.alphaCode)!!
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(sourceLangCode)
                .setTargetLanguage(targetLangCode)
                .build()
        }
    }*/
}