package com.dnnsgnzls.jetpack.views.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dnnsgnzls.jetpack.R
import com.dnnsgnzls.jetpack.databinding.HeroItemBinding
import com.dnnsgnzls.jetpack.models.Hero
import com.dnnsgnzls.jetpack.models.IHeroClick
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HeroAdapter(
    private var list: List<Hero>,
    private val clickable: IHeroClick,
    private val scope: CoroutineScope
) :
    RecyclerView.Adapter<HeroAdapter.HeroViewHolder>() {

    class HeroViewHolder(
        private val binding: HeroItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(hero: Hero, clickable: IHeroClick) {
            binding.hero = hero

            binding.cardButton.setOnClickListener {
                clickable.onClick(it, hero)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<HeroItemBinding>(inflater, R.layout.hero_item, parent, false)
        return HeroViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        holder.bind(list[position], clickable)
    }

    override fun getItemCount() = list.size

    fun updateList(newList: List<Hero>, dispatcher: CoroutineDispatcher = Dispatchers.Default) {
        scope.launch {
            ensureActive() // Check for cancellation

            val diffResult = withContext(dispatcher) {
                DiffUtil.calculateDiff(HeroDiffCallback(list, newList))
            }

            list = newList
            diffResult.dispatchUpdatesTo(this@HeroAdapter)
        }
    }

}