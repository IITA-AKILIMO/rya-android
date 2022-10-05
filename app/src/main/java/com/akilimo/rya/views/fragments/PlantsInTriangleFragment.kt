package com.akilimo.rya.views.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akilimo.rya.R
import com.akilimo.rya.databinding.FragmentPlantingPeriodBinding
import com.akilimo.rya.databinding.FragmentPlantsInTriangleBinding


/**
 * A simple [Fragment] subclass.
 * Use the [PlantsInTriangleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlantsInTriangleFragment : BaseStepFragment() {
    private var ctx: Context? = null
    private var _binding: FragmentPlantsInTriangleBinding? = null


    private val binding get() = _binding!!

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment PlantsInTriangleFragment.
         */
        @JvmStatic
        fun newInstance() = PlantsInTriangleFragment()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return loadFragmentLayout(inflater, container, savedInstanceState)
    }

    override fun loadFragmentLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantsInTriangleBinding.inflate(inflater, container, false)
        return binding.root
    }

}
