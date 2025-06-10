package com.medvedev.domain.repository

interface AuthorizationRepository {
    suspend fun authorization(adminDn: String, adminPassword: String): Boolean
}