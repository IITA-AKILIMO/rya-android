package com.akilimo.rya.views.fragments.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.R
import com.akilimo.rya.databinding.FragmentTriangleBinding
import com.akilimo.rya.entities.PlantTriangleEntity
import com.akilimo.rya.utils.StringToNumberFactory
import com.google.android.material.textfield.TextInputLayout
import com.stepstone.stepper.VerificationError
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 * Use the [TriangleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class TriangleFragment : BasePlantTriangle() {

    protected var _binding: FragmentTriangleBinding? = null
    protected var ctx: Context? = null


    protected val inputLayouts: MutableList<TextInputLayout> = arrayListOf()

    private val binding get() = _binding!!

    companion object {
        /**
         * @param triangleName Parameter 1.
         * @return A new instance of fragment TriangleFragment.
         */
        @JvmStatic
        fun newInstance(triangleName: String = "One") =
            TriangleFragment().apply {
                this.triangleName = triangleName
            }
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
        _binding = FragmentTriangleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buildDynamicWidgets()
    }

    private fun buildDynamicWidgets() {
        try {
            with(binding) {
                if (lytTextField.childCount > 0) {
                    lytTextField.removeAllViews() //clear all components
                }
                inputLayouts.clear()

                val fieldInfoEntity = database?.fieldInfoDao()?.findOne()
                if (fieldInfoEntity != null) {
                    plantCount = fieldInfoEntity.triangle1PlantCount
                }

                for (i in 0 until plantCount) {
                    val textInputLayout = addTextInputLayout(i, requireView().context)
                    lytTextField.addView(textInputLayout)
                    inputLayouts.add(textInputLayout)
                }
                lblTriangleNumber.text = resources.getString(R.string.lbl_triangle_one)
                lblTrianglePlantCount.text = "$plantCount plants"
            }

            loadTriangleData()
        } catch (ex: Exception) {
            //TODO add sentry logging
        }
    }

    override fun onSelected() {
        super.onSelected()
        buildDynamicWidgets()
    }

    override fun loadTriangleData() {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun verifyStep(): VerificationError? {
        if (inputLayouts.isEmpty()) {
            return myVerificationError("No plant root weights have been provided")
        }

        val plantTrianglesMeasurement: MutableList<PlantTriangleEntity> = arrayListOf()
        var plantNumber = 1
        for (inputLayout in inputLayouts) {
            val rootWeightString = inputLayout.editText?.editableText.toString()
            val rootWeight = StringToNumberFactory.stringToDouble(rootWeightString)
            if (rootWeightValid(rootWeight)) {
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
                inputLayout.error = "Provide correct plant root weight between 0.1KG and 20KG"
                inputLayout.requestFocus()
                return myVerificationError()
            }
        }

        database?.plantTriangleDao()?.insertAll(plantTrianglesMeasurement)
        return verificationError
    }
}
