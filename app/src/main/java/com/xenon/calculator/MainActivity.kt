package com.xenon.calculator

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.google.android.material.appbar.MaterialToolbar
import com.xenon.calculator.activities.ConverterActivity
import com.xenon.calculator.activities.SettingsActivity
import com.xenon.calculator.databinding.ActivityMainBinding
import java.text.DecimalFormatSymbols
import java.util.Locale
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


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var vibrator: Vibrator
    private var canAddOperation = false
    private var canAddDecimal = true
    private var isRadians = true



    override fun onCreate(savedInstanceState: Bundle?) {
        val sharedPreferenceManager = SharedPreferenceManager(this)
        AppCompatDelegate.setDefaultNightMode(sharedPreferenceManager.themeFlag[sharedPreferenceManager.theme])
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        vibrator = getSystemService(Vibrator::class.java)



        val screenLayout = findViewById<ConstraintLayout>(R.id.screen)



        screenLayout.post {
            val screenHeight = screenLayout.rootView.height
            val newHeight = (screenHeight * 0.3).toInt()
            val params = screenLayout.layoutParams
            params.height = newHeight
            screenLayout.layoutParams = params
        }



        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.inflateMenu(R.menu.top_app_bar)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.converter -> {
                    startActivity(Intent(this, ConverterActivity::class.java))
                    true
                }

                R.id.settings -> {
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }

                else -> false
            }
        }

        val toggleScientificButton = findViewById<Button>(R.id.toggleScientificButton)
        val row1 = findViewById<ConstraintLayout>(R.id.row1)
        val row2 = findViewById<ConstraintLayout>(R.id.row2)
        val toggleScientificButtonImageView =
            findViewById<ImageView>(R.id.toggleScientificButtonImageView)

        toggleScientificButtonImageView.rotationX = 180f

        toggleScientificButton.setOnClickListener {
            val isVisible = row1.visibility == View.VISIBLE
            row1.visibility = if (isVisible) View.GONE else View.VISIBLE
            row2.visibility = if (isVisible) View.GONE else View.VISIBLE
            onScientificButtonLayoutVisibilityChanged(!isVisible)

            toggleScientificButtonImageView.animate().rotationX(if (isVisible) 0f else 180f)
                .setDuration(300).start()
        }



        binding.apply {

            val numberButtons = listOf(
                button0,
                button1,
                button2,
                button3,
                button4,
                button5,
                button6,
                button7,
                button8,
                button9,
                buttonDot,
                buttonE
            )
            numberButtons.forEach { button ->
                button.setOnClickListener { numberAction(it) }
            }

            val operationButtons = listOf(
                buttonAdd,
                buttonSubtract,
                buttonMultiply,
                buttonDivide
            )
            operationButtons.forEach { button ->
                button.setOnClickListener { operationAction(it) }
            }
            buttonEqual.setOnClickListener { equalAction() }
            buttonClear.setOnClickListener { allClearAction() }
            buttonBackspace.setOnClickListener { backSpaceAction() }
            buttonParentheses.setOnClickListener { parenthesesAction() }
            buttonSin.setOnClickListener { scientificOperationAction("sin") }
            buttonCos.setOnClickListener { scientificOperationAction("cos") }
            buttonTan.setOnClickListener { scientificOperationAction("tan") }
            buttonSqrt.setOnClickListener { scientificOperationAction("√") }
            buttonPercent.setOnClickListener { scientificOperationAction("%") }
            buttonLn.setOnClickListener { scientificOperationAction("ln") }
            buttonLog.setOnClickListener { scientificOperationAction("log") }
            buttonFactorial.setOnClickListener { scientificOperationAction("!") }
            buttonPower.setOnClickListener {
                operationAction(Button(this@MainActivity).apply {
                    text = "^"
                })
            }
            buttonPi.setOnClickListener { addPi() }
            buttonRadDeg.setOnClickListener { switchRadDeg() }
            buttonInverse.setOnClickListener { inverseAction() }
        }
    }

    private val locale = Locale.getDefault()
    private val decimalSeparator = DecimalFormatSymbols.getInstance(locale).decimalSeparator

    @SuppressLint("SetTextI18n")
    private fun numberAction(view: View) {
        if (view is Button) {
            val currentText = binding.workingsTV.text.toString()
            if (view.text == ".") {
                if (canAddDecimal) {
                    binding.workingsTV.text = currentText + decimalSeparator
                    canAddDecimal = false
                }
            } else {
                binding.workingsTV.text = currentText + view.text
                canAddOperation = true
            }
        }
        performHapticFeedback()
    }

    private fun operationAction(view: View) {
        if (view is Button && canAddOperation) {
            binding.workingsTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
        performHapticFeedback()
    }

    private fun scientificOperationAction(operation: String) {
        val workings = binding.workingsTV.text.toString()
        val newOperation = if (isInverse && operation in listOf("sin(", "cos(", "tan(", "√(")) {
            when (operation) {
                "√(" -> "²"
                else -> operation.replace("(", "⁻¹(")
            }
        } else {
            operation
        }

        // This is where the fix is
        binding.workingsTV.text = when (newOperation) {
            "²" -> workings + newOperation
            "%", "!" -> workings + newOperation
            else -> workings + if (workings.isNotEmpty() && workings.last() == '(' && newOperation.endsWith(
                    "("
                )
            ) newOperation.substring(1) else "$newOperation("
        }

        canAddDecimal = true
        performHapticFeedback()
    }

    private fun evaluateScientificFunction(func: String): Double {
        val function = func.substring(0, func.indexOf('('))
        val operandStr = func.substring(function.length + 1, func.length - 1)

        try {
            var operand = evaluateExpression(operandStr)

            if (!isRadians && (function == "sin" || function == "cos" || function == "tan") && !isInverse) {
                operand = Math.toRadians(operand)
            }

            var result = when (function) {
                "sin" -> if (isInverse) asin(operand) else sin(operand)
                "cos" -> if (isInverse) acos(operand) else cos(operand)
                "tan" -> if (isInverse) atan(operand) else tan(operand)
                "√" -> if (isInverse) operand * operand else sqrt(operand) // Calculate square if isInverse is true
                "ln" -> ln(operand)
                "log" -> log10(operand)
                else -> throw IllegalArgumentException("Invalid scientific operation")
            }

            if (!isRadians && isInverse && (function == "sin" || function == "cos" || function == "tan")) {
                result = Math.toDegrees(result)
            }

            return result
        } catch (e: NumberFormatException) {
            Log.e("Calculator", "Error parsing operand: ${e.message}")
            return Double.NaN
        }
    }

    private fun evaluateExpression(expression: String): Double {
        val numbers = java.util.Stack<Double>()
        val operators = java.util.Stack<Char>()
        var number = ""
        var i = 0

        while (i < expression.length) {
            val c = expression[i]
            if (c.isDigit() || c == ',' || c == '.') {
                number += if (c == ',') '.' else c // Replacing comma with dot
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
            } else if (c == '%') {
                if (number.isNotEmpty()) {
                    val num = number.toDouble()
                    numbers.push(num / 100.0)
                    number = ""
                } else if (!numbers.empty()) {
                    val num = numbers.pop()
                    numbers.push(num / 100.0)
                }
            } else if (isOperator(c)) {
                if (number.isNotEmpty()) {
                    numbers.push(number.toDouble())
                    number = ""
                }

                Log.d("Calculator", "Operator: $c")

                while (!operators.empty() && hasPrecedence(c, operators.peek())) {
                    numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()))
                }
                operators.push(c)
            } else if (isScientificFunction(c)) {
                val funcEnd = findFunctionEnd(expression, i)
                val func = expression.substring(i, funcEnd + 1)

                Log.d("Calculator", "Function: $func")

                val result = evaluateScientificFunction(func)
                numbers.push(result)
                i = funcEnd
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

        val result = if (!numbers.empty()) {
            numbers.pop()
        } else {
            Double.NaN
        }

        Log.d("Calculator", "Result: $result")

        return result
    }



    private fun parenthesesAction() {
        val workings = binding.workingsTV.text.toString()
        val lastChar = workings.lastOrNull()

        when {
            workings.isEmpty() || lastChar == '(' || lastChar in listOf(
                '+', '-', '×', '÷', '^', '√'
            ) -> binding.workingsTV.append("(")

            workings.count { it == '(' } > workings.count { it == ')' } -> binding.workingsTV.append(
                ")"
            )

            lastChar?.isDigit() == true -> binding.workingsTV.append("×(")
            else -> binding.workingsTV.append("(")
        }

        performHapticFeedback()
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



    private fun allClearAction() {
        binding.workingsTV.text = ""
        binding.resultsTV.text = ""

        // Reset auto-sizing configuration
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
            binding.workingsTV,
            12,
            50,
            1,
            TypedValue.COMPLEX_UNIT_SP
        )

        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
            binding.resultsTV,
            12,
            50,
            1,
            TypedValue.COMPLEX_UNIT_SP
        )

        performHapticFeedback()
    }

    private fun backSpaceAction() {
        val length = binding.workingsTV.length()
        if (length > 0) binding.workingsTV.text = binding.workingsTV.text.subSequence(0, length - 1)
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

    private fun formatResult(result: Double): String {
        val formattedResult = String.format(Locale.getDefault(), "%.10f", result)
        val parts = formattedResult.split(decimalSeparator)
        val integerPart = parts[0].reversed().chunked(3).joinToString(".").reversed()
        val decimalPart = parts[1].trimEnd('0')
        return if (decimalPart.isEmpty()) {
            integerPart
        } else {
            "$integerPart,$decimalPart"
        }
    }

    private fun equalAction() {
        binding.resultsTV.text = calculateResults()
        performHapticFeedback()
    }



    private fun isScientificFunction(c: Char): Boolean = c in listOf('s', 'c', 't', '√', '!', 'l')
    private fun isOperator(c: Char): Boolean = c in listOf('+', '-', '×', '/', '^')
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



    private fun hasPrecedence(op1: Char, op2: Char): Boolean {
        if (op2 == '(' || op2 == ')') return false
        if ((op1 == '×' || op1 == '/') && (op2 == '+' || op2 == '-')) return false
        return true
    }

    private fun factorial(n: Int): Double {
        if (n < 0) throw IllegalArgumentException("Factorial is not defined for negative numbers")
        return if (n <= 1) 1.0 else n * factorial(n - 1)
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
    private fun onScientificButtonLayoutVisibilityChanged(isVisible: Boolean) {
        if (!isVisible) {
            isInverse = false
            // Update the UI elements based on the new value of isInverse
            updateUiElements()
        }
    }

    private fun inverseAction() {
        isInverse = !isInverse
        updateUiElements()
        performHapticFeedback()
    }

    private fun updateUiElements() {
        binding.buttonSqrt.text = if (isInverse) "x²" else "√"
        binding.buttonSin.text = if (isInverse) "sin⁻¹" else "sin"
        binding.buttonCos.text = if (isInverse) "cos⁻¹" else "cos"
        binding.buttonTan.text = if (isInverse) "tan⁻¹" else "tan"
        binding.buttonInverse.setTextColor(
            ContextCompat.getColorStateList(
                this,
                if (isInverse) com.xenon.commons.accesspoint.R.color.error else com.xenon.commons.accesspoint.R.color.textOnPrimary
            )
        )
        binding.buttonInverse.setBackgroundColor(
            ContextCompat.getColor(
                this,
                if (isInverse) com.xenon.commons.accesspoint.R.color.errorContainer else com.xenon.commons.accesspoint.R.color.transparent
            )
        )
    }



    private fun performHapticFeedback() {
        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    fun backSpaceAction(@Suppress("UNUSED_PARAMETER") view: View) {}
    fun equalAction(@Suppress("UNUSED_PARAMETER") view: View) {}
}
