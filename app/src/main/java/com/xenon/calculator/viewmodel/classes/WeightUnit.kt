package com.xenon.calculator.viewmodel.classes

enum class WeightUnit(
    val displayName: String,
    val toBase: (Double) -> Double,
    val fromBase: (Double) -> Double
) {
    KILOGRAMS("Kilograms", { it }, { it }),
    GRAMS("Grams", { it / 1000.0 }, { it * 1000.0 }),
    MILLIGRAMS("Milligrams", { it / 1_000_000.0 }, { it * 1_000_000.0 }),
    MICROGRAMS("Micrograms", {it / 1_000_000_000.0 }, { it * 1_000_000_000.0 }),
    METRIC_TONNES("Metric Tonnes", { it * 1000.0 }, { it / 1000.0 }),
    IMPERIAL_TONS("Imperial Tons (UK)", { it * 1016.0469088 }, { it / 1016.0469088 }),
    US_TONS("US Tons", { it * 907.18474 }, { it / 907.18474 }),
    POUNDS("Pounds", { it * 0.45359237 }, { it / 0.45359237 }),
    OUNCES("Ounces", { it * 0.028349523125 }, { it / 0.028349523125 }),
    CARATS("Carats", { it * 0.0002 }, { it / 0.0002 }),
    STONES("Stones", { it * 6.35029318 }, { it / 6.35029318 });
}