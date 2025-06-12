package com.medvedev.presentation

import com.medvedev.data.network.Connection
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.util.*
import javax.imageio.ImageIO
import javax.swing.JFrame
import javax.swing.JOptionPane

class MainFrame(title: String) : JFrame(title) {
    private var loginPanel: LoginPanel? = null
    private var managerPanel: ManagerPanel? = null
    private val currentLocale = Locale("ru", "RU")
    private val constants = ResourceBundle.getBundle("constants", currentLocale)

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        size = Dimension(500, 500)
        isResizable = false                         // Запрещает изменение окна пользователем
        setLocationRelativeTo(null)                 // Открывает окно по центру экрана

        try {
            iconImage = ImageIO.read(javaClass.getResource("/manager.png"))
        } catch (e: Exception) {
            e.printStackTrace()
            JOptionPane.showMessageDialog(null, "Ошибка загрузки иконки приложения!")
        }

        loginPanel = LoginPanel(constants, adminLoginSuccessListener()).apply {
            contentPane.add(this)
        }
        isVisible = true

        addWindowListener(object : WindowAdapter() {
            // Закрытие приложения. Освобождение ресурсов...
            override fun windowClosing(e: WindowEvent?) {
                Connection.getInstance().apply {
                    if (isConnected) unBind()
                    close()
                }
                loginPanel?.disposeLoginPanel()
                managerPanel?.disposeManagerPanel()
            }
        })
    }

    private fun adminLoginSuccessListener(): () -> Unit = {
        managerPanel = ManagerPanel()
        contentPane.removeAll()             // Очистка панели
        contentPane.add(managerPanel)       // Добавление новой панели
        revalidate()                        // Обновление панели
        repaint()                           // Перерисовка панели
    }
}