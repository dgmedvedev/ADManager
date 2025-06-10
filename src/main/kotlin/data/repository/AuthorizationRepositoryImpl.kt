package com.medvedev.data.repository

import com.medvedev.data.network.Connection
import com.medvedev.domain.repository.AuthorizationRepository
import org.apache.directory.api.ldap.model.exception.LdapException

class AuthorizationRepositoryImpl : AuthorizationRepository {
    override suspend fun authorization(adminDn: String, adminPassword: String): Boolean =
        try {
            val connection = Connection.getInstance()
            // Подключаемся к серверу с учетными данными администратора
            connection.bind(adminDn, adminPassword)
            true
        } catch (e: LdapException) {
            println("Ошибка авторизации: ${e.message}")
            false
        }
}