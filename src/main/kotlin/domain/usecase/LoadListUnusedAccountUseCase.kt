package com.medvedev.domain.usecase

import com.medvedev.domain.repository.AccountRepository

class LoadListUnusedAccountUseCase(private val repository: AccountRepository) {
    suspend operator fun invoke(month: Int) =
        repository.loadListUnusedAccount(month = month)
}