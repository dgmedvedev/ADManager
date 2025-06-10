package com.medvedev.data.storage

import com.medvedev.data.storage.model.AdminEntity

interface AdminStorage {
    suspend fun save(adminEntity: AdminEntity): Boolean
    suspend fun get(): AdminEntity
}