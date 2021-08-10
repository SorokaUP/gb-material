package ru.sorokin.gb_material.viewmodel

sealed class AppState {
    data class Success<TResponse>(val response: TResponse) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}