package com.akilimo.rya.views.fragments.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.marginTop
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.R
import com.akilimo.rya.databinding.FragmentTriangleBinding
import com.akilimo.rya.entities.PlantTriangleEntity
import com.akilimo.rya.utils.StringToNumberFactory
import com.akilimo.rya.views.fragments.BaseStepFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.stepstone.stepper.VerificationError


private const val PLANT_COUNT = "plant_count"
private const val TRIANGLE_NAME = "triangle_name"

/**
 * A simple [Fragment] subclass.
 * Use the [TriangleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TriangleFragment : BaseStepFragment() {
    var triangleCount: Int = -1
    var triangleName: String? = null

    private var _binding: FragmentTriangleBinding? = null
    private var ctx: Context? = null

    private var database: AppDatabase? = null
    private val binding get() = _binding!!

    private val inputLayouts: MutableList<TextInputLayout> = arrayListOf()

    companion object {
        /**
         * @param plantCount Parameter 1.
         * @return A new instance of fragment TriangleFragment.
         */
        @JvmStatic
        fun newInstance(plantCount: Int, triangleName: String) = TriangleFragment().apply {
            arguments = Bundle().apply {
                putInt(PLANT_COUNT, plantCount)
                putString(TRIANGLE_NAME, triangleName)
            }
        }

        @JvmStatic
        fun newInstance() = TriangleFragment().apply {}
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.ctx = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            triangleCount = it.getInt(PLANT_COUNT)
            triangleName = it.getString(TRIANGLE_NAME)
        }
        database = AppDatabase.getDatabase(ctx!!)
    }

    override fun loadFragmentLayout(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTriangleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onSelected() {
        super.onSelected()
        val lyt = binding.lytTextField
        lyt.removeAllViews() //clear all components
        for (i in 0 until triangleCount) {
            val textInputLayout = addTextInputLayout(i, requireView().context)
            lyt.addView(textInputLayout)
            inputLayouts.add(textInputLayout)
        }

        //get saved values and add them to text fields
        var plantNumber = 1
        for (inputLayout in inputLayouts) {
            val plantTriangle = database?.plantTriangleDao()
                ?.findOneByTriangleNameAndPlantName(triangleName!!, "plant$plantNumber")
            if (plantTriangle != null) {
                inputLayout.editText?.setText(plantTriangle.rootWeight.toString())
                plantNumber++
            }
        }
    }


    private fun addTextInputLayout(identifier: Int, context: Context): TextInputLayout {

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    override fun onError(error: VerificationError) {
        val snackBar = Snackbar.make(
            binding.constraintLayout, error.errorMessage, Snackbar.LENGTH_SHORT
        )

        snackBar.setAction("RETRY") {
            snackBar.dismiss()
        }
        snackBar.show()
    }

    @Deprecated("To be removed")
    fun validateInput(): Boolean {
        return false
    }
}
