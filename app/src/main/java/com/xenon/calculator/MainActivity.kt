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
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
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
                operators.push(c)
            } else if (c == ')') {
                while (operators.peek() != '(') {
                    numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()))
                }
                operators.pop() // Remove '('
            } else if (isOperator(c)) {
                while (!operators.empty() && hasPrecedence(c, operators.peek())) {
                    numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()))
                }
                operators.push(c)
                number = "" // Reset number after an operator
            } else if (isScientificFunction(c)) {
                val funcEnd = findFunctionEnd(expression, i)
                val func = expression.substring(i, funcEnd + 1)
                val result = evaluateScientificFunction(func)
                numbers.push(result)
                i = funcEnd // Skip the processed function
                number = "" // Reset number after a function
            }
            i++
        }

        if (number.isNotEmpty()) {
            numbers.push(number.toDouble())
        }

        while (!operators.empty()) {
            numbers.push(applyOp(operators.pop(), numbers.pop(), numbers.pop()))
        }

        return numbers.pop()
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
        return -1 // Error: unmatched parentheses
    }

    private fun evaluateScientificFunction(func: String): Double {

        val operator = func[0]
        val operandStr = func.substring(2, func.length - 1)
        val operand = evaluateExpression(operandStr)

        return when (operator) {
            's' -> {
                val value = if (isRadians) operand else Math.toRadians(operand)
                val result = if (isInverse) asin(value) else sin(value)
                result
            }
            'c' -> {
                val value = if (isRadians) operand else Math.toRadians(operand)
                val result = if (isInverse) acos(value) else cos(value)
                result
            }
            't' -> {
                val value = if (isRadians) operand else Math.toRadians(operand)
                val result = if (isInverse) atan(value) else tan(value)
                result
            }
            '√' -> {
                val result = if (isInverse) operand * operand else sqrt(operand)
                result
            }
            '!' -> factorial(operand.toInt())
            'l' -> {
                val result = if (func.startsWith("ln")) ln(operand) else log10(operand)
                result
            }
            '%' -> operand / 100.0
            else -> throw IllegalArgumentException("Invalid scientific operation")
        }
    }

    private fun isScientificFunction(c: Char): Boolean = c in listOf('s', 'c', 't', '√', '!', 'l')

    private fun isOperator(c: Char): Boolean = c in listOf('+', '-', '×', '/', '^')

    private fun hasPrecedence(op1: Char, op2: Char): Boolean {
        if (op2 == '(' || op2 == ')') return false
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
        if (operation != "%" && operation != "!(") {
            if (binding.workingsTV.text.isNotEmpty() && binding.workingsTV.text.last() == '(') {
                binding.workingsTV.append(operation.substring(1)) // Remove the first character (which is '(')
            } else {
                binding.workingsTV.append(operation)
            }
        } else {
            binding.workingsTV.append(operation)
        }
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
        binding.buttonInverse.text = if (isInverse) "INV'" else "INV"
        performHapticFeedback()
    }
}
