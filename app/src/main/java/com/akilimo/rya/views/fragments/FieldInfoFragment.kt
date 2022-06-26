package com.akilimo.rya.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.databinding.FragmentFieldInfoBinding
import com.akilimo.rya.entities.FieldInfoEntity
import com.akilimo.rya.utils.StringToNumberFactory
import com.google.android.material.snackbar.Snackbar
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
    private var currency: String = "USD"
    private var currencyName: String = "Dollars"
    private var areaUnit: String = "acre"

    private var database: AppDatabase? = null


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(ctx!!)
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
        binding.fieldAreaUnitPrompt.setOnSpinnerItemSelectedListener(
            OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
                selectedFieldAreaUnit = newItem
                when {
                    newItem.equals("Acre", true) -> {
                        areaUnit = "acre"
                    }
                    newItem.equals("Hectare", true) -> {
                        areaUnit = "ha"
                    }
                    newItem.equals("Meter squared", true) -> {
                        areaUnit = "m2"
                    }
                }
            })

        binding.sellingPriceUnitPrompt.setOnSpinnerItemSelectedListener(
            OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
                selectedSellingPriceUnit = newItem
                when {
                    newItem.equals("USD/Tonne", true) -> {
                        currency = "USD"
                        currencyName = "US Dollars"
                    }
                    newItem.equals("NGN/Tonne", true) -> {
                        currency = "NGN"
                        currencyName = "Naira"
                    }
                    newItem.equals("TZS/Tonne", true) -> {
                        currency = "TZS"
                        currencyName = "Tanzanian Shillings"
                    }
                }

            })

        binding.fieldAreaUnitPrompt.selectItemByIndex(0)
        binding.sellingPriceUnitPrompt.selectItemByIndex(0)
    }

    override fun verifyStep(): VerificationError? {
        var isDataValid: Boolean = true

        binding.txtFieldSize.error = null
        binding.fieldAreaUnitPrompt.error = null
        binding.txtSellingPrice.error = null
        binding.sellingPriceUnitPrompt.error = null

        fieldSize = StringToNumberFactory.stringToDouble(binding.txtFieldSize.text.toString())
        sellingPrice = StringToNumberFactory.stringToDouble(binding.txtSellingPrice.text.toString())

        var errMessage:String = ""
        if (selectedFieldAreaUnit.isNullOrEmpty()) {
            errMessage = "Select proper field area unit"
            binding.fieldAreaUnitPrompt.error = errMessage
            isDataValid = false
        }

        if (fieldSize <= 0.0) {
            errMessage = "Provide a valid field size"
            binding.txtFieldSize.error = errMessage
            isDataValid = false
        }

        if (sellingPrice <= 0.0) {
            errMessage = "Provide a valid selling price"
            binding.txtSellingPrice.error = errMessage
            isDataValid = false
        }

        if (selectedSellingPriceUnit.isNullOrEmpty()) {
           errMessage = "Select proper selling unit"
            binding.sellingPriceUnitPrompt.error = errMessage
            isDataValid = false
        }

        if (!isDataValid) {
            val snackBar = Snackbar.make(binding.lytFieldSize,errMessage,
                Snackbar.LENGTH_SHORT)

            snackBar.setAction("OK") {
                snackBar.dismiss()
            }
            snackBar.show()
            return VerificationError("Please fill all fields")
        }

        //Save to ROOM database
        val fieldInfo = FieldInfoEntity(
            id = 1,
            fieldAreaUnit = selectedFieldAreaUnit!!,
            fieldSize = fieldSize,
            areaUnit = areaUnit,
            sellingPriceUnit = selectedSellingPriceUnit!!,
            currency = currency,
            currencyName = currencyName,
            sellingPrice = sellingPrice
        )

        val data = database?.fieldInfoDao()?.findOne()
        if (data != null) {
            database?.fieldInfoDao()?.deleteField(data)
        }
        database?.fieldInfoDao()?.insert(fieldInfoEntity = fieldInfo)

        return null
    }

    override fun onSelected() {}

    override fun onError(error: VerificationError) {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
