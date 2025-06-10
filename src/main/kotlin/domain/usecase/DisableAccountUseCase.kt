package com.medvedev.domain.usecase

import com.medvedev.domain.repository.AccountRepository

class DisableAccountUseCase(private val repository: AccountRepository) {
    suspend operator fun invoke(username: String): Boolean =
        repository.disableAccount(username = username)
}