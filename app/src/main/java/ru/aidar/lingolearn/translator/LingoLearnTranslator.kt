package ru.aidar.lingolearn.translator

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

data class LlTranslatorState(
    val availableModels: Set<String> = setOf(),
    val availableLanguages: List<LingoLearnLanguage> = listOf(),
    val sourceText: String = "",
    val targetText: String = "",
    val sourceLanguage: String = TranslateLanguage.RUSSIAN,
    val targetLanguage: String = TranslateLanguage.ENGLISH,
)

class LingoLearnTranslator() {

    fun provideState(): Flow<LlTranslatorState> {
        return flowOf(translatorState)
    }

    private val _mutableState: MutableStateFlow<LlTranslatorState> =
        MutableStateFlow(LlTranslatorState())

    val state = _mutableState.asStateFlow()

    companion object {
        private const val NUM_TRANSLATORS = 4
        private val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
    }

    private var translatorState = LlTranslatorState()
    private val modelManager: RemoteModelManager = RemoteModelManager.getInstance()

    /*
        private val translators =
            object : LruCache<TranslatorOptions, Translator>(NUM_TRANSLATORS) {

                override fun create(options: TranslatorOptions): Translator {
                    return Translation.getClient(options)
                }

                override fun entryRemoved(
                    evicted: Boolean,
                    key: TranslatorOptions?,
                    oldValue: Translator,
                    newValue: Translator?
                ) = oldValue.close()


            }
    */

    private fun fetchDownloadedModels() {
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
    }

    fun downloadLanguage(language: LingoLearnLanguage) {
        val model = getModel(TranslateLanguage.fromLanguageTag(language.code())!!)
    }

    private fun getModel(languageCode: String): TranslateRemoteModel {
        return TranslateRemoteModel.Builder(languageCode).build()
    }

    fun deleteLanguage(language: LingoLearnLanguage) {
        val model: TranslateRemoteModel =
            getModel(TranslateLanguage.fromLanguageTag(language.code())!!)
        modelManager.deleteDownloadedModel(model).addOnCompleteListener { fetchDownloadedModels() }
    }

    fun requiresModelDownload(
        lang: LingoLearnLanguage,
        downloadedModels: List<String?>?,
    ): Boolean {
        return if(downloadedModels == null) true
        else !downloadedModels.contains(lang.code())
    }

    suspend fun translate() {
        withContext(Dispatchers.Default) {
            val text = translatorState.sourceText
            val sourceLanguage = translatorState.sourceLanguage
            val targetLanguage = translatorState.targetText
            // todo глянуть
            if(sourceLanguage == null || targetLanguage == null || text.isNullOrEmpty()) {
                return@withContext
            }
            val sourceLangCode = TranslateLanguage.fromLanguageTag(sourceLanguage)!!
            val targetLangCode = TranslateLanguage.fromLanguageTag(targetLanguage)!!
            val options = TranslatorOptions.Builder()
                .setSourceLanguage(sourceLangCode)
                .setTargetLanguage(targetLangCode)
                .build()
        }
    }
}