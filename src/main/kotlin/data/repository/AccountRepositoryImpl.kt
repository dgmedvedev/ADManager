package com.medvedev.data.repository

import com.medvedev.data.Constants
import com.medvedev.data.mapper.AccountMapper.toDomain
import com.medvedev.data.network.Connection
import com.medvedev.data.storage.FileStorage
import com.medvedev.domain.model.Account
import com.medvedev.domain.repository.AccountRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.directory.api.ldap.model.entry.DefaultModification
import org.apache.directory.api.ldap.model.entry.Modification
import org.apache.directory.api.ldap.model.entry.ModificationOperation
import org.apache.directory.api.ldap.model.exception.LdapException
import org.apache.directory.api.ldap.model.message.SearchRequest
import org.apache.directory.api.ldap.model.message.SearchRequestImpl
import org.apache.directory.api.ldap.model.message.SearchScope
import org.apache.directory.api.ldap.model.name.Dn

class AccountRepositoryImpl(private val fileStorage: FileStorage) : AccountRepository {

    val connection = Connection.getInstance()

    override suspend fun enableAccount(username: String): Boolean =
        withContext(Dispatchers.IO) {
            val dn = getAccountByName(username = username)?.dn
            // Создаем операцию разблокировки учетной записи
            val modificationEnableAccount: Modification = DefaultModification(
                ModificationOperation.REPLACE_ATTRIBUTE,
                Constants.ATTRIBUTE_ACCOUNT_CONTROL,
                Constants.ENABLE_ACCOUNT
            )
            connection.modify(dn, modificationEnableAccount)
            true
        }

    override suspend fun disableAccount(username: String): Boolean =
        withContext(Dispatchers.IO) {
            val dn = getAccountByName(username = username)?.dn
            // Создаем операцию блокировки учетной записи
            val modificationEnableAccount: Modification = DefaultModification(
                ModificationOperation.REPLACE_ATTRIBUTE,
                Constants.ATTRIBUTE_ACCOUNT_CONTROL,
                Constants.DISABLE_ACCOUNT
            )
            connection.modify(dn, modificationEnableAccount)
            true
        }

    override suspend fun loadAccountInfo(username: String) =
        withContext(Dispatchers.IO) {
            val info = getAccountByName(username = username)
            if (info != null) {
                println(info)
                fileStorage.saveInfoToFile(username, info.toString())
            } else {
                throw Exception("00000057")
            }
        }

    override suspend fun loadListDisabledAccount() {
        checkConnection()
        try {
            val result: MutableList<String> = mutableListOf()
            withContext(Dispatchers.Default) {
                val searchRequest: SearchRequest = SearchRequestImpl()
                    .setBase(Dn(Constants.BASE_DN))                        // Укажите базовый DN
                    .setFilter("(|(userAccountControl=514)(userAccountControl=66050))") // Укажите фильтр поиска
                    .setScope(SearchScope.SUBTREE)
                val searchResult = connection.search(searchRequest)
                while (searchResult.next()) {
                    val entry = searchResult.entry
                    val username = entry.get(Constants.ATTRIBUTE_ACCOUNT_NAME).string
                    result.add(username)
                }
                fileStorage.saveListToFile(list = result.sorted())
            }
        } catch (e: Exception) {
            println("Ошибка поиска отключенных учетных записей: ${e.message}")
        }
    }

    private suspend fun getAccountByName(username: String): Account? {
        checkConnection()
        var result: Account? = null
        // Имя пользователя, по которому будем искать его DN
        val sAMAccountName = username

        try {
            return withContext(Dispatchers.Default) {
                // Создание запроса на поиск
                val searchRequest: SearchRequest = SearchRequestImpl()
                    .setBase(Dn(Constants.BASE_DN)) // Укажите базовый DN
                    .setFilter("(${Constants.ATTRIBUTE_ACCOUNT_NAME}=$sAMAccountName)") // Укажите фильтр поиска
                    .setScope(SearchScope.SUBTREE)
//            .addAttributes("cn", "sn", "mail") // Укажите атрибуты, которые хотите получить

                val searchResult = connection.search(searchRequest)
                if (searchResult.next()) {
                    result = searchResult.entry.toDomain()
                }
                result
            }
        } catch (e: LdapException) {
            println("Ошибка поиска учетной записи по имени пользователя: ${e.message}")
        }
        return result
    }

    private fun checkConnection() {
        if (!connection.isAuthenticated) throw Exception("000004DC")
    }
}