package com.medvedev.domain.usecase

import com.medvedev.domain.repository.AccountRepository

class EnableAccountUseCase(private val repository: AccountRepository) {
    suspend operator fun invoke(username: String): Boolean =
        repository.enableAccount(username = username)
}