package com.xenon.calculator.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.xenon.calculator.R
import org.json.JSONException
import java.util.Locale

class ConverterActivity : BaseActivity() {

    private lateinit var spinner: Spinner
    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var text1: TextInputEditText
    private lateinit var text2: TextInputEditText
    private var isConverting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)

        // Set up the toolbar
        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Handle back button press using OnBackPressedDispatcher
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }

        })

        // Initialize views
        spinner = findViewById(R.id.spinner)
        spinner1 = findViewById(R.id.spinner1)
        spinner2 = findViewById(R.id.spinner2)
        text1 = findViewById(R.id.text1)
        text2 = findViewById(R.id.text2)

        // Set up the main spinner adapter
        ArrayAdapter.createFromResource(
            this,
            R.array.spinner_items,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        // Set up spinner listeners
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateSpinners(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                convertValue()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                convertValue()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Set up text input listener
        text1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                convertValue()
            }
        })
        text2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!isConverting) {
                    isConverting = true
                    convertValue(isInputFromText2 = true)
                    isConverting = false
                }
            }
        })
    }

    // Update sub-spinners based on main spinner selection
    private fun updateSpinners(position: Int) {
        when (position) {
            0 -> { // Currency
                setSpinnerAdapter(spinner1, R.array.currency_units)
                setSpinnerAdapter(spinner2, R.array.currency_units)
            }
            1 -> { // Length
                setSpinnerAdapter(spinner1, R.array.length_units)
                setSpinnerAdapter(spinner2, R.array.length_units)
            }
            2 -> { // Area
                setSpinnerAdapter(spinner1, R.array.area_units)
                setSpinnerAdapter(spinner2, R.array.area_units)
            }
            3 -> { // Volume
                setSpinnerAdapter(spinner1, R.array.volume_units)
                setSpinnerAdapter(spinner2, R.array.volume_units)
            }
            4 -> { // Mass
                setSpinnerAdapter(spinner1, R.array.mass_units)
                setSpinnerAdapter(spinner2, R.array.mass_units)
            }
            5 -> { // Temperature
                setSpinnerAdapter(spinner1, R.array.temperature_units)
                setSpinnerAdapter(spinner2, R.array.temperature_units)
            }
        }
    }

    // Set spinner adapter with given array resource
    private fun setSpinnerAdapter(spinner: Spinner, arrayId: Int) {
        ArrayAdapter.createFromResource(
            this,
            arrayId,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
    }

    // Perform the conversion
    private fun convertValue(isInputFromText2: Boolean = false) {
        val inputValue = if (isInputFromText2) {
            text2.text.toString().toDoubleOrNull()
        } else {
            text1.text.toString().toDoubleOrNull()
        } ?: return

        val (fromUnit, toUnit) = if (isInputFromText2) {
            Pair(spinner2.selectedItemPosition, spinner1.selectedItemPosition)
        } else {
            Pair(spinner1.selectedItemPosition, spinner2.selectedItemPosition)
        }

        val convertedValue = when (spinner.selectedItemPosition) {
            5 -> { // Temperature
                getTemperatureConversionFactor(inputValue, fromUnit, toUnit)
            }
            else -> {
                val conversionFactor = getConversionFactor(
                    spinner.selectedItemPosition,
                    fromUnit, // Pass fromUnit as Int
                    toUnit // Pass toUnit as Int
                )
                inputValue * conversionFactor
            }
        }

        // Update the appropriate field
        if (isInputFromText2) {
            text1.setText(String.format(Locale.getDefault(), "%.2f", convertedValue))
        } else {
            text2.setText(String.format(Locale.getDefault(), "%.2f", convertedValue))
        }
    }

    // Get the conversion factor based on conversion type and units
    private fun getConversionFactor(conversionType: Int, fromUnit: Int, toUnit: Int): Double {
        return when (conversionType) {
            0 -> getCurrencyConversionFactor(fromUnit, toUnit) // Currency
            1 -> getLengthConversionFactor(fromUnit, toUnit) // Length
            2 -> getAreaConversionFactor(fromUnit, toUnit) // Area
            3 -> getVolumeConversionFactor(fromUnit, toUnit) // Volume
            4 -> getMassConversionFactor(fromUnit, toUnit) // Mass
            else -> 1.0 // Default
        }
    }

    // Get currency conversion factor from Firebase
    private fun getCurrencyConversionFactor(fromUnit: Int, toUnit: Int): Double {
        val currencies = resources.getStringArray(R.array.currency_units_codes) // Use currency codes array
        val fromCurrency = currencies[fromUnit]
        val toCurrency = currencies[toUnit]
        val apiUrl = "https://v6.exchangerate-api.com/v6/pair/$fromCurrency/$toCurrency"

        val requestQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, apiUrl, null,
            { response ->
                Log.d("API Response", response.toString())

                try {
                    val conversionRate = response.getDouble("conversion_rate")
                    // Update UI with conversionRate
                    val inputValue = text1.text.toString().toDoubleOrNull() ?: return@JsonObjectRequest
                    val convertedValue = inputValue * conversionRate
                    text2.setText(String.format(Locale.getDefault(), "%.2f", convertedValue))
                } catch (e: JSONException) {
                    e.printStackTrace()
                    // Handle JSON parsing error
                }
            },
            { error ->
                error.printStackTrace()
                // Handle network error
            }
        )

        requestQueue.add(jsonObjectRequest)
        return 1.0 // Return a default value while fetching online
    }


    private fun getLengthConversionFactor(fromUnit: Int, toUnit: Int): Double {
        val factors = arrayOf(1.0, 100.0, 0.001) // Meter, Centimeter, Kilometer
        return factors[toUnit] / factors[fromUnit]
    }
    private fun getAreaConversionFactor(fromUnit: Int, toUnit: Int): Double {
        val factors = arrayOf(1.0, 10000.0, 0.0001) // Square Meter, Square Centimeter, Square Kilometer
        return factors[toUnit] / factors[fromUnit]
    }


    private fun getVolumeConversionFactor(fromUnit: Int, toUnit: Int): Double {
        val factors = arrayOf(1.0, 1000.0, 0.001) // Liter, Milliliter, Cubic Meter
        return factors[toUnit] / factors[fromUnit]
    }

    private fun getMassConversionFactor(fromUnit: Int, toUnit: Int): Double {
        val factors = arrayOf(1.0, 1000.0, 0.001) // Kilogram, Gram, Tonne
        return factors[toUnit] / factors[fromUnit]
    }

    private fun getTemperatureConversionFactor(value: Double, fromUnit: Int, toUnit: Int): Double {
        return when (fromUnit) {
            0 -> { // Celsius
                when (toUnit) {
                    0 -> value // Celsius
                    1 -> (9.0 / 5.0) * value + 32.0 // Fahrenheit
                    2 -> value + 273.15 // Kelvin
                    else -> value
                }
            }
            1 -> { // Fahrenheit
                when (toUnit) {
                    0 -> (value - 32.0) * (5.0 / 9.0) // Celsius
                    1 -> value // Fahrenheit
                    2 -> (value - 32.0) * (5.0 / 9.0) + 273.15 // Kelvin
                    else -> value
                }
            }
            2 -> { // Kelvin
                when (toUnit) {
                    0 -> value - 273.15 // Celsius
                    1 -> (value - 273.15) * (9.0 / 5.0) + 32.0 // Fahrenheit
                    2 -> value // Kelvin
                    else -> value
                }
            }
            else -> value
        }
    }
}