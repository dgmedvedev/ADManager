package com.medvedev.domain.usecase

import com.medvedev.domain.model.Admin
import com.medvedev.domain.repository.AdminRepository

class GetAdminUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(): Admin =
        repository.getAdmin()
}