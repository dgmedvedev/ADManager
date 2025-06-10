package com.medvedev.domain.usecase

import com.medvedev.domain.model.Admin
import com.medvedev.domain.repository.AdminRepository

class SaveAdminUseCase(private val repository: AdminRepository) {
    suspend operator fun invoke(admin: Admin): Boolean =
        repository.saveAdmin(admin = admin)
}