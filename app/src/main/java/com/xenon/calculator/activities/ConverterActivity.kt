package com.xenon.calculator.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.xenon.calculator.R

class ConverterActivity : BaseActivity() {

    private lateinit var spinner: Spinner
    private lateinit var spinner1: Spinner
    private lateinit var spinner2: Spinner
    private lateinit var text1: TextInputEditText
    private lateinit var text2: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)

        val toolbar: MaterialToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        spinner = findViewById(R.id.spinner)
        spinner1 = findViewById(R.id.spinner1)
        spinner2 = findViewById(R.id.spinner2)
        text1 = findViewById(R.id.text1)
        text2 = findViewById(R.id.text2)

        ArrayAdapter.createFromResource(
            this,
            R.array.spinner_items,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                updateSpinners(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        spinner1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                convertValue()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                convertValue()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        text1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                convertValue()
            }
        })
    }

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

    private fun convertValue() {
        val inputValue = text1.text.toString().toDoubleOrNull() ?: return

        val conversionFactor = getConversionFactor(
            spinner.selectedItemPosition,
            spinner1.selectedItemPosition,
            spinner2.selectedItemPosition
        )

        val convertedValue = inputValue * conversionFactor
        text2.setText(convertedValue.toString())
    }

    private fun getConversionFactor(
        conversionType: Int,
        fromUnit: Int,
        toUnit: Int,
    ): Double {
        return when (conversionType) {
            0 -> getCurrencyConversionFactor(fromUnit.toString(), toUnit.toString())
            1 -> getLengthConversionFactor(fromUnit, toUnit)
            2 -> getAreaConversionFactor(fromUnit, toUnit)
            3 -> getVolumeConversionFactor(fromUnit, toUnit)
            4 -> getMassConversionFactor(fromUnit, toUnit)
            5 -> getTemperatureConversionFactor(fromUnit, toUnit)
            else -> 1.0
        }
    }

    private fun getCurrencyConversionFactor(fromUnit: String, toUnit: String): Double {
        val database = Firebase.database
        val ratesRef = database.getReference("currencies") // Adjust the path if needed

        ratesRef.addValueEventListener(object : com.google.firebase.database.ValueEventListener {
            override fun onDataChange(dataSnapshot: com.google.firebase.database.DataSnapshot) {
                val rates = dataSnapshot.value as? Map<String, Map<String, Double>>
                val conversionFactor = rates?.get(fromUnit)?.get(toUnit) ?: 1.0
                // Use conversionFactor to update the UI or perform calculations
                val inputValue = text1.text.toString().toDoubleOrNull() ?: return
                val convertedValue = inputValue * conversionFactor
                text2.setText(convertedValue.toString())
            }

            override fun onCancelled(error: com.google.firebase.database.DatabaseError) {
                // Handle errors
            }
        })
        return 1.0 // Return a default value while waiting for data
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

    private fun getTemperatureConversionFactor(fromUnit: Int, toUnit: Int): Double {
        return when (fromUnit) {
            0 -> { // Celsius
                when (toUnit) {
                    0 -> 1.0 // Celsius
                    1 -> (9.0 / 5.0) + 32.0 // Fahrenheit
                    2 -> +273.15 // Kelvin
                    else -> 1.0
                }
            }

            1 -> { // Fahrenheit
                when (toUnit) {
                    0 -> (5.0 / 9.0) * (-32.0) // Celsius
                    1 -> 1.0 // Fahrenheit
                    2 -> (5.0 / 9.0) * (-32.0) + 273.15 // Kelvin
                    else -> 1.0
                }
            }

            2 -> { // Kelvin
                when (toUnit) {
                    0 -> -273.15 // Celsius
                    1 -> (9.0 / 5.0) * (-273.15) + 32.0 // Fahrenheit
                    2 -> 1.0 // Kelvin
                    else -> 1.0
                }
            }

            else -> 1.0
        }
    }
}