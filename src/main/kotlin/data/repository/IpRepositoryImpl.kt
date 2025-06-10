package com.medvedev.data.repository

import com.medvedev.data.Constants
import com.medvedev.data.Errors
import com.medvedev.domain.repository.IpRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.*

class IpRepositoryImpl : IpRepository {
    override suspend fun checkPing(ip: String): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val inet = InetAddress.getByName(ip)
                inet.isReachable(2000) // 2000 - таймаут в миллисекундах
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }

    override suspend fun getExternalIp(): String =
        withContext(Dispatchers.IO) {
            var connection: HttpURLConnection? = null
            try {
                // URL сервиса, который возвращает ваш внешний IP-адрес
                val url: URL = URL(Constants.URL)
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = Constants.REQUEST_GET

                // Читаем ответ
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val ipAddress = reader.readLine()
                reader.close()

                println("${Constants.YOUR_EXTERNAL_IP} $ipAddress")
                ipAddress
            } catch (e: Exception) {
                System.err.println(Errors.EXTERNAL_IP_NOT_RECEIVED)
                e.printStackTrace()
                Constants.UNKNOWN_IP
            } finally {
                connection?.disconnect()
            }
        }

    override suspend fun getLocalIp(): String =
        withContext(Dispatchers.IO) {
            try {
                // Получаем локальный IP-адрес
                val localHost = InetAddress.getLocalHost()
                val ipAddress = localHost.hostAddress
                println("${Constants.YOUR_LOCAL_IP} $ipAddress")
                ipAddress
            } catch (e: UnknownHostException) {
                System.err.println(Errors.LOCAL_IP_NOT_RECEIVED)
                e.printStackTrace()
                Constants.UNKNOWN_IP
            }
        }

    override suspend fun sendWakeOnLan(ip: String, mac: String): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val macBytes = mac.split(":").map { it.toInt(16).toByte() }.toByteArray()
                val packetData = ByteArray(102); // 6 + 16 * 6 = 102
                // Заполняем первые 6 байт 0xFF
                for (i in 0..5) {
                    packetData[i] = 0xFF.toByte()
                }
                // Заполняем оставшиеся 96 байт MAC-адресом
                for (i in 6..101) {
                    packetData[i] = macBytes[i % 6]
                }

                // Получаем IP-адрес
                val address = InetAddress.getByName(ip)
                val socket = DatagramSocket()
                socket.send(DatagramPacket(packetData, packetData.size, address, 9)) // Порт 9 - стандартный для WoL
                socket.close()
                println("Wake-on-LAN пакет отправлен на $ip с MAC $mac")
                true
            } catch (e: Exception) {
                println("Ошибка отправки пакета WoL: ${e.message}")
                false
            }
        }
}