package com.example.Ras.models

data class User(
    var id: String = "",
    var Email: String = "",
    var Name: String = "",
    var Group: String = "",
    var Status: String = "",
    var FullName: String = "",

    var Phone: String = "",
    var CreatorID: String = "",

    var Text: String = "",
    var Type: String = "",
    var From: String = "",
    var TimeStamp: Any = ""
)