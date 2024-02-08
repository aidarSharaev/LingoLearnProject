package ru.aidar.lingolearn.lingolearnapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.aidar.lingolearn.data.UserData
import ru.aidar.lingolearn.data.UserDataRepository
import javax.inject.Inject

@HiltViewModel
class LingoLearnMainActivityViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
) : ViewModel() {

    val state: StateFlow<LingoLearnMainActivityUiState> = userDataRepository.userData.map {
        LingoLearnMainActivityUiState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = LingoLearnMainActivityUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )
}


sealed interface LingoLearnMainActivityUiState {
    data object Loading : LingoLearnMainActivityUiState
    data class Success(val userData: UserData) : LingoLearnMainActivityUiState
}
