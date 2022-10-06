package com.akilimo.rya.views.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

    private var _selectedFieldAreaUnit: String? = null
    private var _areaUnitIndex = 0
    private var _currencyUnitIndex = 0
    private var _fieldSize: Double = 0.0
    private var _selectedCurrencyUnit: String? = null
    private var _sellingPrice: Double = 0.0
    private var _currency: String = "USD"
    private var _currencyName: String = "Dollars"
    private var _areaUnit: String = "acre"

    private var database: AppDatabase? = null
    private var fieldInfoEntity: FieldInfoEntity? = null


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
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFieldInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fieldInfoEntity = database?.fieldInfoDao()?.findOne()
        with(binding) {
            if (fieldInfoEntity != null) {

                with(fieldInfoEntity!!) {
                    _sellingPrice = sellingPrice
                    _fieldSize = fieldSize
                    _selectedFieldAreaUnit = fieldAreaUnit
                    _areaUnit = areaUnit
                    _areaUnitIndex = areaUnitIndex
                    _selectedCurrencyUnit = currencyUnit
                    _currency = currency
                    _currencyName = currencyName
                    _currencyUnitIndex = currencyUnitIndex
                }
                areaUnitPrompt.selectItemByIndex(fieldInfoEntity!!.areaUnitIndex)
                currencyUnitPrompt.selectItemByIndex(fieldInfoEntity!!.currencyUnitIndex)
                txtFieldSize.editText?.setText(_fieldSize.toString())
                txtSellingPrice.editText?.setText(_sellingPrice.toString())
            } else {
                areaUnitPrompt.selectItemByIndex(0)
                currencyUnitPrompt.selectItemByIndex(0)
            }

            areaUnitPrompt.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
                _selectedFieldAreaUnit = newItem
                _areaUnitIndex = newIndex
                when {
                    newItem.equals("Acre", true) -> {
                        _areaUnit = "acre"
                    }
                    newItem.equals("Hectare", true) -> {
                        _areaUnit = "ha"
                    }
                    newItem.equals("Meter squared", true) -> {
                        _areaUnit = "m2"
                    }
                }
            })

            currencyUnitPrompt.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
                _selectedCurrencyUnit = newItem
                _currencyUnitIndex = newIndex
                when {
                    newItem.equals("USD/Tonne", true) -> {
                        _currency = "USD"
                        _currencyName = "US Dollars"
                    }
                    newItem.equals("NGN/Tonne", true) -> {
                        _currency = "NGN"
                        _currencyName = "Naira"
                    }
                    newItem.equals("TZS/Tonne", true) -> {
                        _currency = "TZS"
                        _currencyName = "Tanzanian Shillings"
                    }
                }

            })


            txtFieldSize.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    txtFieldSize.error = null
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //TODO "Will not be implemented"
                }

                override fun afterTextChanged(editable: Editable?) {
                    _fieldSize = StringToNumberFactory.stringToDouble(editable.toString())
                }

            })

            txtSellingPrice.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    txtSellingPrice.error = null
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //TODO "Will not be implemented"
                }

                override fun afterTextChanged(editable: Editable?) {
                    _sellingPrice = StringToNumberFactory.stringToDouble(editable.toString())
                }

            })
        }

    }

    override fun verifyStep(): VerificationError? {


        binding.areaUnitPrompt.error = null
        binding.currencyUnitPrompt.error = null


        var errMessage: String = ""
        if (_selectedFieldAreaUnit.isNullOrEmpty()) {
            errMessage = "Select proper field area unit"
            binding.areaUnitPrompt.error = errMessage
        }

        if (_selectedCurrencyUnit.isNullOrEmpty()) {
            errMessage = "Select proper selling unit"
            binding.currencyUnitPrompt.error = errMessage
        }

        if (_fieldSize <= 0.0) {
            errMessage = "Provide a valid field size"
            binding.txtFieldSize.error = errMessage
        }

        if (_sellingPrice <= 0.0) {
            errMessage = "Provide a valid selling price"
            binding.txtSellingPrice.error = errMessage
        }


        if (errMessage.isNotEmpty()) {
            val snackBar = Snackbar.make(
                binding.lytFieldSize, errMessage, Snackbar.LENGTH_SHORT
            )
            snackBar.setAction("OK") {
                snackBar.dismiss()
            }
            snackBar.show()
            return VerificationError(errMessage)
        }

        if (fieldInfoEntity == null) {
            fieldInfoEntity = FieldInfoEntity(id = 1)
        }

        fieldInfoEntity?.fieldAreaUnit = _selectedFieldAreaUnit!!
        fieldInfoEntity?.fieldSize = _fieldSize
        fieldInfoEntity?.areaUnit = _areaUnit
        fieldInfoEntity?.currencyUnit = _selectedCurrencyUnit!!
        fieldInfoEntity?.currency = _currency
        fieldInfoEntity?.currencyName = _currencyName
        fieldInfoEntity?.sellingPrice = _sellingPrice
        fieldInfoEntity?.currencyUnitIndex = _currencyUnitIndex
        fieldInfoEntity?.areaUnitIndex = _areaUnitIndex


        database?.fieldInfoDao()?.insert(fieldInfoEntity = fieldInfoEntity!!)

        return null
    }

    override fun onError(error: VerificationError) {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
