package com.medvedev.domain.usecase

import com.medvedev.domain.repository.IpRepository

class SendWakeOnLanUseCase(private val repository: IpRepository) {
    suspend operator fun invoke(ip: String, mac: String): Boolean =
        repository.sendWakeOnLan(ip = ip, mac = mac)
}