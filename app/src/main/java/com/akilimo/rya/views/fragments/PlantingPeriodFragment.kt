package com.akilimo.rya.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akilimo.rya.databinding.FragmentPlantingPeriodBinding
import com.stepstone.stepper.VerificationError
import java.text.DateFormatSymbols
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [PlantingPeriodFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlantingPeriodFragment : BaseStepFragment() {

    var calendar = Calendar.getInstance()
    private var ctx: Context? = null
    private var _binding: FragmentPlantingPeriodBinding? = null

    var months: Array<String> = DateFormatSymbols().months
    var yearsArray: List<String>? = null

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        yearsArray = getYearRange(calendar.get(Calendar.YEAR) - 5, calendar.get(Calendar.YEAR))

        binding.plantingMonthPrompt.setItems(months.toList())
        binding.plantingYearPrompt.setItems(yearsArray!!)
    }

    override fun verifyStep(): VerificationError? {
        return null
    }

    override fun onSelected() {
    }

    override fun onError(error: VerificationError) {
    }

    private fun getYearRange(startDate: Int, endDate: Int): MutableList<String> {
        val list: MutableList<String> = ArrayList()
        for (i in startDate..endDate) {
            list.add(i.toString())
        }

        list.reverse()
        return list
    }
}
