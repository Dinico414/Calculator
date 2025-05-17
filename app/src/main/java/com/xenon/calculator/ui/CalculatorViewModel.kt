package com.xenon.calculator.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.mXparser

open class CalculatorViewModel : ViewModel() {
    var currentInput by mutableStateOf("")
        private set

    var result by mutableStateOf("")
        private set

    var isScientificMode by mutableStateOf(true)
        private set

    var angleUnit by mutableStateOf(AngleUnit.RAD)
        private set // This is fine, the issue is with explicitly defined methods clashing

    private var openParenthesesCount by mutableIntStateOf(0)
        private set

    var isInverseMode by mutableStateOf(false)
        private set

    init {
        mXparser.setRadiansMode()
    }

    fun onButtonClick(buttonValue: String) {
        when (buttonValue) {
            "AC" -> clear()
            "⌫" -> backspace()
            "=" -> calculate()
            "INV" -> toggleInverseFunctions()
            // Use the public updateAngleUnitAndMode or toggleAngleUnit
            "RAD" -> updateAngleUnitAndMode(AngleUnit.RAD)
            "DEG" -> updateAngleUnitAndMode(AngleUnit.DEG)
            "π" -> appendConstant("pi", Math.PI.toString())
            "e" -> appendConstant("e", Math.E.toString())
            "√" -> appendFunction("sqrt(")
            "^" -> appendOperator("^")
            "!" -> appendFactorial()
            "sin" -> appendTrigFunction("sin(")
            "cos" -> appendTrigFunction("cos(")
            "tan" -> appendTrigFunction("tan(")
            "ln" -> appendFunction("ln(")
            "log" -> appendFunction("log10(")
            "%" -> appendOperator("%")
            "+", "-", "×", "÷" -> appendOperator(buttonValue)
            "." -> appendDecimal()
            else -> if (buttonValue.all { it.isDigit() }) {
                appendInput(buttonValue)
            }
        }
    }

    // ... (other methods remain the same) ...

    private fun appendConstant(mxParserConst: String, displayValue: String) {
        currentInput += displayValue
    }

    private fun appendFunction(func: String) {
        currentInput += func
        if (func.endsWith("(")) {
            openParenthesesCount++
        }
    }

    private fun appendTrigFunction(func: String) {
        val actualFunc = if (isInverseMode) {
            when (func) {
                "sin(" -> "asin("
                "cos(" -> "acos("
                "tan(" -> "atan("
                else -> func
            }
        } else {
            func
        }
        currentInput += actualFunc
        if (actualFunc.endsWith("(")) {
            openParenthesesCount++
        }
    }

    private fun appendOperator(op: String) {
        if (currentInput.isEmpty()) {
            if (op == "-") {
                currentInput += op
            }
            return
        }

        val lastChar = currentInput.last()
        if (lastChar.isDigit() || lastChar == ')' || lastChar == 'π' || lastChar == 'e') {
            currentInput += op
        } else if ("(+-×÷^%".contains(lastChar) && op == "-") {
            currentInput += op
        } else if ("(+-×÷^%".contains(lastChar) && !"(+-×÷^%".contains(op.first())) {
            currentInput = currentInput.dropLast(1) + op
        }
    }

    private fun appendDecimal() {
        if (currentInput.isEmpty()) {
            currentInput += "0."
            return
        }
        val lastOperatorIndex = currentInput.indexOfLast { char ->
            "+-×÷^%eπ(".contains(char)
        }
        val currentNumberSegment = if (lastOperatorIndex == -1) {
            currentInput
        } else {
            currentInput.substring(lastOperatorIndex + 1)
        }
        if (!currentNumberSegment.contains('.')) {
            val lastChar = currentInput.lastOrNull()
            if (lastChar == null || (!lastChar.isDigit() && lastChar != ')')) {
                if (lastChar != '(' && !currentInput.endsWith("e") && !currentInput.endsWith("π")) {
                    currentInput += "0"
                }
            }
            currentInput += "."
        }
    }

