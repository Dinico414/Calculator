package com.xenon.calculator

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.xenon.calculator.databinding.ActivityMainBinding
import kotlin.math.*

@Suppress("DEPRECATION", "UNUSED_CHANGED_VALUE")
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


        val toggleScientificButton = findViewById<Button>(R.id.toggleScientificButton)
        val scientificButtonsLayout = findViewById<LinearLayout>(R.id.scientificButtonsLayout)

        toggleScientificButton.setOnClickListener {
            if (scientificButtonsLayout.visibility == View.VISIBLE) {
                // Fade out and slide up animation
                val fadeOut = ObjectAnimator.ofFloat(scientificButtonsLayout, "alpha", 1f, 0f)
                val slideUp = ObjectAnimator.ofFloat(scientificButtonsLayout, "translationY", 0f, -scientificButtonsLayout.height.toFloat())

                val fadeOutDuration = 200L // Fade out faster
                val slideUpDuration = 300L // Slide up a bit slower

                fadeOut.duration = fadeOutDuration
                slideUp.duration = slideUpDuration

                val animatorSet = AnimatorSet()
                animatorSet.playTogether(fadeOut, slideUp)
                animatorSet.startDelay = slideUpDuration / 2 // Start fade out halfway through slide up animation

                animatorSet.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        scientificButtonsLayout.visibility = View.GONE
                    }
                })

                animatorSet.start()

                // Flip text vertically
                val flipImageUp = ObjectAnimator.ofFloat(binding.toggleScientificButtonImageView, "rotationX", 180f, 0f)
                flipImageUp.duration = 300 // Flip text smoothly
                flipImageUp.start()

                // Adjust other button heights
                binding.root.forEach { view ->
                    if (view is Button && view != toggleScientificButton) {
                        val slideUpOther = ObjectAnimator.ofFloat(view, "translationY", 0f, -scientificButtonsLayout.height.toFloat())
                        slideUpOther.duration = slideUpDuration
                        slideUpOther.start()
                    }
                }
            } else {
                // Fade in and slide down animation
                val fadeIn = ObjectAnimator.ofFloat(scientificButtonsLayout, "alpha", 0f, 1f)
                val slideDown = ObjectAnimator.ofFloat(scientificButtonsLayout, "translationY", -scientificButtonsLayout.height.toFloat(), 0f)

                val fadeInDuration = 400L // Fade in a bit slower
                val slideDownDuration = 300L // Slide down a bit slower

                fadeIn.duration = fadeInDuration
                slideDown.duration = slideDownDuration

                val animatorSet = AnimatorSet()
                animatorSet.playTogether(fadeIn, slideDown)
                animatorSet.startDelay = slideDownDuration / 2 // Start fade in halfway through slide down animation

                scientificButtonsLayout.visibility = View.VISIBLE

                // Flip text vertically
                val flipTextDown = ObjectAnimator.ofFloat(binding.toggleScientificButtonImageView, "rotationX", 0f, 180f)
                flipTextDown.duration = 300 // Flip text smoothly
                flipTextDown.start()

                // Adjust other button heights
                binding.root.forEach { view ->
                    if (view is Button && view != toggleScientificButton) {
                        val slideDownOther = ObjectAnimator.ofFloat(view, "translationY", -scientificButtonsLayout.height.toFloat(), 0f)
                        slideDownOther.duration = slideDownDuration
                        slideDownOther.start()
                    }
                }

                animatorSet.start()
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
            buttonAdd.setOnClickListener { operationAction(it) }
            buttonSubtract.setOnClickListener { operationAction(it) }
            buttonMultiply.setOnClickListener { operationAction(it) }
            buttonDivide.setOnClickListener { operationAction(it) }
            buttonEqual.setOnClickListener { equalAction() }
            buttonClear.setOnClickListener { allClearAction() }
            buttonBackspace.setOnClickListener { backSpaceAction() }
            buttonOpenParentheses.setOnClickListener { openParenthesesAction() }
            buttonCloseParentheses.setOnClickListener { closeParenthesesAction() }
            buttonSin.setOnClickListener { scientificOperationAction("sin(") }
            buttonCos.setOnClickListener { scientificOperationAction("cos(") }
            buttonTan.setOnClickListener { scientificOperationAction("tan(") }
            buttonSqrt.setOnClickListener { scientificOperationAction("√(") }
            buttonPower.setOnClickListener { operationAction(Button(this@MainActivity).apply { text = "^" }) }
            buttonPi.setOnClickListener { addPi() }
            buttonRadDeg.setOnClickListener { switchRadDeg() }
            buttonInverse.setOnClickListener { inverseAction() }
            buttonPercent.setOnClickListener { scientificOperationAction("%") }
            buttonLn.setOnClickListener { scientificOperationAction("ln(") }
            buttonLog.setOnClickListener { scientificOperationAction("log(") }
            buttonE.setOnClickListener { numberAction(it) }
            buttonFactorial.setOnClickListener { scientificOperationAction("!(") }

        }
        setupButtonAnimations()
    }

    private fun setupButtonAnimations() {
        binding.root.forEach { view ->
            if (view is Button) {
                val stateListAnimator = AnimatorInflater.loadStateListAnimator(this, R.animator.button_pressed_anim)
                view.stateListAnimator = stateListAnimator
            }
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

    private fun openParenthesesAction() {
        if (binding.workingsTV.text.isNotEmpty() && binding.workingsTV.text.last().isDigit()) {
            binding.workingsTV.append("×(")
        } else {
            binding.workingsTV.append("(")
        }
        openParentheses = false
        performHapticFeedback()
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
        if (length > 0)
            binding.workingsTV.text = binding.workingsTV.text.subSequence(0, length - 1)
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
                    's', 'c', 't', '√', '^' -> {
                        val endIndex = expression.indexOf('(', operands.size)
                        val operation = expression.substring(expression.indexOf(char), endIndex + 1) // Include the closing parenthesis
                        operands.add(performScientificOperation(operation))
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

    private fun performScientificOperation(operation: String): Float {
        val operator = operation[0]
        val operand = operation.substring(1).toFloat()
        return when (operator) {
            's' -> {
                if (isInverse)
                    asin(operand)
                else
                    sin(operand)
            }
            'c' -> {
                if (isInverse)
                    acos(operand)
                else
                    cos(operand)
            }
            't' -> {
                if (isInverse)
                    atan(operand)
                else
                    tan(operand)
            }
            '√' -> sqrt(operand)
            '^' -> operand.pow(operand.toInt())
            else -> throw IllegalArgumentException("Invalid scientific operation")
        }
    }

    private fun scientificOperationAction(operation: String) {
        binding.workingsTV.append(operation)
        canAddDecimal = true
        canAddOperation = false
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
        binding.buttonRadDeg.text = if (isRadians) "DEG" else "RAD"
        performHapticFeedback()
    }

    private var isInverse = false
    private fun inverseAction() {
        isInverse = !isInverse
        binding.buttonInverse.text = if (isInverse) "INV" else "INV"
        performHapticFeedback()
    }
}
