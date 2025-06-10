package com.medvedev.app.presentation

import com.medvedev.data.repository.AccountRepositoryImpl
import com.medvedev.data.repository.AdminRepositoryImpl
import com.medvedev.data.repository.AuthorizationRepositoryImpl
import com.medvedev.data.storage.AdminStorageImpl
import com.medvedev.data.storage.FileStorageImpl
import com.medvedev.domain.usecase.*

object ViewModelFactory {
    fun getLoginViewModel(): LoginViewModel {
        val adminStorage: AdminStorageImpl by lazy { AdminStorageImpl() }
        val adminRepository: AdminRepositoryImpl by lazy { AdminRepositoryImpl(adminStorage) }
        val authorizationRepository: AuthorizationRepositoryImpl by lazy { AuthorizationRepositoryImpl() }

        return LoginViewModel(
            getAdminUseCase = GetAdminUseCase(repository = adminRepository),
            saveAdminUseCase = SaveAdminUseCase(repository = adminRepository),
            authorizationUseCase = AuthorizationUseCase(repository = authorizationRepository)
        )
    }

    fun getManagerViewModel(): ManagerViewModel {
        val fileStorage: FileStorageImpl by lazy { FileStorageImpl() }
        val accountRepository by lazy { AccountRepositoryImpl(fileStorage) }

        return ManagerViewModel(
            enableAccountUseCase = EnableAccountUseCase(repository = accountRepository),
            disableAccountUseCase = DisableAccountUseCase(repository = accountRepository),
            loadAccountInfoUseCase = LoadAccountInfoUseCase(repository = accountRepository),
            loadListDisabledAccountUseCase = LoadListDisabledAccountUseCase(repository = accountRepository)
        )
    }
}