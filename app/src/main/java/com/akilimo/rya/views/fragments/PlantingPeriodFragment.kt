package com.akilimo.rya.views.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.Fragment
import com.akilimo.rya.databinding.FragmentPlantingPeriodBinding
import com.akilimo.rya.utils.DateHelper
import com.stepstone.stepper.VerificationError
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [PlantingPeriodFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlantingPeriodFragment : BaseStepFragment() {

    private var cal: Calendar = Calendar.getInstance()
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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return loadFragmentLayout(inflater, container, savedInstanceState)
    }


    override fun loadFragmentLayout(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantingPeriodBinding.inflate(inflater, container, false)
        return binding.root
    }

    // create an OnDateSetListener
    val dateSetListener = object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(
            view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int
        ) {
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "MM/yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.plantingYearMonth.text = sdf.format(cal.time)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val minDate = DateHelper.getMinDate(-16)
        val maxDate = DateHelper.getMinDate(0)

        binding.btnDatePicker.setOnClickListener { vw ->
            val dialog = DatePickerDialog(
                vw.context, dateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
            )

            dialog.datePicker.minDate = minDate.timeInMillis
            dialog.datePicker.maxDate = maxDate.timeInMillis
            dialog.show()


//            val dialog = DatePickerDialog(vw.context, { _, year, month, day_of_month ->
//                cal.get(Calendar.YEAR)
//                cal.get(Calendar.MONTH)
//                cal.get(Calendar.DAY_OF_MONTH)
//                val myFormat = "MM-yyyy"
//                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
//                binding.plantingYearMonth.text = sdf.format(cal.time)
//            }, cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH])
//            dialog.datePicker.minDate = minDate.timeInMillis
//            dialog.datePicker.maxDate = cal.timeInMillis
//            dialog.show()

        }


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
