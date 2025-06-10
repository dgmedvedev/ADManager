package com.medvedev.domain.repository

interface AccountRepository {
    suspend fun enableAccount(username: String): Boolean
    suspend fun disableAccount(username: String): Boolean
    suspend fun loadAccountInfo(username: String)
    suspend fun loadListDisabledAccount()
}