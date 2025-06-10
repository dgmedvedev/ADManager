package com.medvedev.domain.usecase

import com.medvedev.domain.repository.IpRepository

class CheckPingUseCase(private val repository: IpRepository) {
    suspend operator fun invoke(ip: String): Boolean =
        repository.checkPing(ip = ip)
}