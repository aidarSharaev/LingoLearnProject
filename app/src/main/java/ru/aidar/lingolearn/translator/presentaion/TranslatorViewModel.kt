package ru.aidar.lingolearn.translator.presentaion

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ru.aidar.lingolearn.component.text_to_speech.LlTextToSpeech
import ru.aidar.lingolearn.translator.LingoLearnTranslator
import ru.aidar.lingolearn.translator.LlTranslatorState
import ru.aidar.lingolearn.utils.AvailableLanguage
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class TranslatorViewModel @Inject constructor(
    private val llTranslator: LingoLearnTranslator,
    private val llTextToSpeech: LlTextToSpeech
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO


    //TODO узнать про смерть
    val translatorState: StateFlow<LlTranslatorState> = llTranslator.translatorState.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LlTranslatorState(),
    )

    fun download(language: AvailableLanguage) {
        llTranslator.downloadLanguage(language)
    }

    fun changeSourceText(newText: String) {
        llTranslator.changeSourceText(newText)
    }

    fun peekNewSourceLanguage(language: AvailableLanguage) {
        if(translatorState.value.targetLanguage != language)
            llTranslator.peekNewSourceLanguage(language)
        else
            swapSourceAndTargetLanguages()
    }

    fun peekNewTargetLanguage(language: AvailableLanguage) {
        if(translatorState.value.sourceLanguage != language) {
            Log.d("HAHA", "!=")

            llTranslator.peekNewTargetLanguage(language)
        } else {
            Log.d("HAHA", "==")

            swapSourceAndTargetLanguages()
        }
    }

    fun swapSourceAndTargetLanguages() {
        val temp = translatorState.value.sourceLanguage
        llTranslator.peekNewSourceLanguage(translatorState.value.targetLanguage)
        llTranslator.peekNewTargetLanguage(temp)
    }

    fun textToSpeech() {
        llTextToSpeech.readTheText(
            translatorState.value.sourceText,
            translatorState.value.sourceLanguage
        )
    }

    override fun onCleared() {
        Log.d("ViewModel", "onCleared")
        super.onCleared()
    }
}

/*class TranslatopViewModelWrapper(
    private val translatorState: StateFlow<LlTranslatorState>
) : BaseViewModelWrapper {

    override val job: Job?
        get() = TODO("Not yet implemented")


}*/

