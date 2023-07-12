package com.dnnsgnzls.mvvm.models.enums

enum class PrimaryAttr(val value: String) {
    All("all"),
    Agi("agi"),
    PrimaryAttrInt("int"),
    Str("str");

    companion object {
        public fun fromValue(value: String): PrimaryAttr = when (value) {
            "agi" -> Agi
            "all" -> All
            "int" -> PrimaryAttrInt
            "str" -> Str
            else -> throw IllegalArgumentException("Invalid primary attribute: $value")
        }
    }
}