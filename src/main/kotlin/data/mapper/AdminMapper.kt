package com.medvedev.data.mapper

import com.medvedev.data.storage.model.AdminEntity
import com.medvedev.domain.model.Admin

object AdminMapper {
    fun Admin.toEntity(): AdminEntity = AdminEntity(
        dn = this.dn,
        password = this.password
    )

    fun AdminEntity.toDomain(): Admin = Admin(
        dn = this.dn,
        password = this.password
    )
}