package com.dnnsgnzls.jetpack.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dnnsgnzls.jetpack.R
import com.dnnsgnzls.jetpack.databinding.HeroItemBinding
import com.dnnsgnzls.jetpack.models.Hero
import com.dnnsgnzls.jetpack.models.IHeroClick
import com.dnnsgnzls.jetpack.util.getProgressDrawable
import com.dnnsgnzls.jetpack.util.loadImage


class HeroAdapter(
    private var list: List<Hero>,
    private val clickable: IHeroClick
) :
    RecyclerView.Adapter<HeroAdapter.HeroViewHolder>() {

    class HeroViewHolder(private val binding: HeroItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hero: Hero, clickable: IHeroClick) {
            // hero name
            binding.cardTitle.text = hero.localizedName

            // hero description - using string format
            val heroDescStringFormat = itemView.context.getString(R.string.hero_details)
            binding.cardInformation.text = hero.getDescription(heroDescStringFormat)

            // load hero images using Glide - via kotlin extensions
            binding.cardProfile.loadImage(
                hero.iconUrl,
                getProgressDrawable(itemView.context)
            )

            binding.cardBackground.loadImage(
                hero.fullImageUrl,
                getProgressDrawable(itemView.context)
            )

            // setup click listener
            binding.cardButton.setOnClickListener {
                clickable.onClick(it, hero)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val binding = HeroItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeroViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        holder.bind(list[position], clickable)
    }

    override fun getItemCount() = list.size

    fun updateList(newList: List<Hero>) {
        val diffResult = DiffUtil.calculateDiff(HeroDiffCallback(list, newList))
        this.list = newList
        diffResult.dispatchUpdatesTo(this)
    }
}