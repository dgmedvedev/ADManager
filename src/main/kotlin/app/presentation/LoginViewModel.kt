package com.medvedev.app.presentation

import com.medvedev.domain.model.Admin
import com.medvedev.domain.usecase.AuthorizationUseCase
import com.medvedev.domain.usecase.GetAdminUseCase
import com.medvedev.domain.usecase.SaveAdminUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class LoginViewModel(
    private val getAdminUseCase: GetAdminUseCase,
    private val saveAdminUseCase: SaveAdminUseCase,
    private val authorizationUseCase: AuthorizationUseCase
) {
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    fun initAdmin(callback: (Admin) -> Unit) {
        coroutineScope.launch {
            val admin = getAdminUseCase()
            callback.invoke(admin)
        }
    }

    fun loginButtonIsPressed(dn: String, password: String, callback: (Boolean) -> Unit) {
        if (!dn.isBlank() && !password.isBlank()) {
            coroutineScope.launch {
                val successfulAuthorization = authorizationUseCase(dn = dn, password = password)
                callback.invoke(successfulAuthorization && saveAdminUseCase(Admin(dn = dn, password = password)))
            }
        } else {
            callback.invoke(false)
        }
    }

    fun cancelLoginCoroutineScope() {
        coroutineScope.cancel()
    }
}