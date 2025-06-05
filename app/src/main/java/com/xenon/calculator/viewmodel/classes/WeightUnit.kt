package com.xenon.calculator.viewmodel.classes

enum class WeightUnit(
    val displayName: String,
    val toBaseFactor: Double
) {
    METRIC_TONNES("Metric Tonnes", 1000.0 ), //T
    KILOGRAMS("Kilograms", 1.0 ), //Kg
    GRAMS("Grams", 1000.0 ), //g
    MILLIGRAMS("Milligrams", 1000000.0 ), //mg
    MICROGRAMS("Micrograms", 1000000000.0 ), //µg
    NANOGRAMS("Nanograms", 1000000000000.0 ), //ng
    IMPERIAL_TONS("Imperial Tons (UK)", 1016.0469088 ), //uk-T
    US_TONS("US Tons", 907.18474 ), // us-T
    POUNDS("Pounds", 0.45359237 ), //lb
    OUNCES("Ounces", 0.028349523125 ), //oz
    GRAIN("Grain", 0.00006479891 ), //gr
    CARATS("Carats", 0.0002 ), //ct
    STONES("Stones", 6.35029318 ); //st

    fun fromBase(baseValue: Double): Double = baseValue * toBaseFactor
    fun toBase(value: Double): Double = value / toBaseFactor
}