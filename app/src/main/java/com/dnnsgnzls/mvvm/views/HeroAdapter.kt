package com.dnnsgnzls.mvvm.views

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dnnsgnzls.mvvm.R
import com.dnnsgnzls.mvvm.models.Hero


class HeroAdapter(private val list: List<Hero>) : RecyclerView.Adapter<HeroAdapter.HeroViewHolder>() {

    class HeroViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.cardTitle)
        val info: TextView = view.findViewById(R.id.cardInformation)
        val profleImage: ImageView = view.findViewById(R.id.cardProfile)
        val coverImage: ImageView = view.findViewById((R.id.cardBackground))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.hero_item,
            parent, false)

        return HeroViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        val currentItem = list[position]

        // hero name
        holder.title.text = currentItem.localizedName

        // hero description
        val heroDetailsStringFormat: String = holder.itemView.context.getString(R.string.hero_details)
        holder.info.text = String.format(heroDetailsStringFormat, currentItem.attackType.value, currentItem.primaryAttr.value, currentItem.roles.joinToString(", "))
    }

    override fun getItemCount() = list.size
}