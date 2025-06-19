package com.medvedev.data.storage

import java.io.File

class FileStorageImpl : FileStorage {
    override fun saveInfoToFile(username: String, info: String) {
        try {
            val path = String.format(ACCOUNT_INFO_FILE_PATH, username)
            val file = File(path)
            // Создаем директорию, если она не существует
            file.parentFile.mkdirs()
            // Записываем текст в файл
            file.printWriter().use { out ->
                out.println(info)
            }
            // Запускаем Блокнот с указанным файлом
            val processBuilder = ProcessBuilder(NOTEPAD_LAUNCHER, file.path)
            processBuilder.start()
        } catch (e: Exception) {
            println("Произошла ошибка при сохранении/открытии файла: ${e.message}")
        }
    }

    override fun saveListToFile(list: List<String>, filePath: String) {
        try {
            val file = File(filePath)
            file.parentFile.mkdirs()
            file.printWriter().use { out ->
                list.forEach { username ->
                    out.println(username)
                }
            }
            val processBuilder = ProcessBuilder(NOTEPAD_LAUNCHER, file.absolutePath)
            processBuilder.start()
        } catch (e: Exception) {
            println("Произошла ошибка при сохранении/открытии файла: ${e.message}")
        }
    }

    companion object {
        private const val ACCOUNT_INFO_FILE_PATH = "Files/AccountInfo(%s).txt"
        private const val NOTEPAD_LAUNCHER = "notepad.exe"
    }
}