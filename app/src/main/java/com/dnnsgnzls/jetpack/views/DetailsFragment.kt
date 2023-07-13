package com.dnnsgnzls.jetpack.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.dnnsgnzls.jetpack.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {
    private var heroId: Int? = null

    private var _binding: FragmentDetailsBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args: DetailsFragmentArgs by navArgs()
        heroId = args.heroId
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        initializeViews()

        return view
    }

    private fun initializeViews() {
        binding.testTextView.text = "${heroId ?: -1}"
    }
}