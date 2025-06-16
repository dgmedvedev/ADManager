package com.medvedev.presentation

import com.medvedev.factory.ViewModelFactory
import com.medvedev.presentation.ManagerState.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.awt.*
import java.awt.event.ActionListener
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import javax.imageio.ImageIO
import javax.swing.*

class ManagerPanel() : JPanel() {

    private val vm: ManagerViewModel by lazy { ViewModelFactory.getManagerViewModel() }
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)

    private val gbc = GridBagConstraints().apply {
        fill = GridBagConstraints.HORIZONTAL
        gridx = 0
        gridy = 0
        anchor = GridBagConstraints.NORTH
    }
    private var count: Int = 0
    private var backgroundImage: Image? = null
    private var imageOfSelectedCheckbox: Image? = null
    private var imageOfUnselectedCheckbox: Image? = null

    init {
        layout = GridBagLayout()
        try {
            backgroundImage = ImageIO.read(javaClass.getResource("/background.jpg"))
            imageOfUnselectedCheckbox = ImageIO.read(javaClass.getResource("/cross.png"))
            imageOfSelectedCheckbox = ImageIO.read(javaClass.getResource("/checkmark.png"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        createAndShowGUI()
        subscribeSharedFlow()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponents(g)
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

    private fun resizeIcon(icon: ImageIcon): ImageIcon {
        val img = icon.getImage()
        val newImg = img.getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_SMOOTH)
        return ImageIcon(newImg)
    }

    private fun createAndShowGUI() {
        val titleCheckTask = JLabel(SELECT_TASK).apply {
            font = Font(FONT_ARIAL, Font.BOLD, LARGE_FONT) // Шрифт Arial, жирный, размер 18
        }

        addTopInset(50)

        gbc.gridx++
        gbc.gridy++
        add(titleCheckTask, gbc)

        addTopInset(35)

        addTaskPanelWithTextField(TASK_1, HINT_ENTER_USERNAME)
        addTaskPanelWithTextField(TASK_2, HINT_ENTER_USERNAME)
        addTaskPanelWithTextField(TASK_3, HINT_ENTER_USERNAME)
        addTaskPanelWithTextField(TASK_4, HINT_ENTER_PERIOD)
        addTaskPanel(TASK_5)

        gbc.gridy++
        gbc.weighty = 1.0
        add(Box.createVerticalGlue(), gbc)
    }

    private fun addTaskPanelWithTextField(task: String, hint: String) {
        val checkBox = JCheckBox().apply {
            icon = resizeIcon(ImageIcon(imageOfUnselectedCheckbox))
            selectedIcon = resizeIcon(ImageIcon(imageOfSelectedCheckbox))
            isFocusPainted = false
            isBorderPainted = false
            isContentAreaFilled = false
            preferredSize = Dimension(ICON_WIDTH, ICON_HEIGHT)
            horizontalAlignment = SwingConstants.CENTER
            verticalAlignment = SwingConstants.CENTER
        }

        val taskLabel = JLabel(task).apply {
            val preferredSize = Dimension(LABEL_WIDTH, ICON_HEIGHT)
            this.preferredSize = preferredSize
            minimumSize = preferredSize
            font = Font(FONT_ARIAL, Font.PLAIN, MEDIUM_FONT)
        }

        val emptyLabel = JLabel().apply {
            val preferredSize = Dimension(BUTTON_WIDTH + BUTTON_HEIGHT, ICON_HEIGHT)
            this.preferredSize = preferredSize
            minimumSize = preferredSize
            font = Font(FONT_ARIAL, Font.PLAIN, MEDIUM_FONT)
        }

        val textField = JTextField(hint, COLUMNS).apply {
            font = Font(FONT_ARIAL, Font.PLAIN, SMALL_FONT)
            foreground = Color.GRAY
            preferredSize = Dimension(0, TEXT_FIELD_HEIGHT)
            isVisible = false
        }

        val executeButton = JButton(BUTTON_NAME).apply {
            preferredSize = Dimension(BUTTON_WIDTH, BUTTON_HEIGHT)
            isVisible = false
        }

        addTopInset(15)

        gbc.gridx = 0
        gbc.gridy++
        gbc.insets = Insets(0, 0, 0, INSET_OF_CHECKBOX) // Добавляем отступ CheckBox
        add(checkBox, gbc)

        gbc.gridx++
        gbc.insets = Insets(0, 0, 0, 0) // Сбрасываем отступы
        add(taskLabel, gbc)

        gbc.gridx++
        add(emptyLabel, gbc)

        addTopInset(2)

        gbc.gridx--
        gbc.gridy++
        add(textField, gbc)

        gbc.gridx++
        gbc.insets = Insets(0, INSET_OF_BUTTON, 0, 0) // Добавляем отступ Button
        add(executeButton, gbc)

        checkBox.addActionListener {
            textField.isVisible = checkBox.isSelected
            executeButton.isVisible = checkBox.isSelected
        }

        textField.addFocusListener(object : FocusListener {
            override fun focusGained(e: FocusEvent?) {
                if (textField.text == hint) {
                    textField.font = Font(FONT_ARIAL, Font.PLAIN, MEDIUM_FONT)
                    textField.foreground = Color.BLACK
                    textField.text = ""
                }
            }

            override fun focusLost(e: FocusEvent?) {
                if (textField.text.isEmpty()) {
                    textField.font = Font(FONT_ARIAL, Font.PLAIN, SMALL_FONT)
                    textField.foreground = Color.GRAY
                    textField.text = hint
                }
            }
        })

        executeButton.addActionListener(createButtonListener(textField, count++))
    }

    private fun addTaskPanel(task: String) {
        val checkBox = JCheckBox().apply {
            icon = resizeIcon(ImageIcon(imageOfUnselectedCheckbox))
            selectedIcon = resizeIcon(ImageIcon(imageOfSelectedCheckbox))
            isFocusPainted = false
            isBorderPainted = false
            isContentAreaFilled = false
            preferredSize = Dimension(ICON_WIDTH, ICON_HEIGHT)
            horizontalAlignment = SwingConstants.CENTER
            verticalAlignment = SwingConstants.CENTER
        }

        val taskLabel = JLabel(task).apply {
            val preferredSize = Dimension(LABEL_WIDTH, ICON_HEIGHT)
            this.preferredSize = preferredSize
            minimumSize = preferredSize
            font = Font(FONT_ARIAL, Font.PLAIN, MEDIUM_FONT)
        }

        val executeButton = JButton(BUTTON_NAME).apply {
            preferredSize = Dimension(BUTTON_WIDTH, BUTTON_HEIGHT)
            isVisible = false
        }

        addTopInset(15)

        gbc.gridx = 0
        gbc.gridy++
        gbc.insets = Insets(0, 0, 0, INSET_OF_CHECKBOX) // Добавляем отступ CheckBox
        add(checkBox, gbc)

        gbc.gridx++
        gbc.insets = Insets(0, 0, 0, 0) // Сбрасываем отступы
        add(taskLabel, gbc)

        gbc.gridx++
        gbc.insets = Insets(0, INSET_OF_BUTTON, 0, 0) // Добавляем отступ Button
        add(executeButton, gbc)

        checkBox.addActionListener {
            executeButton.isVisible = checkBox.isSelected
        }

        executeButton.addActionListener(createButtonListener(buttonNumber = count++))
    }

    private fun createButtonListener(textField: JTextField = JTextField(), buttonNumber: Int): ActionListener =
        ActionListener { event ->
            try {
                when (buttonNumber) {
                    0 -> vm.enableButtonIsPressed(username = textField.text)
                    1 -> vm.disableButtonIsPressed(username = textField.text)
                    2 -> vm.loadAccountInfoButtonIsPressed(username = textField.text)
                    3 -> vm.loadListUnusedAccountButtonIsPressed(month = textField.text)
                    4 -> vm.loadListDisabledAccountButtonIsPressed()
                }
            } catch (e: Exception) {
                showErrorMessage("Ошибка установки событий кнопки!")
                e.printStackTrace()
            }
        }

    private fun subscribeSharedFlow() {
        coroutineScope.launch {
            vm.state.collect { state ->
                when (state) {
                    is AccountInfoLoaded -> showInformationMessage(message = "Информация о \"${state.username}\" получена.")
                    is ListLoaded -> showInformationMessage(message = "Список получен.")
                    is EnablingAccount -> showInformationMessage(message = "Объект \"${state.username}\" доступен.")
                    is DisablingAccount -> showInformationMessage(message = "Объект \"${state.username}\" был отключен.")
                    is Error -> showErrorMessage(message = state.message)
                }
            }
        }
    }

    private fun showInformationMessage(message: String) {
        JOptionPane.showMessageDialog(null, message, MESSAGE_TITLE_AD, JOptionPane.INFORMATION_MESSAGE)
    }

    private fun showErrorMessage(message: String) {
        JOptionPane.showMessageDialog(null, message, MESSAGE_TITLE_ERROR, JOptionPane.ERROR_MESSAGE)
    }

    fun disposeManagerPanel() {
        coroutineScope.cancel()
        vm.cancelManagerCoroutineScope()
    }

    companion object {
        private const val SELECT_TASK = "Выберите задачу"
        private const val BUTTON_NAME = "OK"
        private const val BUTTON_HEIGHT = 24
        private const val BUTTON_WIDTH = 50
        private const val ICON_HEIGHT = 15
        private const val ICON_WIDTH = 15
        private const val LABEL_WIDTH = 270
        private const val TEXT_FIELD_HEIGHT = BUTTON_HEIGHT + 1

        private const val FONT_ARIAL = "Arial"
        private const val LARGE_FONT = 20
        private const val MEDIUM_FONT = 14
        private const val SMALL_FONT = 12
        private const val COLUMNS = 5
        private const val INSET_OF_BUTTON = 20
        private const val INSET_OF_CHECKBOX = 20

        private const val MESSAGE_TITLE_AD = "Доменные службы Active Directory"
        private const val MESSAGE_TITLE_ERROR = "Ошибка!"

        private const val HINT_ENTER_USERNAME = "Введите имя пользователя"
        private const val HINT_ENTER_PERIOD = "Введите период (месяцев)"

        private const val TASK_1 = "1. Разблокировать учетную запись"
        private const val TASK_2 = "2. Отключить учетную запись"
        private const val TASK_3 = "3. Информация об учетной записи"
        private const val TASK_4 = "4. Информация о 'мертвых душах'"
        private const val TASK_5 = "5. Список отключенных учетных записей"
    }
}