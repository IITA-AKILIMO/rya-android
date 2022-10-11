package com.akilimo.rya.views.fragments.ui

import android.content.Context
import android.text.InputType
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.setPadding
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.R
import com.akilimo.rya.entities.PlantTriangleEntity
import com.akilimo.rya.utils.StringToNumberFactory
import com.akilimo.rya.views.fragments.BaseStepFragment
import com.google.android.material.textfield.TextInputLayout
import com.stepstone.stepper.VerificationError

abstract class BasePlantTriangle : BaseStepFragment() {

    protected var triangleCount: Int = -1
    protected var triangleName: String? = null

    protected var database: AppDatabase? = null


    protected val inputLayouts: MutableList<TextInputLayout> = arrayListOf()

    protected fun addTextInputLayout(identifier: Int, context: Context): TextInputLayout {

        val layoutParams = LinearLayout.LayoutParams(
            /* width = */ ViewGroup.LayoutParams.MATCH_PARENT,
            /* height = */ ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 0, 0, resources.getDimension(R.dimen.dimen_124).toInt())

        val textInputLayout = TextInputLayout(
            context, null, R.attr.customTextInputStyle
        )
        textInputLayout.id = identifier
        textInputLayout.hint = getString(R.string.lbl_root_weight_hint)
        textInputLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
        //textInputLayout.layoutParams = layoutParams


        textInputLayout.addView(addEditText(textInputLayout.context))

        return textInputLayout
    }

    private fun addEditText(context: Context): AppCompatEditText {
        val layoutParams = LinearLayout.LayoutParams(
            /* width = */ ViewGroup.LayoutParams.MATCH_PARENT,
            /* height = */ ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(
            /* left = */ 0,
            /* top = */0,
            /* right = */0,
            /* bottom = */resources.getDimension(R.dimen.spacing_medium).toInt()
        )


        val editText = AppCompatEditText(context)
//        editText.width = ViewGroup.LayoutParams.MATCH_PARENT
//        editText.height = ViewGroup.LayoutParams.WRAP_CONTENT
        editText.layoutParams = layoutParams
        editText.setPadding(resources.getDimension(R.dimen.dimen_16).toInt())
        editText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
        return editText
    }

    override fun verifyStep(): VerificationError? {
        var inputValid = false
        val plantTrianglesMeasurement: MutableList<PlantTriangleEntity> = arrayListOf()
        var plantNumber = 1
        for (inputLayout in inputLayouts) {
            val rootWeightString = inputLayout.editText?.editableText.toString()
            val rootWeight = StringToNumberFactory.stringToDouble(rootWeightString)
            inputValid = rootWeight > 0
            if (inputValid) {
                //save this value to the database
                inputLayout.error = null
                plantTrianglesMeasurement.add(
                    PlantTriangleEntity(
                        triangleName = triangleName!!,
                        plantName = "plant$plantNumber",
                        rootWeight = rootWeight
                    )
                )
                plantNumber++
            } else {
                inputLayout.error = "Provide correct plant root weight"
                inputLayout.requestFocus()
                break //no need to loop all through
            }
        }

        if (!inputValid) {
            return VerificationError("Provide correct plant root weight for all inputs")
        }

        database?.plantTriangleDao()?.insertAll(plantTrianglesMeasurement)
        return verificationError
    }


    @Deprecated("To be removed completely", ReplaceWith("false"))
    fun validateInput(): Boolean {
        return false
    }

}
