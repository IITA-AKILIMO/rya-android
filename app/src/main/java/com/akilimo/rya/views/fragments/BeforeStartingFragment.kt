package com.akilimo.rya.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akilimo.rya.R
import com.akilimo.rya.databinding.FragmentBeforeStartingBinding
import com.akilimo.rya.databinding.FragmentPlantTrianglesBinding
import com.akilimo.rya.databinding.FragmentPlantingPeriodBinding
import com.stepstone.stepper.VerificationError

/**
 * A simple [Fragment] subclass.
 * Use the [BeforeStartingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BeforeStartingFragment : BaseStepFragment() {
    private var _binding: FragmentBeforeStartingBinding? = null

    private val binding get() = _binding!!

    companion object {
        @JvmStatic
        fun newInstance(): BeforeStartingFragment = BeforeStartingFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return loadFragmentLayout(inflater, container, savedInstanceState)
    }

    override fun loadFragmentLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBeforeStartingBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun verifyStep(): VerificationError? {
        return null
    }

    override fun onSelected() {
    }

    override fun onError(error: VerificationError) {

    }
}
