package com.dnnsgnzls.mvvm.models.enums

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val CARRY = "Carry"
private const val DISABLER = "Disabler"
private const val DURABLE = "Durable"
private const val ESCAPE = "Escape"
private const val INITIATOR = "Initiator"
private const val NUKER = "Nuker"
private const val PUSHER = "Pusher"
private const val SUPPORT = "Support"

@Entity
enum class Role(val value: String) {
    @ColumnInfo(CARRY)
    Carry(CARRY),

    @ColumnInfo(DISABLER)
    Disabler(DISABLER),

    @ColumnInfo(DURABLE)
    Durable(DURABLE),

    @ColumnInfo(ESCAPE)
    Escape(ESCAPE),

    @ColumnInfo(INITIATOR)
    Initiator(INITIATOR),

    @ColumnInfo(NUKER)
    Nuker(NUKER),

    @ColumnInfo(PUSHER)
    Pusher(PUSHER),

    @ColumnInfo(SUPPORT)
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

class RoleListTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromRoleList(roles: List<Role>): String {
        return gson.toJson(roles)
    }

    @TypeConverter
    fun toRoleList(data: String): List<Role> {
        val listType = object : TypeToken<List<Role>>() {}.type
        return gson.fromJson(data, listType)
    }
}