    private fun appendFactorial() {
        if (currentInput.isNotEmpty() && (currentInput.last()
                .isDigit() || currentInput.last() == ')')
        ) {
            currentInput += "!"
        }
    }

    private fun appendOpenParenthesis() {
        currentInput += "("
        openParenthesesCount++
    }

    private fun appendCloseParenthesis() {
        if (openParenthesesCount > 0 && currentInput.isNotEmpty() &&
            (currentInput.last()
                .isDigit() || currentInput.last() == ')' || currentInput.last() == '!' || currentInput.last() == 'π' || currentInput.last() == 'e')
        ) {
            currentInput += ")"
            openParenthesesCount--
        }
    }

    private fun appendInput(value: String) {
        currentInput += value
    }

    private fun clear() {
        currentInput = ""
        result = ""
        openParenthesesCount = 0
        isInverseMode = false
    }

    private fun backspace() {
        if (currentInput.isNotEmpty()) {
            val removedChar = currentInput.last()
            currentInput = currentInput.dropLast(1)
            if (removedChar == '(') {
                openParenthesesCount--
            } else if (removedChar == ')') {
                openParenthesesCount++
            }
        }
    }

    private fun calculate() {
        if (currentInput.isBlank()) {
            result = ""
            return
        }
        var tempInput = currentInput
        if (openParenthesesCount > 0) {
            tempInput += ")".repeat(openParenthesesCount)
        }
        openParenthesesCount = 0
        try {
            val expressionString = tempInput
                .replace("×", "*")
                .replace("÷", "/")
                .replace("%", "#")
                .replace("√", "sqrt")
                .replace("π", "(pi)")
                .replace("e", "(e)")
            val expression = Expression(expressionString)
            if (expression.checkSyntax()) {
                val calculatedResult = expression.calculate()
                result =
                    if (calculatedResult.isNaN()) "Error: NaN" else formatResult(calculatedResult)
            } else {
                result = "Error: Syntax"
            }
        } catch (e: Exception) {
            result = "Error: Calc"
            e.printStackTrace()
        }
    }

    private fun formatResult(value: Double): String {
        return if (value.isInfinite()) {
            "Error: Infinity"
        } else if (value == value.toLong().toDouble() && !value.toString()
                .contains("E", ignoreCase = true)
        ) {
            value.toLong().toString()
        } else {
            String.format("%.10f", value).trimEnd('0').trimEnd('.')
        }
    }

    fun toggleScientificMode() {
        isScientificMode = !isScientificMode
    }

    // KEEP THIS ONE or a similar public method
    fun updateAngleUnitAndMode(newUnit: AngleUnit) {
        this.angleUnit = newUnit // Internally, this uses the private setter
        if (newUnit == AngleUnit.RAD) {
            mXparser.setRadiansMode()
        } else {
            mXparser.setDegreesMode()
        }
    }

    // This method is also good for toggling
    fun toggleAngleUnit() {
        val newUnit = if (angleUnit == AngleUnit.RAD) AngleUnit.DEG else AngleUnit.RAD
        updateAngleUnitAndMode(newUnit) // Call the consolidated public method
    }

    private fun toggleInverseFunctions() {
        isInverseMode = !isInverseMode
    }

    fun onParenthesesClick() {
        val lastChar = currentInput.lastOrNull()
        if (lastChar == null ||
            "+-×÷^%(".contains(lastChar) ||
            (currentInput.isNotEmpty() && currentInput.last()
                .isLetter() && currentInput.last() != 'e' && currentInput.last() != 'i')
        ) {
            appendOpenParenthesis()
        }
        else if (openParenthesesCount > 0 &&
            (lastChar != null && (lastChar.isDigit() || lastChar == ')' || lastChar == '!' || lastChar == 'π' || lastChar == 'e'))
        ) {
            appendCloseParenthesis()
        }
    }
}

enum class AngleUnit { RAD, DEG }