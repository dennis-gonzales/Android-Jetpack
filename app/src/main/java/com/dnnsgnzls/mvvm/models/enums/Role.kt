package com.dnnsgnzls.mvvm.models.enums

enum class Role(val value: String) {
    Carry("Carry"),
    Disabler("Disabler"),
    Durable("Durable"),
    Escape("Escape"),
    Initiator("Initiator"),
    Nuker("Nuker"),
    Pusher("Pusher"),
    Support("Support");

    companion object {
        public fun fromValue(value: String): Role = when (value) {
            "Carry" -> Carry
            "Disabler" -> Disabler
            "Durable" -> Durable
            "Escape" -> Escape
            "Initiator" -> Initiator
            "Nuker" -> Nuker
            "Pusher" -> Pusher
            "Support" -> Support
            else -> throw IllegalArgumentException("Invalid role: $value")
        }
    }
}