package com.xenon.calculator

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.forEach
import com.google.android.material.appbar.MaterialToolbar
import com.xenon.calculator.activities.SettingsActivity
import com.xenon.calculator.databinding.ActivityMainBinding
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

@Suppress("DEPRECATION", "unused", "UNUSED_PARAMETER")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var vibrator: Vibrator
    private var canAddOperation = false
    private var canAddDecimal = true
    private var openParentheses = true
    private var isRadians = true

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator

        val screenLayout = findViewById<ConstraintLayout>(R.id.screen)

        screenLayout.post {
            val screenHeight = screenLayout.rootView.height
            val newHeight = (screenHeight * 0.3).toInt()

            val params = screenLayout.layoutParams
            params.height = newHeight
            screenLayout.layoutParams = params
        }


        val toggleScientificButton = findViewById<Button>(R.id.toggleScientificButton)
        val scientificButtonsLayout = findViewById<ConstraintLayout>(R.id.scientificButtonsLayout)

        binding.toggleScientificButtonImageView.rotationX = 180f

        toggleScientificButton.setOnClickListener {
            if (scientificButtonsLayout.visibility == View.VISIBLE) {

                val fadeOut = ObjectAnimator.ofFloat(scientificButtonsLayout, "alpha", 1f, 0f)
                val slideUp = ObjectAnimator.ofFloat(
                    scientificButtonsLayout,
                    "translationY",
                    0f,
                    -scientificButtonsLayout.height.toFloat()
                )

                val fadeOutDuration = 200L
                val slideUpDuration = 300L

                fadeOut.duration = fadeOutDuration
                slideUp.duration = slideUpDuration

                val animatorSet = AnimatorSet()
                animatorSet.playTogether(fadeOut, slideUp)
                animatorSet.startDelay = slideUpDuration / 2

                animatorSet.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        scientificButtonsLayout.visibility = View.GONE
                    }
                })

                animatorSet.start()

                val flipImageUp = ObjectAnimator.ofFloat(
                    binding.toggleScientificButtonImageView, "rotationX", 180f, 0f
                )
                flipImageUp.duration = 300
                flipImageUp.start()


                binding.root.forEach { view ->
                    if (view is Button && view != toggleScientificButton) {
                        val slideUpOther = ObjectAnimator.ofFloat(
                            view, "translationY", 0f, -scientificButtonsLayout.height.toFloat()
                        )
                        slideUpOther.duration = slideUpDuration
                        slideUpOther.start()
                    }
                }
            } else {

                val fadeIn = ObjectAnimator.ofFloat(scientificButtonsLayout, "alpha", 0f, 1f)
                val slideDown = ObjectAnimator.ofFloat(
                    scientificButtonsLayout,
                    "translationY",
                    -scientificButtonsLayout.height.toFloat(),
                    0f
                )

                val fadeInDuration = 400L
                val slideDownDuration = 300L

                fadeIn.duration = fadeInDuration
                slideDown.duration = slideDownDuration

                val animatorSet = AnimatorSet()
                animatorSet.playTogether(fadeIn, slideDown)
                animatorSet.startDelay = slideDownDuration / 2

                scientificButtonsLayout.visibility = View.VISIBLE

                val flipTextDown = ObjectAnimator.ofFloat(
                    binding.toggleScientificButtonImageView, "rotationX", 0f, 180f
                )
                flipTextDown.duration = 300
                flipTextDown.start()

                binding.root.forEach { view ->
                    if (view is Button && view != toggleScientificButton) {
                        val slideDownOther = ObjectAnimator.ofFloat(
                            view, "translationY", -scientificButtonsLayout.height.toFloat(), 0f
                        )
                        slideDownOther.duration = slideDownDuration
                        slideDownOther.start()
                    }
                }
                animatorSet.start()
            }
        }
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.inflateMenu(R.menu.top_app_bar)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }

                else -> false
            }
        }


        binding.apply {
            button0.setOnClickListener { numberAction(it) }
            button1.setOnClickListener { numberAction(it) }
            button2.setOnClickListener { numberAction(it) }
            button3.setOnClickListener { numberAction(it) }
            button4.setOnClickListener { numberAction(it) }
            button5.setOnClickListener { numberAction(it) }
            button6.setOnClickListener { numberAction(it) }
            button7.setOnClickListener { numberAction(it) }
            button8.setOnClickListener { numberAction(it) }
            button9.setOnClickListener { numberAction(it) }
            buttonDot.setOnClickListener { numberAction(it) }
            buttonDot.text = ","
            buttonAdd.setOnClickListener { operationAction(it) }
            buttonSubtract.setOnClickListener { operationAction(it) }
            buttonMultiply.setOnClickListener { operationAction(it) }
            buttonDivide.setOnClickListener { operationAction(it) }
            buttonEqual.setOnClickListener { equalAction() }
            buttonClear.setOnClickListener { allClearAction() }
            buttonBackspace.setOnClickListener { backSpaceAction() }
            buttonParentheses.setOnClickListener { parenthesesAction() }
            buttonSin.setOnClickListener { scientificOperationAction("sin(") }
            buttonCos.setOnClickListener { scientificOperationAction("cos(") }
            buttonTan.setOnClickListener { scientificOperationAction("tan(") }
            buttonSqrt.setOnClickListener { scientificOperationAction("√(") }
            buttonPower.setOnClickListener {
                operationAction(Button(this@MainActivity).apply {
                    text = "^"
                })
            }
            buttonPi.setOnClickListener { addPi() }
            buttonRadDeg.setOnClickListener { switchRadDeg() }
            buttonInverse.setOnClickListener { inverseAction() }
            buttonPercent.setOnClickListener { scientificOperationAction("%") }
            buttonLn.setOnClickListener { scientificOperationAction("ln(") }
            buttonLog.setOnClickListener { scientificOperationAction("log(") }
            buttonE.setOnClickListener { numberAction(it) }
            buttonFactorial.setOnClickListener { scientificOperationAction("!") }

        }
    }

    private fun setupToolbar() {
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.settings -> openSettingsActivity()
                else -> return@setOnMenuItemClickListener false
            }
            return@setOnMenuItemClickListener true
        }
    }


    @SuppressLint("SetTextI18n")
    private fun numberAction(view: View) {

        if (view is Button) {
            val currentText = binding.workingsTV.text.toString()
            if (view.text == ",") {
                if (canAddDecimal) binding.workingsTV.text = "$currentText${view.text}"
                canAddDecimal = false
            } else binding.workingsTV.text = "$currentText${view.text}"
            canAddOperation = true
        }
        performHapticFeedback()
    }

    private fun operationAction(view: View) {
        if (view is Button) {
            if (canAddOperation) {
                binding.workingsTV.append(view.text)
                canAddOperation = false
                canAddDecimal = true
            }
        }
        performHapticFeedback()
    }

    private fun parenthesesAction() {
        binding.buttonParentheses.setOnLongClickListener {
            binding.workingsTV.append(")")
            true
        }
        val workings = binding.workingsTV.text.toString()
        var openParenthesesCount = 0

        for (char in workings) {
            if (char == '(') openParenthesesCount++
            else if (char == ')') openParenthesesCount--
        }

        if (workings.isEmpty() || workings.last() == '(' || workings.last() in listOf(
                '+',
                '-',
                '×',
                '÷',
                '^',
                '√'
            )
        ) {
            binding.workingsTV.append("(")
        } else if (openParenthesesCount > 0 && (workings.last()
                .isDigit() || workings.last() == ')')
        ) {
            binding.workingsTV.append(")")
        } else if (workings.isNotEmpty() && workings.last().isDigit()) {
            binding.workingsTV.append("×(")
        } else {
            binding.workingsTV.append("(")
        }

        performHapticFeedback()
    }

    @SuppressLint("SetTextI18n")
    private fun allClearAction() {
        binding.workingsTV.text = ""
        binding.resultsTV.text = ""
        performHapticFeedback()
    }

    private fun backSpaceAction() {
        val length = binding.workingsTV.length()
        if (length > 0) binding.workingsTV.text = binding.workingsTV.text.subSequence(0, length - 1)
        performHapticFeedback()
    }

    private fun equalAction() {
        binding.resultsTV.text = calculateResults()
        performHapticFeedback()
    }

    private fun calculateResults(): String {
        val expression = binding.workingsTV.text.toString()
        try {
            val result = evaluateExpression(expression)
            return formatResult(result)
        } catch (e: Exception) {
            return "Error"
        }
    }

    @SuppressLint("DefaultLocale")
    private fun formatResult(result: Double): String {
        val parts = result.toString().split(".")
        val integerPart = parts[0]
        val decimalPart = if (parts.size > 1) parts[1] else ""

        val formattedInteger = StringBuilder()
        for (i in integerPart.indices.reversed()) {
            formattedInteger.insert(0, integerPart[i])
            if ((integerPart.length - i) % 3 == 0 && i != 0) {
                formattedInteger.insert(0, ".")
            }
        }

        return if (decimalPart.isNotEmpty()) {
            val formattedDecimal = if (decimalPart.length >= 2) decimalPart else decimalPart.padEnd(2, '0')
            "${formattedInteger},${formattedDecimal}"
        } else {
            "${formattedInteger},00"
        }
    }

    private fun evaluateExpression(expression: String): Double {
        val numbers = java.util.Stack<Double>()
        val operators = java.util.Stack<Char>()
        var number = ""
        var i = 0

        while (i < expression.length) {
            val c = expression[i]
            if (c.isDigit() || c == '.') {
                number += c
            } else if (c == '(') {
                val closingParenIndex = findClosingParenthesis(expression, i)
                if (closingParenIndex != -1) {
                    val subExpression = expression.substring(i + 1, closingParenIndex)
                    val subResult = evaluateExpression(subExpression)
                    numbers.push(subResult)
                    i = closingParenIndex
                } else {

                    return Double.NaN
                }
            } else if (c == ')') {

                return Double.NaN
            } else if (c == '!') {
                if (number.isNotEmpty()) {
                    val num = number.toDouble()
                    numbers.push(factorial(num.toInt()))
                    number = ""
                } else if (!numbers.empty()) {
                    val num = numbers.pop()
                    numbers.push(factorial(num.toInt()))
                }
            } else if (isOperator(c)) {
                if (number.isNotEmpty()) {
                    numbers.push(number.toDouble())
                    number = ""
                }
                while (!operators.empty() && hasPrecedence(c, operators.peek())) {
                    numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()))
                }
                operators.push(c)
            } else if (isScientificFunction(c)) {
                val funcEnd = findFunctionEnd(expression, i)
                val func = expression.substring(i, funcEnd + 1)
                val result = evaluateScientificFunction(func)
                numbers.push(result)
                i = funcEnd
            } else if (c == '%') {
                if (number.isNotEmpty()) {
                    val num = number.toDouble()
                    numbers.push(num / 100.0)
                    number = ""
                } else if (!numbers.empty()) {
                    val num = numbers.pop()
                    numbers.push(num / 100.0)
                }
            } else if (c == 'π') {
                if (number.isNotEmpty()) {
                    numbers.push(number.toDouble() * Math.PI)
                } else {
                    numbers.push(Math.PI)
                }
                number = ""
            } else if (c == '²') {
                if (number.isNotEmpty()) {
                    val num = number.toDouble()
                    numbers.push(num * num)
                    number = ""
                } else if (!numbers.empty()) {
                    val num = numbers.pop()
                    numbers.push(num * num)
                }
            }
            i++
        }

        if (number.isNotEmpty()) {
            numbers.push(number.toDouble())
        }

        while (!operators.empty()) {
            numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()))
        }

        return if (!numbers.empty()) {
            var result = numbers.pop()
            if (!isRadians && isTrigonometricFunction(expression)) {
                result = Math.toDegrees(result)
            }
            result
        } else {
            Double.NaN
        }
    }

    private fun findClosingParenthesis(expression: String, startIndex: Int): Int {
        var count = 1
        for (i in startIndex + 1 until expression.length) {
            when (expression[i]) {
                '(' -> count++
                ')' -> count--
            }
            if (count == 0) return i
        }
        return -1
    }

    private fun isTrigonometricFunction(expression: String): Boolean {
        return expression.contains("sin") || expression.contains("cos") || expression.contains("tan")
    }

    private fun findFunctionEnd(expression: String, startIndex: Int): Int {
        var parenthesesCount = 0
        for (i in startIndex until expression.length) {
            if (expression[i] == '(') {
                parenthesesCount++
            } else if (expression[i] == ')') {
                parenthesesCount--
                if (parenthesesCount == 0) {
                    return i
                }
            }
        }
        return -1
    }

    private fun evaluateScientificFunction(func: String): Double {
        val function = func.substring(0, 2)
        val operandStr = func.substring(function.length, func.length - 1)
        val operand = evaluateExpression(operandStr)

        return when (function) {
            "sin" -> if (isInverse) asin(operand) else sin(operand)
            "cos" -> if (isInverse) acos(operand) else cos(operand)
            "tan" -> if (isInverse) atan(operand) else tan(operand)
            "√(" -> sqrt(operand)
            "ln(" -> ln(operand)
            "log" -> log10(operand)
            else -> throw IllegalArgumentException("Invalid scientific operation")
        }
    }

    private fun isScientificFunction(c: Char): Boolean = c in listOf('s', 'c', 't', '√', '!', 'l')

    private fun isOperator(c: Char): Boolean = c in listOf('+', '-', '×', '/', '^')

    private fun hasPrecedence(op1: Char, op2: Char): Boolean {
        if (op2 == '(' || op2 == ')') return false
        if (op1 == '%') return true
        if ((op1 == '×' || op1 == '/') && (op2 == '+' || op2 == '-')) return false
        return true
    }

    private fun applyOp(op: Char, b: Double, a: Double): Double {
        return when (op) {
            '+' -> a + b
            '-' -> a - b
            '×' -> a * b
            '/' -> a / b
            '^' -> a.pow(b)
            '%' -> a / 100.0
            else -> throw IllegalArgumentException("Invalid operator")
        }
    }

    private fun factorial(n: Int): Double {
        if (n < 0) throw IllegalArgumentException("Factorial is not defined for negative numbers")
        return if (n <= 1) 1.0 else n * factorial(n - 1)
    }

    private fun ln(x: Float): Float {
        return kotlin.math.ln(x)
    }

    private fun log10(x: Float): Float {
        return kotlin.math.log10(x)
    }

    private fun scientificOperationAction(operation: String) {
        var workings = binding.workingsTV.text.toString()
        if (operation == "√(") {
            workings += if (isInverse) {
                "²"
            } else {
                if (workings.isNotEmpty() && workings.last() == '(') {
                    operation.substring(1)
                } else {
                    operation
                }
            }
            canAddOperation = true
        } else if (operation != "%" && operation != "!") {
            workings += if (workings.isNotEmpty() && workings.last() == '(') {
                operation.substring(1)
            } else {
                operation
            }
        } else {
            workings += operation
        }
        binding.workingsTV.text = workings
        canAddDecimal = true
        performHapticFeedback()
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun performHapticFeedback() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(50)
        }
    }

    private fun addPi() {
        binding.workingsTV.append("π")
        canAddDecimal = true
        canAddOperation = false
        performHapticFeedback()
    }

    private fun switchRadDeg() {
        isRadians = !isRadians
        binding.buttonRadDeg.text = if (isRadians) "RAD" else "DEG"
        performHapticFeedback()
    }

    private var isInverse = false
    private fun inverseAction() {
        isInverse = !isInverse
        binding.buttonInverse.text = if (isInverse) "INV'" else "INV"
        binding.buttonSqrt.text = if (isInverse) "x²" else "√"
        binding.buttonSin.text = if (isInverse) "sin⁻¹" else "sin"
        binding.buttonCos.text = if (isInverse) "cos⁻¹" else "cos"
        binding.buttonTan.text = if (isInverse) "tan⁻¹" else "tan"
        performHapticFeedback()
    }

    private fun openSettingsActivity() {
        startActivity(Intent(applicationContext, SettingsActivity::class.java))
    }


    fun backSpaceAction(view: View) {}
    fun equalAction(view: View) {}

}
