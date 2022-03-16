package com.akilimo.rya.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akilimo.rya.R


/**
 * A simple [Fragment] subclass.
 * Use the [PlantingPeriodFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlantingPeriodFragment : Fragment() {


    companion object {
        @JvmStatic
        fun newInstance() = PlantingPeriodFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_planting_period, container, false)
    }


}
