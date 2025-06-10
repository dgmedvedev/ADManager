package com.medvedev.domain.model

data class Account(
    val name: String,
    val dn: String,
    val userAccountControl: String,
    val lastLogon: String,
    val badPasswordTime: String,
    val pwdLastSet: String,
    val logonCount: String
) {
    override fun toString(): String =
        """Информация об учетной записи "$name":
            
            DN: $dn
            Состояние учетной записи: $userAccountControl
            Последний вход в систему: $lastLogon
            Последняя неудачная попытка входа в систему: $badPasswordTime
            Дата изменения пароля учетной записи: $pwdLastSet
            Количество успешных входов в систему: $logonCount
        """
}