package com.medvedev.domain.repository

import com.medvedev.domain.model.Admin

interface AdminRepository {
    suspend fun saveAdmin(admin: Admin): Boolean
    suspend fun getAdmin(): Admin
}