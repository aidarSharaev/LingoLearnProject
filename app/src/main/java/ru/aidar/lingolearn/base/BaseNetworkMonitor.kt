package ru.aidar.lingolearn.base

import kotlinx.coroutines.flow.Flow

interface BaseNetworkMonitor {
    val isOnline: Flow<Boolean>
}