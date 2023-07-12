package com.dnnsgnzls.mvvm.models.enums

private const val CARRY = "Carry"
private const val DISABLER = "Disabler"
private const val DURABLE = "Durable"
private const val ESCAPE = "Escape"
private const val INITIATOR = "Initiator"
private const val NUKER = "Nuker"
private const val PUSHER = "Pusher"
private const val SUPPORT = "Support"

enum class Role(val value: String) {
    Carry(CARRY),
    Disabler(DISABLER),
    Durable(DURABLE),
    Escape(ESCAPE),
    Initiator(INITIATOR),
    Nuker(NUKER),
    Pusher(PUSHER),
    Support(SUPPORT);

    companion object {
        public fun fromValue(value: String): Role = when (value) {
            CARRY -> Carry
            DISABLER -> Disabler
            DURABLE -> Durable
            ESCAPE -> Escape
            INITIATOR -> Initiator
            NUKER -> Nuker
            PUSHER -> Pusher
            SUPPORT -> Support
            else -> throw IllegalArgumentException("Invalid role: $value")
        }
    }
}