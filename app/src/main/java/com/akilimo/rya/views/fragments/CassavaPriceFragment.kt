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
import com.akilimo.rya.databinding.FragmentCassavaPriceBinding
import com.akilimo.rya.entities.FieldInfoEntity
import com.akilimo.rya.utils.StringToNumberFactory
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.stepstone.stepper.VerificationError


/**
 * A simple [Fragment] subclass.
 * Use the [CassavaPriceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CassavaPriceFragment : BaseStepFragment() {

    private var ctx: Context? = null
    private var _binding: FragmentCassavaPriceBinding? = null

    private var _currencyUnitIndex = 0
    private var _fieldSize: Double = 0.0
    private var _selectedCurrencyUnit: String? = null
    private var _sellingPrice: Double = 0.0
    private var _currency: String = "USD"
    private var _currencyName: String = "Dollars"

    private var database: AppDatabase? = null
    private var fieldInfoEntity: FieldInfoEntity? = null


    private val binding get() = _binding!!

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment [CassavaPriceFragment].
         */
        @JvmStatic
        fun newInstance() = CassavaPriceFragment()
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
        _binding = FragmentCassavaPriceBinding.inflate(inflater, container, false)
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
                    _selectedCurrencyUnit = currencyUnit
                    _currency = currency
                    _currencyName = currencyName
                    _currencyUnitIndex = currencyUnitIndex
                }
                currencyUnitPrompt.selectItemByIndex(fieldInfoEntity!!.currencyUnitIndex)
                txtSellingPrice.editText?.setText(_sellingPrice.toString())
            }


            currencyUnitPrompt.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { _, _, newIndex, newItem ->
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


        binding.currencyUnitPrompt.error = null


        var errMessage: String

        if (_selectedCurrencyUnit.isNullOrEmpty()) {
            errMessage = "Select proper selling unit"
            binding.currencyUnitPrompt.error = errMessage
        }


        if (_sellingPrice <= 0.0) {
            errMessage = "Provide a valid selling price"
            binding.txtSellingPrice.error = errMessage
        }


        if (fieldInfoEntity == null) {
            fieldInfoEntity = FieldInfoEntity(id = 1)
        }

        fieldInfoEntity?.fieldSize = _fieldSize
        fieldInfoEntity?.currencyUnit = _selectedCurrencyUnit!!
        fieldInfoEntity?.currency = _currency
        fieldInfoEntity?.currencyName = _currencyName
        fieldInfoEntity?.sellingPrice = _sellingPrice
        fieldInfoEntity?.currencyUnitIndex = _currencyUnitIndex


        database?.fieldInfoDao()?.insert(fieldInfoEntity = fieldInfoEntity!!)

        return null
    }

    override fun onError(error: VerificationError) {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
