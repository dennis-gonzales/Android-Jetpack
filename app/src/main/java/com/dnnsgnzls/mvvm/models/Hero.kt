package com.dnnsgnzls.mvvm.models

import com.dnnsgnzls.mvvm.models.enums.AttackType
import com.dnnsgnzls.mvvm.models.enums.ImageType
import com.dnnsgnzls.mvvm.models.enums.PrimaryAttribute
import com.dnnsgnzls.mvvm.models.enums.Role
import com.google.gson.annotations.SerializedName

data class Hero(
    val id: Int,

    val name: String,

    @SerializedName("localized_name")
    val localizedName: String,

    @SerializedName("primary_attr")
    val primaryAttribute: PrimaryAttribute,

    @SerializedName("attack_type")
    val attackType: AttackType,

    val roles: List<Role>,

    val legs: Int
)  {
    private fun getImageUrl(type: ImageType): String {
        val typeName = when (type) {
            ImageType.ICON -> "icon"
            ImageType.FULL -> "full"
        }

        // Move the api string to constants
        return "https://api.opendota.com/apps/dota2/images/heroes/${name.removePrefix("npc_dota_hero_")}_${typeName}.png"
    }

    val fullImageUrl: String
        get() = getImageUrl(ImageType.FULL)

    val iconUrl: String
        get() = getImageUrl(ImageType.ICON)
}

