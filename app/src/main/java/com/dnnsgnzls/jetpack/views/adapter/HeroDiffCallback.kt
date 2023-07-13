package com.dnnsgnzls.jetpack.views.adapter

import androidx.recyclerview.widget.DiffUtil
import com.dnnsgnzls.jetpack.models.Hero

class HeroDiffCallback(
    private val oldList: List<Hero>,
    private val newList: List<Hero>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Checks if two items represent the same entity or object, typically by comparing their unique IDs.
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Checks if the data of two items is the same, i.e., whether the contents of an item have changed.
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}