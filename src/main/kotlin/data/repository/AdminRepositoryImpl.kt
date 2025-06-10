package com.medvedev.data.repository

import com.medvedev.data.mapper.AdminMapper.toDomain
import com.medvedev.data.mapper.AdminMapper.toEntity
import com.medvedev.data.storage.AdminStorage
import com.medvedev.domain.model.Admin
import com.medvedev.domain.repository.AdminRepository

class AdminRepositoryImpl(private val adminStorage: AdminStorage) : AdminRepository {
    override suspend fun saveAdmin(admin: Admin): Boolean {
        if (admin.dn.isBlank() || admin.password.isBlank()) return false
        adminStorage.save(adminEntity = admin.toEntity())
        return true
    }

    override suspend fun getAdmin(): Admin {
        return adminStorage.get().toDomain()
    }
}