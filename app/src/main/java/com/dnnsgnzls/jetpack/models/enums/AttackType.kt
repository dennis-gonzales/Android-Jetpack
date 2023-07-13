package com.dnnsgnzls.jetpack.models.enums

private const val MELEE = "Melee"
private const val RANGED = "Ranged"

enum class AttackType(val value: String) {
    Melee(MELEE),
    Ranged(RANGED);

    companion object {
        public fun fromValue(value: String): AttackType = when (value) {
            MELEE -> Melee
            RANGED -> Ranged
            else -> throw IllegalArgumentException("Invalid attack type: $value")
        }
    }
}