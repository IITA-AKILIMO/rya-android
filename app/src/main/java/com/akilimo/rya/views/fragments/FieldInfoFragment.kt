package com.akilimo.rya.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akilimo.rya.databinding.FragmentFieldInfoBinding
import com.akilimo.rya.utils.StringToNumberFactory
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.stepstone.stepper.VerificationError


/**
 * A simple [Fragment] subclass.
 * Use the [FieldInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FieldInfoFragment : BaseStepFragment() {

    private var ctx: Context? = null
    private var _binding: FragmentFieldInfoBinding? = null

    private var selectedFieldAreaUnit: String? = null
    private var fieldSize: Double = 0.0
    private var selectedSellingPriceUnit: String? = null
    private var sellingPrice: Double = 0.0

    private val binding get() = _binding!!

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment FieldInfoFragment.
         */
        @JvmStatic
        fun newInstance() = FieldInfoFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.ctx = context
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
        _binding = FragmentFieldInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fieldAreaUnitPrompt.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            selectedFieldAreaUnit = newItem
        })

        binding.sellingPriceUnitPrompt.setOnSpinnerItemSelectedListener(
            OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
                selectedSellingPriceUnit = newItem
            })
    }

    override fun verifyStep(): VerificationError? {
        var isDataValid: Boolean = true

        binding.txtFieldSize.error = null
        binding.fieldAreaUnitPrompt.error = null
        binding.txtSellingPrice.error = null
        binding.sellingPriceUnitPrompt.error = null

        fieldSize = StringToNumberFactory.stringToDouble(binding.txtFieldSize.text.toString())
        sellingPrice = StringToNumberFactory.stringToDouble(binding.txtSellingPrice.text.toString())

        if (selectedFieldAreaUnit.isNullOrEmpty()) {
            binding.fieldAreaUnitPrompt.error = "Select proper field area unit"
            isDataValid = false
        }

        if (fieldSize <= 0.0) {
            binding.txtFieldSize.error = "Provide a valid field size"
            isDataValid = false
        }

        if (sellingPrice <= 0.0) {
            binding.txtSellingPrice.error = "Provide a valid selling price"
            isDataValid = false
        }

        if (selectedSellingPriceUnit.isNullOrEmpty()) {
            binding.sellingPriceUnitPrompt.error = "Select proper selling unit"
            isDataValid = false
        }

        if (!isDataValid) {
            return VerificationError("Please fill all fields")
        }

        //save to ROOM database
        return null
    }

    override fun onSelected() {
    }

    override fun onError(error: VerificationError) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
