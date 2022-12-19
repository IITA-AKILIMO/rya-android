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
import com.akilimo.rya.databinding.FragmentFieldInfoBinding
import com.akilimo.rya.entities.FieldInfoEntity
import com.akilimo.rya.entities.UserInfoEntity
import com.akilimo.rya.utils.StringToNumberFactory
import com.google.android.material.snackbar.Snackbar
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
    private var _fieldSize: Double = 0.0
    private var _areaUnit: String = "acre"

    private var database: AppDatabase? = null
    private var fieldInfoEntity: FieldInfoEntity? = null
    private var userInfoEntity: UserInfoEntity? = null


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
        userInfoEntity = database?.userInfoDao()?.findOne()
        with(binding) {
            if (fieldInfoEntity != null) {
                with(fieldInfoEntity!!) {
                    _fieldSize = fieldSize
                }
                txtFieldSize.editText?.setText(_fieldSize.toString())
            }

            with(userInfoEntity) {
                _areaUnit = this?.areaUnit ?: "ha"
            }

            val labelText = activity?.getString(R.string.lbl_field_area, _areaUnit)
            val hintText = activity?.getString(R.string.lbl_field_area_hint, _areaUnit)
            txtFieldSize.hint = hintText
            lblFieldAreaUnit.text = labelText

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
        }

    }

    override fun verifyStep(): VerificationError? {

        var errMessage = ""


        if (_fieldSize <= 0.0) {
            errMessage = "Provide a valid field size"
            binding.txtFieldSize.error = errMessage
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

        fieldInfoEntity?.fieldSize = _fieldSize
        fieldInfoEntity?.areaUnit = _areaUnit


        database?.fieldInfoDao()?.insert(fieldInfoEntity = fieldInfoEntity!!)

        return null
    }

    override fun onError(error: VerificationError) {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
