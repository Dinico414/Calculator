package com.xenonware.calculator.viewmodel.classes

import kotlinx.serialization.Serializable

@Serializable
data class HistoryItem(
    val id: Long = System.currentTimeMillis(),
    val expression: String,
    val result: String
)