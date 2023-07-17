package com.dnnsgnzls.jetpack.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.dnnsgnzls.jetpack.R
import com.dnnsgnzls.jetpack.databinding.FragmentHeroBinding
import com.dnnsgnzls.jetpack.models.Hero
import com.dnnsgnzls.jetpack.models.HeroRepository
import com.dnnsgnzls.jetpack.models.IHeroClick
import com.dnnsgnzls.jetpack.util.Notifs
import com.dnnsgnzls.jetpack.viewmodel.HeroViewModel
import com.dnnsgnzls.jetpack.viewmodel.HeroViewModelFactory
import com.dnnsgnzls.jetpack.views.adapter.HeroAdapter


class HeroFragment : Fragment(), IHeroClick, MenuProvider {
    private val viewModel by viewModels<HeroViewModel> {
        HeroViewModelFactory(requireActivity().application, HeroRepository())
    }

    private val heroAdapter: HeroAdapter by lazy {
        HeroAdapter(arrayListOf(), this, viewModel)
    }

    private lateinit var navController: NavController

    private var _binding: FragmentHeroBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Create instance of `FragmentMainBinding` class - `fragment_hero.xml` layout
        _binding = FragmentHeroBinding.inflate(inflater, container, false)
        val view = binding.root

        navController = findNavController()

        initializeViews()
        observeViewModels()

        return view
    }

    private fun initializeViews() {
        // Add Menu Provider
        requireActivity().addMenuProvider(this, viewLifecycleOwner)

        binding.errorText.isSelected = true

        binding.heroList.apply {
            adapter = heroAdapter
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

        viewModel.error.observe(viewLifecycleOwner) { error ->
            binding.errorText.apply {
                text = error
                visibility = if (error.isNullOrEmpty()) View.GONE else View.VISIBLE
            }
        }

        // Fetch data
        viewModel.refresh()
    }

    override fun onClick(view: View, hero: Hero) {
        val action = HeroFragmentDirections.actionHeroFragmentToDetailsFragment(hero.id)
        navController.navigate(action)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.preferences_menu -> navController.navigate(
                HeroFragmentDirections
                    .actionHeroFragmentToSettingsFragment()
            )

            R.id.notification_menu -> Notifs(requireActivity()).createNofitication()
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}