package com.dnnsgnzls.mvvm.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    fun initializeViews() {
        // Recyclerview fixed size for optimization
        binding.list.setHasFixedSize(true)
    }

    fun observeViewModels() {
        // Observe the LiveData from the ViewModel
        viewModel.hero.observe(viewLifecycleOwner) { data ->
            // Update UI with the data
            binding.list.apply { adapter = HeroAdapter(data) }
        }

        // Fetch data
        viewModel.fetchData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchData()
    }
}