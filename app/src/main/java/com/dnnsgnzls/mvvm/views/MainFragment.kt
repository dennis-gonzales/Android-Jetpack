package com.dnnsgnzls.mvvm.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dnnsgnzls.mvvm.databinding.FragmentMainBinding
import com.dnnsgnzls.mvvm.models.HeroRepository
import com.dnnsgnzls.mvvm.viewmodel.MainViewModel


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val heroAdapter = HeroAdapter(arrayListOf())

    // Prefer Dependency Injection - Dagger or Hilt
    class ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(HeroRepository()) as T
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the ViewModel with a new instance of Repository
        viewModel = ViewModelProvider(this, ViewModelFactory()).get(MainViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Create instance of FragmentMainBinding class
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        val view = binding.root

        initializeViews()
        observeViewModels()

        return view
    }

    private fun initializeViews() {
        binding.heroList.apply {
            adapter = heroAdapter
            setHasFixedSize(true)
        }

        binding.swiperefresh.setOnRefreshListener {
            binding.heroList.visibility = View.GONE
            binding.loadingHeroes.visibility = View.GONE
            binding.swiperefresh.isRefreshing = false

            viewModel.fetchFromRemote()
        }
    }

    private fun observeViewModels() {
        // Observe the LiveData from the ViewModel
        viewModel.heroList.observe(viewLifecycleOwner) { heroList ->
            // Update UI with the heroList
            binding.heroList.visibility = View.VISIBLE
            heroAdapter.updateList(heroList)

            Toast.makeText(activity, "Hero list retrieved from endpoint", Toast.LENGTH_SHORT).show()
        }

        // Loading indicator
        viewModel.loading.observe(viewLifecycleOwner) { loading ->
            binding.loadingHeroes.apply {
                visibility = if (loading) View.VISIBLE else View.GONE
            }
        }

        // Fetch data
        viewModel.fetchFromRemote()
    }
}