package ru.aidar.lingolearn.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.aidar.lingolearn.base.BaseNetworkMonitor
import ru.aidar.lingolearn.data.HUI
import ru.aidar.lingolearn.data.UserDataRepository
import ru.aidar.lingolearn.domain.network.ConnectivityManagerNetworkMonitor
import ru.aidar.lingolearn.translator.LingoLearnTranslator
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
    fun provideTranslator()
        = LingoLearnTranslator()



}
