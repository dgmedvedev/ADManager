package com.medvedev.data.storage

import com.medvedev.data.storage.model.AdminEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.util.*

class AdminStorageImpl : AdminStorage {
    private val properties: Properties = Properties()

    override suspend fun save(adminEntity: AdminEntity): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val file = File(FILE_NAME)
                file.parentFile.mkdirs()
                FileOutputStream(FILE_NAME).use { output ->
                    OutputStreamWriter(output, Charsets.UTF_8).use { writer ->
                        properties.setProperty(KEY_DN, adminEntity.dn)
                        properties.setProperty(KEY_PASSWORD, "")
                        properties.store(writer, null)
                    }
                    true
                }
            } catch (e: IOException) {
                println("Ошибка сохранения AdminEntity: ${e.message}")
                false
            }
        }

    override suspend fun get(): AdminEntity =
        withContext(Dispatchers.IO) {
            try {
                FileInputStream(FILE_NAME).use { input ->
                    InputStreamReader(input, Charsets.UTF_8).use { reader ->
                        properties.load(reader)
                    }
                    val dn = properties.getProperty(KEY_DN)
                    val password = properties.getProperty(KEY_PASSWORD)
                    AdminEntity(dn, password)
                }
            } catch (e: IOException) {
                println("Ошибка получения AdminEntity: ${e.message}")
                AdminEntity()
            }
        }

    companion object {
        private const val FILE_NAME = "Config/config.properties"
        private const val KEY_DN = "DN"
        private const val KEY_PASSWORD = "password"
    }
}