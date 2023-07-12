package com.dnnsgnzls.mvvm.models.enums

enum class AttackType(val value: String) {
    Melee("Melee"),
    Ranged("Ranged");

    companion object {
        public fun fromValue(value: String): AttackType = when (value) {
            "Melee" -> Melee
            "Ranged" -> Ranged
            else -> throw IllegalArgumentException("Invalid attack type: $value")
        }
    }
}