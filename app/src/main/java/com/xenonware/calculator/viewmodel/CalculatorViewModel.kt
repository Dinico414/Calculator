package com.xenonware.calculator.viewmodel

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
        private set

    private var openParenthesesCount by mutableIntStateOf(0)

    var isInverseMode by mutableStateOf(false)
        private set

    fun toggleInverseMode() {
        isInverseMode = !isInverseMode
    }

    init {
        mXparser.setRadiansMode()
    }

    fun onButtonClick(buttonValue: String) {
        when (buttonValue) {
            "AC" -> clear()
            "⌫" -> backspace()
            "=" -> calculate()
            "π" -> appendConstant(Math.PI.toString())
            "e" -> appendConstant(Math.E.toString())

            "√" -> {
                if (isInverseMode) {
                    if (currentInput.isNotEmpty() && (currentInput.last().isDigit() || currentInput.last() == ')')) {
                        appendOperator("^2")
                    } else if (currentInput.isEmpty() || !currentInput.last().isDigit() && currentInput.last() != ')') {
                        appendOperator("^2")
                    }
                } else {
                    appendFunction("√(")
                }
            }
            "x²" -> {
                if (currentInput.isNotEmpty() && (currentInput.last().isDigit() || currentInput.last() == ')')) {
                    appendOperator("^2")
                } else if (currentInput.isEmpty() || !currentInput.last().isDigit() && currentInput.last() != ')') {
                    appendOperator("^2")
                }
            }
            "^" -> appendOperator("^")
            "!" -> appendFactorial()

            "sin" -> appendTrigFunction("sin(")
            "cos" -> appendTrigFunction("cos(")
            "tan" -> appendTrigFunction("tan(")

            "ln" -> {
                if (isInverseMode) {
                    appendFunction("exp(")
                } else {
                    appendFunction("ln(")
                }
            }
            "eˣ" -> {
                appendFunction("exp(")
            }
            "log" -> {
                if (isInverseMode) {
                    currentInput += "10^("
                    openParenthesesCount++
                } else {
                    appendFunction("log10(")
                }
            }
            "10ˣ" -> {
                currentInput += "10^("
                openParenthesesCount++
            }

            "%" -> appendOperator("%")
            "+", "-", "×", "÷" -> appendOperator(buttonValue)
            "." -> appendDecimal()
            else -> if (buttonValue.all { it.isDigit() }) {
                appendInput(buttonValue)
            }
        }
    }

    private fun appendConstant(displayValue: String) {
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

            if (op == "^2" && currentInput.isNotEmpty() && (currentInput.last().isDigit() || currentInput.last() == ')')) {
                currentInput += op
                return
            } else if (op == "^2") {
                return
            }
            if (currentInput.isEmpty() && op != "-") return

        }

        val lastChar = currentInput.lastOrNull()

        if (op == "^2") {
            if (lastChar != null && (lastChar.isDigit() || lastChar == ')' || lastChar == 'π' || lastChar == 'e')) {
                currentInput += op
            }
            return
        }


        if (lastChar != null && (lastChar.isDigit() || lastChar == ')' || lastChar == 'π' || lastChar == 'e')) {
            currentInput += op
        } else if (lastChar != null && "(+-×÷^%".contains(lastChar) && op == "-") {
            currentInput += op
        } else if (lastChar != null && "(+-×÷^%".contains(lastChar) && !"(+-×÷^%".contains(op.first())) {
            if (op.length == 1 && "+-×÷^%".contains(op.first())) {
                currentInput = currentInput.dropLast(1) + op
            }
        }
    }


    private fun appendDecimal() {
        if (currentInput.isEmpty()) {
            currentInput += "0."
            return
        }
        var lastOperatorIndex = -1
        for (i in currentInput.length - 1 downTo 0) {
            if ("+-×÷^%eπ(".contains(currentInput[i])) {
                lastOperatorIndex = i
                break
            }
        }

        val currentNumberSegment = if (lastOperatorIndex == -1) {
            currentInput
        } else {
            currentInput.substring(lastOperatorIndex + 1)
        }

        if (!currentNumberSegment.contains('.')) {
            val lastChar = currentInput.lastOrNull()
            if (lastChar == null || (!lastChar.isDigit() && lastChar != ')' && lastChar != 'π' && lastChar != 'e' )) {
                if (currentInput.isEmpty() || currentInput.endsWith("(") || "+-×÷^%".contains(currentInput.last())){
                    currentInput += "0"
                }
            }
            currentInput += "."
        }
    }

    private fun appendFactorial() {
        if (currentInput.isNotEmpty() && (currentInput.last().isDigit() || currentInput.last() == ')')) {
            currentInput += "!"
        }
    }

    private fun appendOpenParenthesis() {
        currentInput += "("
        openParenthesesCount++
    }

    private fun appendCloseParenthesis() {
        if (openParenthesesCount > 0 && currentInput.isNotEmpty() &&
            (currentInput.last().isDigit() || currentInput.last() == ')' || currentInput.last() == '!' || currentInput.last() == 'π' || currentInput.last() == 'e')
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
    }

    private fun backspace() {
        if (currentInput.isNotEmpty()) {
            val removedChar = currentInput.last()
            currentInput = if (currentInput.endsWith("^2")) {
                currentInput.dropLast(2)
            } else {
                currentInput.dropLast(1)
            }

            if (removedChar == '(') {
                if(openParenthesesCount > 0) openParenthesesCount--
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


            val expression = Expression(expressionString)

            if (expression.checkSyntax()) {
                val calculatedResult = expression.calculate()
                result = if (calculatedResult.isNaN()) "Error: NaN" else formatResult(calculatedResult)
            } else {
                result = "Error: Syntax (${expression.errorMessage})"
            }
        } catch (e: Exception) {
            result = "Error: Calc"
            e.printStackTrace()
        }
    }

    private fun formatResult(value: Double): String {
        return if (value.isInfinite()) {
            "Error: Infinity"
        } else if (value == value.toLong().toDouble() && !value.toString().contains("E", ignoreCase = true)) {
            value.toLong().toString()
        } else {
            String.format("%.10f", value).trimEnd('0').trimEnd('.')
        }
    }

    fun toggleScientificMode() {
        isScientificMode = !isScientificMode
    }

    fun updateAngleUnitAndMode(newUnit: AngleUnit) {
        this.angleUnit = newUnit
        if (newUnit == AngleUnit.RAD) {
            mXparser.setRadiansMode()
        } else {
            mXparser.setDegreesMode()
        }
    }

    fun toggleAngleUnit() {
        val newUnit = if (angleUnit == AngleUnit.RAD) AngleUnit.DEG else AngleUnit.RAD
        updateAngleUnitAndMode(newUnit)
    }

    fun onParenthesesClick() {
        val lastChar = currentInput.lastOrNull()

        if (currentInput.isEmpty() ||
            "+-×÷^%(".contains(lastChar ?: '(') ||
            (currentInput.isNotEmpty() && currentInput.last().isLetter() && currentInput.endsWith("(") && currentInput.last() != 'e' && currentInput.last() != 'i')
        ) {
            appendOpenParenthesis()
        }
        else if (openParenthesesCount > 0 &&
            (lastChar != null && (lastChar.isDigit() || lastChar == ')' || lastChar == '!' || lastChar == 'π' || lastChar == 'e')) &&
            !currentInput.endsWith("(")
        ) {
            appendCloseParenthesis()
        }

    }
}

enum class AngleUnit { RAD, DEG }