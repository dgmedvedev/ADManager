package com.medvedev.domain.usecase

import com.medvedev.domain.repository.AccountRepository

class LoadAccountInfoUseCase(private val repository: AccountRepository) {
    suspend operator fun invoke(username: String) =
        repository.loadAccountInfo(username = username)
}