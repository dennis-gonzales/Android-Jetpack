package com.dnnsgnzls.jetpack.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.dnnsgnzls.jetpack.R
import com.dnnsgnzls.jetpack.databinding.FragmentHeroBinding
import com.dnnsgnzls.jetpack.models.Hero
import com.dnnsgnzls.jetpack.models.HeroRepository
import com.dnnsgnzls.jetpack.models.IHeroClick
import com.dnnsgnzls.jetpack.viewmodel.MainViewModel
import com.dnnsgnzls.jetpack.views.adapter.HeroAdapter


class HeroFragment : Fragment(), IHeroClick {
    private lateinit var viewModel: MainViewModel
    private lateinit var heroAdapter: HeroAdapter

    private var _binding: FragmentHeroBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Prefer Dependency Injection - Dagger or Hilt
        class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(activity!!.application, HeroRepository()) as T
            }
        }

        // Initialize the ViewModel with a new instance of Repository
        viewModel = ViewModelProvider(this, ViewModelFactory()).get(MainViewModel::class.java)

        // Instantiate HeroAdapter with the viewModel's scope
        heroAdapter = HeroAdapter(arrayListOf(), this, viewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Create instance of `FragmentMainBinding` class - `fragment_hero.xml` layout
        _binding = FragmentHeroBinding.inflate(inflater, container, false)
        val view = binding.root

        initializeViews()
        observeViewModels()

        return view
    }

    private fun initializeViews() {
        val anim = AnimationUtils.loadAnimation(context, R.anim.item_animation_fall_down)

        binding.heroList.apply {
            adapter = heroAdapter
            animation = anim
            setHasFixedSize(true)
        }

        binding.swiperefresh.setOnRefreshListener {
            binding.heroList.visibility = View.GONE
            binding.loadingHeroes.visibility = View.GONE
            binding.swiperefresh.isRefreshing = false

            // hard refresh - fetch from api
            viewModel.refresh(hardRefresh = true)
        }
    }

    private fun observeViewModels() {
        // Observe the LiveData from the ViewModel
        viewModel.heroList.observe(viewLifecycleOwner) { heroList ->
            // Update UI with the heroList
            binding.heroList.visibility = View.VISIBLE
            heroAdapter.updateList(heroList)
        }

        // Loading indicator
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.loadingHeroes.apply {
                visibility = if (loading) View.VISIBLE else View.GONE
            }
        }

        // Fetch data
        viewModel.refresh()
    }

    override fun onClick(view: View, hero: Hero) {
        val action = HeroFragmentDirections.actionHeroFragmentToDetailsFragment(hero.id)
        findNavController().navigate(action)
    }
}