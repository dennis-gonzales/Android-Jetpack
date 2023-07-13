package com.dnnsgnzls.jetpack.models.enums

import com.google.gson.annotations.SerializedName

private const val AGILITY = "agi"
private const val ALL = "all"
private const val INTELLIGENCE = "int"
private const val STRENGTH = "str"

enum class PrimaryAttribute(val value: String) {
    @SerializedName(AGILITY)
    Agility(AGILITY),

    @SerializedName(ALL)
    All(ALL),

    @SerializedName(INTELLIGENCE)
    Intelligence(INTELLIGENCE),

    @SerializedName(STRENGTH)
    Strength(STRENGTH);

    companion object {
        fun fromValue(value: String): PrimaryAttribute = when (value) {
            AGILITY -> Agility
            ALL -> All
            INTELLIGENCE -> Intelligence
            STRENGTH -> Strength
            else -> throw IllegalArgumentException("Invalid primary attribute: $value")
        }
    }
}