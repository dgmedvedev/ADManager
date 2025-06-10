package com.medvedev.domain.usecase

import com.medvedev.domain.repository.AuthorizationRepository

class AuthorizationUseCase(private val repository: AuthorizationRepository) {
    suspend operator fun invoke(dn: String, password: String): Boolean =
        repository.authorization(adminDn = dn, adminPassword = password)
}