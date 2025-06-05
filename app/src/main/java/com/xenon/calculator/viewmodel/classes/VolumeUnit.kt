package com.xenon.calculator.viewmodel.classes

enum class VolumeUnit(
    val displayName: String,
    val toBaseFactor: Double) {
    MILLILITERS("Milliliters", 1.0), //ml
    LITERS("Liters", 1000.0), //l
    CUBIC_METERS("Cubic Meters", 1_000_000.0), //m^3
    CUBIC_DECIMETERS("Cubic Decimeters", 1_000.0), //dm^3
    CUBIC_CENTIMETERS("Cubic Centimeters", 1.0), //cm^3
    CUBIC_MILLIMETERS("Cubic Millimeters", 0.001), //mm^3
    CUBIC_INCHES("Cubic Inches", 16.39), //in^3
    CUBIC_FEET("Cubic Feet", 28.3168), //ft^3
    GALLONS_US("US Gallons", 3785.41), //us-gal
    GALLONS_UK("UK Gallons", 4546.09), //uk-gal
    QUART_US("US Quart", 946.353), //qt
    QUART_UK("UK Quarts", 1136.52), //uk-qt
    TEASPOON_US("US Teaspoon", 4.93), //tsp
    TEASPOON_UK("UK Teaspoon", 5.92), //uk-tsp
    TABLESPOON_US("US Tablespoon", 14.786), //tbsp
    TABLESPOON_UK("UK Tablespoon", 17.76), //uk-tbsp
    PINTS_US("US Pints", 473.18), //pt
    PINTS_UK("UK Pints", 568.26), //uk-pt
    CUP_US("US Cups", 236.588), //cup
    CUP_UK("UK Cups", 284.13), //uk-cup
    FLUID_OUNCE_US("US Fluid Ounces", 29.5735), //fl-oz
    FLUID_OUNCE_UK("UK Fluid Ounces", 28.41); //uk-fl-oz

    fun fromBase(baseValue: Double): Double = baseValue / toBaseFactor
    fun toBase(value: Double): Double = value * toBaseFactor
}