package com.dnnsgnzls.jetpack.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dnnsgnzls.jetpack.common.constants
import com.dnnsgnzls.jetpack.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment() {
    private var heroId: Int? = null

    private var _binding: FragmentDetailsBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            heroId = it.getInt(constants.HERO_ID)
        }
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
        binding.testTextView.text = "${heroId?: -1}"
    }
}