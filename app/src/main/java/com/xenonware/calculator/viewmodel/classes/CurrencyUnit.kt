package com.xenonware.calculator.viewmodel.classes

import android.content.Context
import androidx.annotation.StringRes
import com.xenon.calculator.R

data class CurrencyData(
    @StringRes val displayNameResId: Int,
    val factor: Double
)

private val currencyDataMap: Map<String, CurrencyData> = mapOf(
    "AED" to CurrencyData(R.string.currency_aed, 3.673),
    "ALL" to CurrencyData(R.string.currency_all, 85.750),
    "AMD" to CurrencyData(R.string.currency_amd, 383.26),
    "AUD" to CurrencyData(R.string.currency_aud, 1.533),
    "AZN" to CurrencyData(R.string.currency_azn, 1.7),
    "BAM" to CurrencyData(R.string.currency_bam, 1.686),
    "BGN" to CurrencyData(R.string.currency_bgn, 1.707),
    "BRL" to CurrencyData(R.string.currency_brl, 5.594),
    "BYN" to CurrencyData(R.string.currency_byn, 3.27),
    "CAD" to CurrencyData(R.string.currency_cad, 1.365),
    "CHF" to CurrencyData(R.string.currency_chf, 0.820),
    "CNY" to CurrencyData(R.string.currency_cny, 7.165),
    "CZK" to CurrencyData(R.string.currency_czk, 21.657),
    "DKK" to CurrencyData(R.string.currency_dkk, 6.51),
    "EUR" to CurrencyData(R.string.currency_eur, 0.87),
    "GBP" to CurrencyData(R.string.currency_gbp, 0.736),
    "GEL" to CurrencyData(R.string.currency_gel, 2.722),
    "HKD" to CurrencyData(R.string.currency_hkd, 7.846),
    "HUF" to CurrencyData(R.string.currency_huf, 351.936),
    "INR" to CurrencyData(R.string.currency_inr, 85.83),
    "ISK" to CurrencyData(R.string.currency_isk, 128.5),
    "JPY" to CurrencyData(R.string.currency_jpy, 143.505),
    "KRW" to CurrencyData(R.string.currency_krw, 1335.57),
    "MDL" to CurrencyData(R.string.currency_mdl, 17.21),
    "MKD" to CurrencyData(R.string.currency_mkd, 53.57),
    "MXN" to CurrencyData(R.string.currency_mxn, 19.166),
    "NOK" to CurrencyData(R.string.currency_nok, 10.049),
    "NZD" to CurrencyData(R.string.currency_nzd, 1.651),
    "PLN" to CurrencyData(R.string.currency_pln, 3.736),
    "RON" to CurrencyData(R.string.currency_ron, 4.408),
    "RSD" to CurrencyData(R.string.currency_rsd, 102.334),
    "RUB" to CurrencyData(R.string.currency_rub, 79.383),
    "SEK" to CurrencyData(R.string.currency_sek, 9.556),
    "SGD" to CurrencyData(R.string.currency_sgd, 1.285),
    "TRY" to CurrencyData(R.string.currency_try, 39.27),
    "UAH" to CurrencyData(R.string.currency_uah, 41.526),
    "USD" to CurrencyData(R.string.currency_usd, 1.0),
    "ZAR" to CurrencyData(R.string.currency_zar, 17.73)
).toSortedMap(String.CASE_INSENSITIVE_ORDER)

enum class CurrencyUnit {
    USD, EUR, JPY, GBP, AED, AUD, CAD, CHF, CNY, INR, BRL, ZAR, SGD, HKD, NZD, KRW, MXN, TRY,
    ALL, AMD, AZN, BAM, BGN, BYN, CZK, DKK, GEL, HRK, HUF, ISK, MDL, MKD, NOK, PLN, RON, RSD, RUB, SEK, UAH;


    fun getDisplayName(context: Context): String {
        val currencyData = currencyDataMap[this.name]
        return currencyData?.let {
            context.getString(it.displayNameResId)
        } ?: "Unknown ${this.name}"
    }

    val toBaseFactor: Double
        get() = currencyDataMap[this.name]?.factor ?: 1.0


    fun fromBase(baseValue: Double): Double = baseValue * toBaseFactor
    fun toBase(value: Double): Double = value / toBaseFactor
}