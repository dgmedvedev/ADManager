package com.medvedev.domain.usecase

import com.medvedev.domain.repository.AccountRepository

class LoadListDisabledAccountUseCase(private val repository: AccountRepository) {
    suspend operator fun invoke() =
        repository.loadListDisabledAccount()
}