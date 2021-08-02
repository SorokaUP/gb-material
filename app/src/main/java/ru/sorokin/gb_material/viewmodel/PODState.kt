package ru.sorokin.gb_material.viewmodel

import ru.sorokin.gb_material.model.PODServerResponseData

sealed class PODState {
    data class Success(val serverResponseData: PODServerResponseData) : PODState()
    data class Error(val error: Throwable) : PODState()
    data class Loading(val progress: Int?) : PODState()
}