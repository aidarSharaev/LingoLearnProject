package ru.aidar.lingolearn.base

import kotlinx.coroutines.Job

interface BaseViewModelWrapper {
    val job: Job
}