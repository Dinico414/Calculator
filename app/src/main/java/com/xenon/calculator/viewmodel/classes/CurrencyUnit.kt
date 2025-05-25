package com.xenon.calculator.viewmodel.classes

enum class CurrencyUnit(val displayName: String, val code: String) { // code is standard currency code
    USD("US Dollar", "USD"),
    EUR("Euro", "EUR"),
    GBP("British Pound", "GBP"),
    JPY("Japanese Yen", "JPY"),
    INR("Indian Rupee", "INR"),
    AUD("Australian Dollar", "AUD"),
    CAD("Canadian Dollar", "CAD"),
    CHF("Swiss Franc", "CHF"),
    CNY("Chinese Yuan", "CNY"),
    SEK("Swedish Krona", "SEK"),
    NZD("New Zealand Dollar", "NZD"),
    MXN("Mexican Peso", "MXN"),
    SGD("Singapore Dollar", "SGD"),
    HKD("Hong Kong Dollar", "HKD"),
    NOK("Norwegian Krone", "NOK"),
    KRW("South Korean Won", "KRW"),
    TRY("Turkish Lira", "TRY"),
    RUB("Russian Ruble", "RUB"),
    BRL("Brazilian Real", "BRL"),
    ZAR("South African Rand", "ZAR");
    // In a real app, conversion rates would be fetched dynamically
}