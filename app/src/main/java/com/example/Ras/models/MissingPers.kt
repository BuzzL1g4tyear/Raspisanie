package com.example.Ras.models

data class MissingPers(
    var Group: String = "",
    var Disease: List<String> = listOf(),
    var Order: List<String> = listOf(),
    var Reason: List<String> = listOf(),
    var Statement: List<String> = listOf(),
    var TimeStamp: Any = ""
)