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
import com.akilimo.rya.databinding.FragmentPlantsInTriangleBinding
import com.akilimo.rya.entities.FieldInfoEntity
import com.akilimo.rya.utils.StringToNumberFactory
import com.google.android.material.textfield.TextInputLayout
import com.stepstone.stepper.VerificationError


/**
 * A simple [Fragment] subclass.
 * Use the [PlantsInTriangleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlantsInTriangleFragment : BaseStepFragment() {
    private var ctx: Context? = null
    private var _binding: FragmentPlantsInTriangleBinding? = null


    private val binding get() = _binding!!

    private var database: AppDatabase? = null

    private var triangle1PlantCount: Int = 0
    private var triangle2PlantCount: Int = 0
    private var triangle3PlantCount: Int = 0
    private var hasError = false
    private var fieldInfoEntity: FieldInfoEntity? = null

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


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.ctx = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(ctx!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fieldInfoEntity = database?.fieldInfoDao()?.findOne()

        with(binding) {

            if (fieldInfoEntity != null) {
                txtPlantCountTri1.editText?.setText(fieldInfoEntity?.triangle1PlantCount.toString())
                txtPlantCountTri2.editText?.setText(fieldInfoEntity?.triangle2PlantCount.toString())
                txtPlantCountTri3.editText?.setText(fieldInfoEntity?.triangle3PlantCount.toString())
            }
            txtPlantCountTri1.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    txtPlantCountTri1.error = null
                }

                override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //this will not be implemented
                }

                override fun afterTextChanged(editable: Editable?) {
                    triangle1PlantCount = extractNumberValue(editable)
                    validateInput(triangle1PlantCount, txtPlantCountTri1)
                }
            })

            txtPlantCountTri2.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    txtPlantCountTri2.error = null
                }

                override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //this will not be implemented
                }

                override fun afterTextChanged(editable: Editable?) {
                    triangle2PlantCount = extractNumberValue(editable)
                    validateInput(triangle2PlantCount, txtPlantCountTri2)
                }
            })

            txtPlantCountTri3.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence?, p1: Int, p2: Int, p3: Int
                ) {
                    txtPlantCountTri3.error = null
                }

                override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //this will not be implemented
                }

                override fun afterTextChanged(editable: Editable?) {
                    triangle3PlantCount = extractNumberValue(editable)
                    validateInput(triangle3PlantCount, txtPlantCountTri3)
                }
            })
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return loadFragmentLayout(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun verifyStep(): VerificationError? {
        if (hasError) {
            return VerificationError("Please enter values greater than 0")
        }

        //save the values now
        if (fieldInfoEntity == null) {
            fieldInfoEntity = FieldInfoEntity(id = 1)
        }

        fieldInfoEntity?.triangle1PlantCount = triangle1PlantCount
        fieldInfoEntity?.triangle2PlantCount = triangle2PlantCount
        fieldInfoEntity?.triangle3PlantCount = triangle3PlantCount

        database?.fieldInfoDao()?.insert(fieldInfoEntity!!)

        return verificationError
    }

    override fun loadFragmentLayout(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantsInTriangleBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun extractNumberValue(editable: Editable?): Int {
        return StringToNumberFactory.stringToInt(editable.toString())
    }

    private fun validateInput(intValue: Int, inputLayout: TextInputLayout) {
        if (intValue <= 0) {
            hasError = true
            inputLayout.error = "Enter a value greater than 0"
            return
        }
        hasError = false
    }

}
