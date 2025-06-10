package com.medvedev.app.presentation

sealed class ManagerState {
    object ListLoaded : ManagerState()
    data class AccountInfoLoaded(val username: String) : ManagerState()
    data class EnablingAccount(val username: String) : ManagerState()
    data class DisablingAccount(val username: String) : ManagerState()
    data class Error(val message: String) : ManagerState()
}