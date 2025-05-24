package com.xenon.calculator.viewmodel.classes

enum class CurrencyUnit(val displayName: String, val code: String) { // code is standard currency code
    USD("US Dollar", "USD"),
    EUR("Euro", "EUR"),
    GBP("British Pound", "GBP"),
    JPY("Japanese Yen", "JPY"),
    INR("Indian Rupee", "INR");
    // Add more currencies
    // In a real app, conversion rates would be fetched dynamically
}