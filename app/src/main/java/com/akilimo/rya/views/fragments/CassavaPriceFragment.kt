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
import com.akilimo.rya.R
import com.akilimo.rya.databinding.FragmentCassavaPriceBinding
import com.akilimo.rya.entities.FieldInfoEntity
import com.akilimo.rya.entities.UserInfoEntity
import com.akilimo.rya.utils.StringToNumberFactory
import com.stepstone.stepper.VerificationError


/**
 * A simple [Fragment] subclass.
 * Use the [CassavaPriceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CassavaPriceFragment : BaseStepFragment() {

    private var ctx: Context? = null
    private var _binding: FragmentCassavaPriceBinding? = null

    private var _fieldSize: Double = 0.0
    private var _sellingPrice: Double = 0.0
    private var _currency: String = "USD"
    private var _currencyName: String = "Dollars"

    private var database: AppDatabase? = null
    private var fieldInfoEntity: FieldInfoEntity? = null
    private var userInfoEntity: UserInfoEntity? = null


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
        userInfoEntity = database?.userInfoDao()?.findOne()
        with(binding) {
            if (fieldInfoEntity != null) {

                with(fieldInfoEntity!!) {
                    _sellingPrice = sellingPrice
                    _fieldSize = fieldSize
                }

                with(userInfoEntity) {
                    _currency = this?.currencyCode ?: "USD"
                    _currencyName = this?.currencyName ?: "Dollars"
                }

                val hintText =
                    activity?.getString(R.string.lbl_cassava_selling_price_hint, _currencyName)
                txtSellingPrice.hint = hintText

                txtSellingPrice.editText?.setText(_sellingPrice.toString())
            }




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

        if (_sellingPrice <= 0.0) {
            val errMessage = "Provide a valid selling price"
            binding.txtSellingPrice.error = errMessage
        }


        if (fieldInfoEntity == null) {
            fieldInfoEntity = FieldInfoEntity(id = 1)
        }

        fieldInfoEntity?.currency = _currency
        fieldInfoEntity?.currencyName = _currencyName
        fieldInfoEntity?.sellingPrice = _sellingPrice


        database?.fieldInfoDao()?.insert(fieldInfoEntity = fieldInfoEntity!!)

        return null
    }

    override fun onError(error: VerificationError) {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
