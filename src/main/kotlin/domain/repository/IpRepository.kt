package com.medvedev.domain.repository

interface IpRepository {
    suspend fun checkPing(ip: String): Boolean
    suspend fun getExternalIp(): String
    suspend fun getLocalIp(): String
    suspend fun sendWakeOnLan(ip: String, mac: String): Boolean
}