package com.dnnsgnzls.jetpack.views

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.dnnsgnzls.jetpack.R
import com.dnnsgnzls.jetpack.databinding.FragmentDetailsBinding
import com.dnnsgnzls.jetpack.models.Hero
import com.dnnsgnzls.jetpack.models.HeroPalette
import com.dnnsgnzls.jetpack.models.HeroRepository
import com.dnnsgnzls.jetpack.viewmodel.DetailsViewModel

class DetailsFragment : Fragment(), MenuProvider {
    private lateinit var viewModel: DetailsViewModel
    private lateinit var navController: NavController

    private var _binding: FragmentDetailsBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var hero: Hero

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args: DetailsFragmentArgs by navArgs()
        val heroId = args.heroId

        // Prefer Dependency Injection - Dagger or Hilt
        class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DetailsViewModel(activity!!.application, HeroRepository()) as T
            }
        }

        // Initialize the ViewModel with a new instance of Repository
        viewModel = ViewModelProvider(this, ViewModelFactory()).get(DetailsViewModel::class.java)

        // Get the hero
        viewModel.getHero(heroId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        initializeViews()
        observeVieModels()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
    }

    private fun initializeViews() {
        // Add Menu Provider
        requireActivity().addMenuProvider(this, viewLifecycleOwner)

        // TODO: Check if hero is not initialized and handle gracefully
    }

    private fun observeVieModels() {
        viewModel.heroDetails.observe(viewLifecycleOwner) { heroDetails ->
            hero = heroDetails
            binding.hero = heroDetails
            setupBackgroundColor(heroDetails.fullImageUrl)
        }
    }

    private fun setupBackgroundColor(url: String) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {
                }

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    Palette.from(resource)
                        .generate { palette ->
                            val intColor = palette?.darkMutedSwatch?.rgb ?: 0
                            val myPalette = HeroPalette(intColor)
                            binding.palette = myPalette
                        }
                }

            })
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.details_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.action_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "Hey let's play some dota!")
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "i'll play the hero *${hero.localizedName}*, I will play the roles *${hero.printableRoles}*"
                )
                intent.putExtra(Intent.EXTRA_STREAM, hero.fullImageUrl)
                startActivity(Intent.createChooser(intent, "Share with"))
            }
        }

        return true
    }
}