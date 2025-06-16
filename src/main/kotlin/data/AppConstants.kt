package com.medvedev.data

object Constants {
    const val ATTRIBUTE_LAST_LOGON: String = "lastLogon"
    const val ATTRIBUTE_ACCOUNT_NAME: String = "sAMAccountName"
    const val ATTRIBUTE_ACCOUNT_CONTROL: String = "userAccountControl"

    const val ENABLE_ACCOUNT: String = "66048"      // 512, 66048 - учетная запись активна
    const val DISABLE_ACCOUNT: String = "66050"     // 514, 66050 - учетная запись заблокирована

    const val REQUEST_GET = "GET"
    const val YOUR_LOCAL_IP = "Ваш локальный IP-адрес:"
    const val YOUR_EXTERNAL_IP = "Ваш внешний IP-адрес:"
    const val UNKNOWN_IP = "0.0.0.0"
    const val URL = "http://checkip.amazonaws.com/"

    const val BASE_DN: String =
        "OU=Belgiprodor,DC=bgd,DC=local"
    const val LDAP_SERVER_IP: String = "172.16.1.33"
    const val LDAP_SERVER_PORT: Int = 389
}

object Errors {
    const val LOCAL_IP_NOT_RECEIVED = "Не удалось получить локальный IP-адрес."
    const val EXTERNAL_IP_NOT_RECEIVED = "Не удалось получить внешний IP-адрес."
}