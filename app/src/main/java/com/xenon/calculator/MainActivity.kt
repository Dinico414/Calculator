package com.xenon.calculator

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.xenon.calculator.databinding.ActivityMainBinding // Import the generated binding class

@Suppress("UNUSED_CHANGED_VALUE")
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding // Declare your binding variable
    private var canAddOperation = false
    private var canAddDecimal = true
    private var openParentheses = true // Indicates whether to add an opening or closing parentheses

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // Inflate the layout using View Binding
        setContentView(binding.root)

        // Set click listeners for buttons
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
            buttonAdd.setOnClickListener { operationAction(it) }
            buttonSubtract.setOnClickListener { operationAction(it) }
            buttonMultiply.setOnClickListener { operationAction(it) }
            buttonDivide.setOnClickListener { operationAction(it) }
            buttonEqual.setOnClickListener { equalAction() }
            buttonClear.setOnClickListener { allClearAction() }
            buttonBackspace.setOnClickListener { backSpaceAction() }
            buttonOpenParentheses.setOnClickListener { openParenthesesAction() }
            buttonCloseParentheses.setOnClickListener { closeParenthesesAction() }
        }
    }

    private fun numberAction(view: View) {
        if (view is Button) {
            if (view.text == ".") {
                if (canAddDecimal)
                    binding.workingsTV.append(view.text)
                canAddDecimal = false
            } else
                binding.workingsTV.append(view.text)
            canAddOperation = true
        }
    }

    private fun operationAction(view: View) {
        if (view is Button) {
            if (canAddOperation) {
                binding.workingsTV.append(view.text)
                canAddOperation = false
                canAddDecimal = true
            }
        }
    }

    private fun openParenthesesAction() {
        if (binding.workingsTV.text.isNotEmpty() && binding.workingsTV.text.last().isDigit()) {
            binding.workingsTV.append("×(") // Append multiplication operator before opening parenthesis
        } else {
            binding.workingsTV.append("(")
        }
        openParentheses = false
    }

    private fun closeParenthesesAction() {
        var openParenthesesCount = 0
        for (char in binding.workingsTV.text) {
            if (char == '(') {
                openParenthesesCount++
            } else if (char == ')') {
                openParenthesesCount--
            }
        }

        if (openParenthesesCount > 0) {
            binding.workingsTV.append(")")
            openParenthesesCount--
        }
    }

    fun allClearAction() {
        binding.workingsTV.text = ""
        binding.resultsTV.text = ""
    }

    fun backSpaceAction() {
        val length = binding.workingsTV.length()
        if (length > 0)
            binding.workingsTV.text = binding.workingsTV.text.subSequence(0, length - 1)
    }

    fun equalAction() {
        binding.resultsTV.text = calculateResults()
    }

    private fun calculateResults(): String {
        val expression = binding.workingsTV.text.toString()
        try {
            val result = evaluateExpression(expression)
            return result.toString()
        } catch (e: Exception) {
            return "Error"
        }
    }

    private fun evaluateExpression(expression: String): Float {
        val operators = mutableListOf<Char>()
        val operands = mutableListOf<Float>()
        var number = ""
        for (char in expression) {
            if (char.isDigit() || char == '.') {
                number += char
            } else {
                if (number.isNotEmpty()) {
                    operands.add(number.toFloat())
                    number = ""
                }
                when (char) {
                    '(' -> {
                        operators.add(char)
                    }
                    ')' -> {
                        while (operators.isNotEmpty() && operators.last() != '(') {
                            val second = operands.removeLast()
                            val first = operands.removeLast()
                            operands.add(performOperation(operators.removeLast(), first, second))
                        }
                        operators.removeLast() // Remove '('
                    }
                    else -> {
                        while (operators.isNotEmpty() && precedence(char) <= precedence(operators.last())) {
                            val second = operands.removeLast()
                            val first = operands.removeLast()
                            operands.add(performOperation(operators.removeLast(), first, second))
                        }
                        operators.add(char)
                    }
                }
            }
        }
        if (number.isNotEmpty()) {
            operands.add(number.toFloat())
        }
        while (operators.isNotEmpty()) {
            val second = operands.removeLast()
            val first = operands.removeLast()
            operands.add(performOperation(operators.removeLast(), first, second))
        }
        return operands.first()
    }

    private fun precedence(operator: Char): Int {
        return when (operator) {
            '+', '-' -> 1
            '×', '/' -> 2
            else -> 0
        }
    }

    private fun performOperation(operator: Char, operand1: Float, operand2: Float): Float {
        return when (operator) {
            '+' -> operand1 + operand2
            '-' -> operand1 - operand2
            '×' -> operand1 * operand2
            '/' -> operand1 / operand2
            else -> throw IllegalArgumentException("Invalid operator")
        }
    }
}
