package com.medvedev.presentation

import com.medvedev.factory.ViewModelFactory
import java.awt.*
import java.util.ResourceBundle
import javax.imageio.ImageIO
import javax.swing.*

class LoginPanel(private val constants: ResourceBundle, private val adminLoginSuccessListener: (() -> Unit)?) :
    JPanel() {

    private val vm: LoginViewModel by lazy { ViewModelFactory.getLoginViewModel() }
    private val gbc = GridBagConstraints().apply {
        // Устанавливаем параметры для выравнивания по центру
        fill = GridBagConstraints.HORIZONTAL
        gridx = 0
        gridy = 0
        anchor = GridBagConstraints.NORTH
    }
    private val textField = JTextField(COLUMNS)
    private var backgroundImage: Image? = null
    private var imageHiddenPassword: Image? = null
    private var imageOpenPassword: Image? = null
    private var countErrors = 0

    init {
        layout = GridBagLayout()
        try {
            backgroundImage = ImageIO.read(javaClass.getResource("/bgd.png"))
            imageHiddenPassword = ImageIO.read(javaClass.getResource("/passwordHidden.png"))
            imageOpenPassword = ImageIO.read(javaClass.getResource("/passwordOpen.png"))

        } catch (e: Exception) {
            e.printStackTrace()
        }
        vm.initAdmin { admin -> textField.text = admin.dn }
        createAndShowGUI()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponents(g)
        // Рисуем изображение на панели
        backgroundImage?.let {
            g.drawImage(it, 0, 0, width, height, this)
        }
    }

    private fun addTopInset(value: Int) {
        gbc.gridy++
        gbc.insets = Insets(value, 0, 0, 0) // Отступ сверху
        add(JLabel(), gbc) // Пустой JLabel для отступа
        gbc.insets = Insets(0, 0, 0, 0) // Сбрасываем отступы
    }

    private fun convertUTF8(text: String) =
        String(text.toByteArray(charset("ISO-8859-1")), charset("UTF-8"))

    private fun resizeIcon(icon: ImageIcon): ImageIcon {
        val img = icon.getImage()
        val newImg = img.getScaledInstance(WIDTH_ICON, HEIGHT_ICON, Image.SCALE_SMOOTH)
        return ImageIcon(newImg)
    }

    private fun createAndShowGUI() {
        val titleAuthorization = JLabel(AUTHORIZATION).apply {
            font = Font(FONT_ARIAL, Font.BOLD, LARGE_FONT) // Шрифт Arial, жирный, размер 18
        }
        val labelEnterDn = JLabel(ENTER_DN).apply {
            font = Font(FONT_ARIAL, Font.BOLD, SMALL_FONT)
        }
        textField.apply {
            preferredSize = Dimension(0, 30)
            font = Font(FONT_ARIAL, Font.PLAIN, MEDIUM_FONT)
        }
        val labelEnterPassword = JLabel(ENTER_PASSWORD).apply {
            font = Font(FONT_ARIAL, Font.BOLD, SMALL_FONT)
        }
        val passwordField = JPasswordField(COLUMNS).apply {
            preferredSize = Dimension(0, 30)
            font = Font(FONT_ARIAL, Font.PLAIN, MEDIUM_FONT)
            echoChar = '\u25CF' // u2022    u25CF     u2731       u2732
        }
        val checkBox = JCheckBox().apply {
            setIcon(resizeIcon(ImageIcon(imageHiddenPassword)))
            setSelectedIcon(resizeIcon(ImageIcon(imageOpenPassword)))
//          setSelectedIcon(resizeIcon(ImageIcon("src/main/resources/checkmark.png")))
            setBorderPainted(false)
            setContentAreaFilled(false)
            setFocusPainted(false)
            setSelected(false)
            preferredSize = Dimension(20, 20)
            horizontalAlignment = SwingConstants.CENTER
            verticalAlignment = SwingConstants.CENTER
        }
        val loginButton = JButton("Вход")
        val labelVersion = JLabel().apply {
            val value = constants.getString("app_version")

            println(value)

            text = convertUTF8(value)
            font = Font(FONT_ARIAL, Font.BOLD, FINE_PRINT)
            horizontalAlignment = SwingConstants.CENTER
            foreground = Color.decode(COLOR_VERSION)
        }

        addTopInset(120)

        gbc.gridx++
        gbc.gridy++
        add(titleAuthorization, gbc)

        addTopInset(50)

        gbc.gridy++
        add(labelEnterDn, gbc)
        addTopInset(2)
        gbc.gridy++
        add(textField, gbc)

        addTopInset(15)

        gbc.gridy++
        add(labelEnterPassword, gbc)

        addTopInset(2)

        gbc.gridy++
        add(passwordField, gbc)
        gbc.gridx++
        gbc.insets = Insets(5, 5, 0, 0) // Сбрасываем отступы
        add(checkBox, gbc)

        addTopInset(50)

        gbc.gridx--
        gbc.gridy++
        add(loginButton, gbc)

        gbc.gridy++
        gbc.weighty = 1.0
        add(Box.createVerticalGlue(), gbc)

        addTopInset(20)

        gbc.gridy++
        add(labelVersion, gbc)

        checkBox.addActionListener {
            passwordField.echoChar = if (checkBox.isSelected) 0.toChar() else '\u25CF'
        }

        loginButton.addActionListener {
            val dn = textField.text
            val password = String(passwordField.password)
            vm.loginButtonIsPressed(dn = dn, password = password) { isAuthorized ->
                if (isAuthorized) {
                    adminLoginSuccessListener?.invoke()
                } else {
                    when (countErrors++) {
                        in 0..2 -> showWarningMessage(message = INVALID_DATA)
                        else -> showWarningMessage(message = "$INVALID_DATA\n$ACCOUNT_IS_BLOCKED")
                    }
                }
            }
        }
    }

    private fun showWarningMessage(message: String) {
        JOptionPane.showMessageDialog(null, message, MESSAGE_TITLE_WARNING, JOptionPane.WARNING_MESSAGE)
    }

    fun disposeLoginPanel() {
        vm.cancelLoginCoroutineScope()
    }

    companion object {
        //        private const val APP_VERSION = "Версия: 1.0.1"
        private const val COLOR_VERSION = "#4D4D4D"

        private const val AUTHORIZATION = "Авторизация"
        private const val ENTER_DN = "DN доменного администратора:"
        private const val ENTER_PASSWORD = "Пароль:"

        private const val COLUMNS = 25
        private const val FONT_ARIAL = "Arial"
        private const val FINE_PRINT = 10
        private const val SMALL_FONT = 12
        private const val MEDIUM_FONT = 14
        private const val LARGE_FONT = 20
        private const val HEIGHT_ICON = 20
        private const val WIDTH_ICON = 20

        private const val ACCOUNT_IS_BLOCKED = "Возможно, ваша учетная запись заблокирована!"
        private const val INVALID_DATA = "Неверный DN или пароль!"
        private const val MESSAGE_TITLE_WARNING = "Внимание!"
    }
}