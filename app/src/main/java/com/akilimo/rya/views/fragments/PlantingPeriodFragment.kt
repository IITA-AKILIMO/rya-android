package com.akilimo.rya.views.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akilimo.rya.R
import com.akilimo.rya.databinding.FragmentFieldInfoBinding
import com.akilimo.rya.databinding.FragmentPlantingPeriodBinding
import com.stepstone.stepper.VerificationError


/**
 * A simple [Fragment] subclass.
 * Use the [PlantingPeriodFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlantingPeriodFragment : BaseStepFragment() {

    private var ctx: Context? = null
    private var _binding: FragmentPlantingPeriodBinding? = null

    private val binding get() = _binding!!

    companion object {
        @JvmStatic
        fun newInstance() = PlantingPeriodFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.ctx = context
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return loadFragmentLayout(inflater, container, savedInstanceState)
    }


    override fun loadFragmentLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantingPeriodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun verifyStep(): VerificationError? {
        return null
    }

    override fun onSelected() {
    }

    override fun onError(error: VerificationError) {
    }
}
