package com.medvedev.factory

import com.medvedev.data.repository.AccountRepositoryImpl
import com.medvedev.data.repository.AdminRepositoryImpl
import com.medvedev.data.repository.AuthorizationRepositoryImpl
import com.medvedev.data.storage.AdminStorageImpl
import com.medvedev.data.storage.FileStorageImpl
import com.medvedev.domain.usecase.AuthorizationUseCase
import com.medvedev.domain.usecase.DisableAccountUseCase
import com.medvedev.domain.usecase.EnableAccountUseCase
import com.medvedev.domain.usecase.GetAdminUseCase
import com.medvedev.domain.usecase.LoadAccountInfoUseCase
import com.medvedev.domain.usecase.LoadListDisabledAccountUseCase
import com.medvedev.domain.usecase.LoadListUnusedAccountUseCase
import com.medvedev.domain.usecase.SaveAdminUseCase
import com.medvedev.presentation.LoginViewModel
import com.medvedev.presentation.ManagerViewModel

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
            loadListUnusedAccountUseCase = LoadListUnusedAccountUseCase(repository = accountRepository),
            loadListDisabledAccountUseCase = LoadListDisabledAccountUseCase(repository = accountRepository)
        )
    }
}