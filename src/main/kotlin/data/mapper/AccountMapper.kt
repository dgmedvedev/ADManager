package com.medvedev.data.mapper

import com.medvedev.domain.model.Account
import org.apache.directory.api.ldap.model.entry.Entry
import java.text.SimpleDateFormat
import java.util.*

object AccountMapper {
    private const val DATE_FORMAT = "dd.MM.yyyy"
    private const val FROM_WINDOWS_TIME_TO_UNIX = 11644473600000L

    fun Entry.toDomain(): Account = Account(
        name = this.get("sAMAccountName").string,
        dn = this.dn.toString(),
        userAccountControl = accountState(this.get("userAccountControl").string),
        lastLogon = correctLastLogon(this.get("lastLogon").string, this),
        badPasswordTime = convertWindowsTimeToDate(this.get("badPasswordTime").string),
        pwdLastSet = convertWindowsTimeToDate(this.get("pwdLastSet").string),
        logonCount = this.get("logonCount").string
    )

    private fun accountState(userAccountControl: String) =
        when (userAccountControl) {
            "514", "66050" -> "отключена"
            else -> "активна"
        }

    private fun correctLastLogon(value: String, entry: Entry): String =
        convertWindowsTimeToDate(value).ifBlank {
            convertWindowsTimeToDate(entry.get("lastLogonTimestamp").string)
        }

    fun convertWindowsTimeToDate(windowsFileTime: String): String {
        val valueToLong = windowsFileTime.toLong()
        if (valueToLong == 0L) return ""
        val milliseconds = valueToLong / 10000 - FROM_WINDOWS_TIME_TO_UNIX
        val date = Date(milliseconds)
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(date)
    }
}