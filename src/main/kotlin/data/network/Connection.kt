package com.medvedev.data.network

import com.medvedev.data.Constants
import org.apache.directory.ldap.client.api.LdapConnection
import org.apache.directory.ldap.client.api.LdapConnectionConfig
import org.apache.directory.ldap.client.api.LdapNetworkConnection

object Connection {
    private const val LDAP_SERVER_IP: String = Constants.LDAP_SERVER_IP
    private const val LDAP_SERVER_PORT: Int = Constants.LDAP_SERVER_PORT
    private var instance: LdapConnection? = null

    fun getInstance(): LdapConnection {
        instance?.let { return it }
        synchronized(this) {
            instance?.let { return it }
            // Настройка соединения
            val config = LdapConnectionConfig().apply {
                ldapHost = LDAP_SERVER_IP
                ldapPort = LDAP_SERVER_PORT
                // Если требуется, добавьте настройки для SSL
            }
            // Установка соединения с LDAP
            val connection = LdapNetworkConnection(config) as LdapConnection
            instance = connection
            return connection
        }
    }
}