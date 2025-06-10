package com.medvedev.domain.usecase

import com.medvedev.domain.repository.IpRepository

class GetExternalIpUseCase(private val repository: IpRepository) {
    suspend operator fun invoke(): String =
        repository.getExternalIp()
}