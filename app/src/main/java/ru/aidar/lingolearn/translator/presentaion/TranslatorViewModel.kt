package ru.aidar.lingolearn.translator.presentaion

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ru.aidar.lingolearn.base.BaseViewModelWrapper
import ru.aidar.lingolearn.translator.LingoLearnTranslator
import ru.aidar.lingolearn.translator.LlTranslatorState
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class TranslatorViewModel @Inject constructor(
    private val llTranslator: LingoLearnTranslator
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    val translatorState: StateFlow<LlTranslatorState> = llTranslator.provideState().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LlTranslatorState(),
    )

    init {
        Log.d("ViewModel", translatorState.value.toString())
    }

    override fun onCleared() {
        Log.d("ViewModel", "onCleared")
    }
}

/*class TranslatopViewModelWrapper(
    private val translatorState: StateFlow<LlTranslatorState>
) : BaseViewModelWrapper {

    override val job: Job?
        get() = TODO("Not yet implemented")


}*/

