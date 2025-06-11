package com.medvedev.presentation

import com.medvedev.domain.usecase.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class ManagerViewModel(
    private val enableAccountUseCase: EnableAccountUseCase,
    private val disableAccountUseCase: DisableAccountUseCase,
    private val loadAccountInfoUseCase: LoadAccountInfoUseCase,
    private val loadListDisabledAccountUseCase: LoadListDisabledAccountUseCase
) {
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private var _state: MutableSharedFlow<ManagerState> = MutableSharedFlow()
    val state: SharedFlow<ManagerState> get() = _state

    fun enableButtonIsPressed(username: String) {
        coroutineScope.launch {
            try {
                if (!username.isBlank()) {
                    enableAccountUseCase(username = username.trim())
                    _state.emit(ManagerState.EnablingAccount(username))
                }
            } catch (e: Exception) {
                val message = handleError(exception = e, username = username)
                _state.emit(ManagerState.Error(message = message))
            }
        }
    }

    fun disableButtonIsPressed(username: String) {
        coroutineScope.launch {
            try {
                if (!username.isBlank()) {
                    disableAccountUseCase(username = username)
                    _state.emit(ManagerState.DisablingAccount(username))
                }
            } catch (e: Exception) {
                val message = handleError(exception = e, username = username)
                _state.emit(ManagerState.Error(message = message))
            }
        }
    }

    fun loadAccountInfoButtonIsPressed(username: String) {
        coroutineScope.launch {
            try {
                if (!username.isBlank()) {
                    loadAccountInfoUseCase(username = username)
                    _state.emit(ManagerState.AccountInfoLoaded(username = username))
                }
            } catch (e: Exception) {
                val message = handleError(exception = e, username = username)
                _state.emit(ManagerState.Error(message = message))
            }
        }
    }

    fun loadListDisabledAccountButtonIsPressed() {
        coroutineScope.launch {
            try {
                loadListDisabledAccountUseCase()
                _state.emit(ManagerState.ListLoaded)
            } catch (e: Exception) {
                val message = handleError(exception = e)
                _state.emit(ManagerState.Error(message = message))
            }
        }
    }

    fun cancelManagerCoroutineScope() {
        coroutineScope.cancel()
    }

    private fun handleError(exception: Exception, username: String = ""): String {
        val exceptionNumber =
            exception.message?.let { message -> if (message.length >= 8) message.substring(0, 8) else "unknown" }
        return when (exceptionNumber) {
            "00000057" -> "Пользователь \"${username.trim()}\" не найден!"
            "000004DC" -> "В целях безопасности, время сессии истекло!\nПройдите повторную аутентификацию!"
            "00002098" -> "У авторизованного пользователя\nнедостаточно прав доступа!"
            "80090308" -> "Неверный логин/пароль!"
            else -> "Ошибка выполнения!"
        }
    }
}