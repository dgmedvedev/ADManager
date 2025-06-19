package com.medvedev.data.storage

interface FileStorage {
    fun saveInfoToFile(username: String, info: String)
    fun saveListToFile(list: List<String>, filePath: String)
}