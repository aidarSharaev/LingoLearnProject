package ru.aidar.lingolearn.translator

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.aidar.lingolearn.utils.AvailableLanguage
import ru.aidar.lingolearn.utils.Constants
import javax.inject.Inject

class LlTranslatorFlowWrapper @Inject constructor(
    private val mutableStateFlow: MutableStateFlow<LlTranslatorState>
) {
    fun getState() = mutableStateFlow.asStateFlow()

    fun updateSourceText(text: String) {
        mutableStateFlow.update {
            it.copy(sourceText = text)
        }
    }

    fun updateTargetText(text: String) {
        mutableStateFlow.update {
            it.copy(targetText = text)
        }
    }

    /*fun addNewModelInDownloadedLanguages(language: AvailableLanguage) {
        mutableStateFlow.update {
            it.copy(
                downloadedLanguages = mutableStateFlow.value.downloadedLanguages +
                    AvailableLanguage(
                        Constants.LANGUAGE_MAP.getOrDefault(language.alphaCode, ""),
                        language.alphaCode
                    )
            )
        }
    }*/

    fun clearAll() {
        mutableStateFlow.update {
            it.copy(sourceText = "", targetText = "")
        }
    }

    fun updateIsModelsAreLoaded() {
        mutableStateFlow.update {
            it.copy(
                isModelsAreLoaded = when(mutableStateFlow.value.isTargetModelDownloaded && mutableStateFlow.value.isSourceModelDownloaded) {
                    true -> Constants.LanguagesDownloadState.AllRight
                    else -> Constants.LanguagesDownloadState.OnlyOne
                }
            )
        }
    }

    fun updateIsSomeModelDownloaded(value: Boolean, thatsTheTarget: Boolean) {
        mutableStateFlow.update {
            when {
                thatsTheTarget -> it.copy(isTargetModelDownloaded = value)
                else -> it.copy(isSourceModelDownloaded = value)
            }
        }
        updateIsModelsAreLoaded()
    }

    fun updateSomeLanguage(language: AvailableLanguage, thatsTheTarget: Boolean) {
        mutableStateFlow.update {
            when {
                thatsTheTarget -> it.copy(targetLanguage = language)
                else -> it.copy(sourceLanguage = language)
            }
        }
    }

    fun updateDownloadedLanguages(downloadedLanguages: List<AvailableLanguage>) {
        mutableStateFlow.update {
            it.copy(downloadedLanguages = downloadedLanguages)
        }
    }

    fun swapLanguages() {
        val p1 = mutableStateFlow.value.sourceLanguage
        val p2 = mutableStateFlow.value.targetLanguage
        Log.d("swapAidar", "$p1, $p2 - end")

        mutableStateFlow.update {
            it.copy(sourceLanguage = p2, targetLanguage = p1)
        }
    }
}