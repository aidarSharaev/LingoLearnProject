package ru.aidar.lingolearn.di

import android.content.Context
import com.google.mlkit.common.model.RemoteModelManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableStateFlow
import ru.aidar.lingolearn.base.BaseNetworkMonitor
import ru.aidar.lingolearn.component.text_to_speech.LlTextToSpeech
import ru.aidar.lingolearn.data.HUI
import ru.aidar.lingolearn.data.UserDataRepository
import ru.aidar.lingolearn.domain.network.ConnectivityManagerNetworkMonitor
import ru.aidar.lingolearn.translator.LingoLearnTranslator
import ru.aidar.lingolearn.translator.LlTranslatorFlowWrapper
import ru.aidar.lingolearn.translator.LlTranslatorState
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MLKitDI {
    @Provides
    @Singleton
    fun provideNetworkMonitor(
        @ApplicationContext context: Context
    ): BaseNetworkMonitor {
        return ConnectivityManagerNetworkMonitor(context = context)
    }

    @Provides
    @Singleton
    fun provideRepository(
        @ApplicationContext context: Context
    ): UserDataRepository {
        return HUI()
    }

    @Provides
    @Singleton
    fun provideTextToSpeech(
        @ApplicationContext context: Context
    ) = LlTextToSpeech(context = context)

    @Provides
    @Singleton
    fun provideLlTranslatorStateFlow() = MutableStateFlow(LlTranslatorState())

    @Provides
    @Singleton
    fun provideLlTranslatorFlowWrapper(
        mutableStateFlow: MutableStateFlow<LlTranslatorState>
    ) = LlTranslatorFlowWrapper(mutableStateFlow = mutableStateFlow)

    @Provides
    @Singleton
    fun provideTranslator(
        baseNetworkMonitor: BaseNetworkMonitor,
        flowWrapper: LlTranslatorFlowWrapper,
    ) = LingoLearnTranslator(
        networkMonitor = baseNetworkMonitor,
        modelManager = RemoteModelManager.getInstance(),
        llTranslatorFlowWrapper = flowWrapper,
    )
}
