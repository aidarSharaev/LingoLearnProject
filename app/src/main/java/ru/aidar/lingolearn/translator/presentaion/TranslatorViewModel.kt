package ru.aidar.lingolearn.translator.presentaion

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ru.aidar.lingolearn.component.text_to_speech.LlTextToSpeech
import ru.aidar.lingolearn.translator.LingoLearnTranslator
import ru.aidar.lingolearn.translator.LlTranslatorState
import ru.aidar.lingolearn.utils.AvailableLanguage
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * 1) узнать про удаление английского языка
 * 2) job'oв дохуя
 * 3) пересмотреть init
 * 4) проверить checkDownloadedOnInit()
 * 5) проверить ввод какого-то языка, а затем смену локали целевой и вводной
 * 6) проверить эксешпены и добавить туда хэндлеры
 * 7) что будет на перевод китайского на англ без скаичивания
 * 8) удаление английского
 **/

@HiltViewModel
class TranslatorViewModel @Inject constructor(
    private val llTranslator: LingoLearnTranslator,
    private val llTextToSpeech: LlTextToSpeech,
) : ViewModel(), CoroutineScope {

    private val parentJob: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + parentJob

    init {
        myLog("TranslatorViewModel init")
        parentJob.invokeOnCompletion {
            myLog(
                "${parentJob.children.count()} " +
                    "${parentJob.isActive}"
            )
        }
    }

    //TODO узнать про смерть
    val translatorState: StateFlow<LlTranslatorState> = llTranslator.translatorState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LlTranslatorState(),
    )

    fun download(language: AvailableLanguage, thatsTheTarget: Boolean) {
        llTranslator.launchDownload(language = language, thatsTheTarget = thatsTheTarget)
    }

    fun delete(language: AvailableLanguage, thatsTheTarget: Boolean) {
        llTranslator.launchDelete(language = language, thatsTheTarget = thatsTheTarget)
    }

    fun changeSourceText(newText: String) {
        llTranslator.changeSourceText(newSourceText = newText)
    }

    fun peekNewSourceLanguage(language: AvailableLanguage) {
        llTranslator.peekNewSourceLanguage(language = language)
    }

    fun peekNewTargetLanguage(language: AvailableLanguage) {
        llTranslator.peekNewTargetLanguage(language = language)
    }

    fun swapLanguages() {
        llTranslator.swapLanguages()
    }

    fun textToSpeech(thatsIsSourceText: Boolean) {
        with(translatorState.value) {
            llTextToSpeech.readTheText(
                textToSpeak = if(thatsIsSourceText) sourceText else targetText,
                language = if(thatsIsSourceText) sourceLanguage else targetLanguage
            )
        }
    }

    override fun onCleared() {
        Log.d("ViewModel", "onCleared")
        parentJob.cancelChildren()
        parentJob.cancel()
        llTranslator.onCleared()
        super.onCleared()
    }

    private fun myLog(comment: String) {
        Log.d("TranslatorViewModel", comment)
    }
}
