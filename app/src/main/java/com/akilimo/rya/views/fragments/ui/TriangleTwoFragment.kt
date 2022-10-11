package com.akilimo.rya.views.fragments.ui

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.R
import com.akilimo.rya.databinding.FragmentTriangleBinding
import com.akilimo.rya.entities.PlantTriangleEntity
import com.akilimo.rya.utils.StringToNumberFactory
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
class TriangleTwoFragment : BasePlantTriangle() {

    private var _binding: FragmentTriangleBinding? = null
    private var ctx: Context? = null


    private val inputLayouts: MutableList<TextInputLayout> = arrayListOf()

    private val binding get() = _binding!!

    companion object {
        /**
         * @param triangleCount Parameter 1.
         * @return A new instance of fragment TriangleFragment.
         */
        @JvmStatic
        fun newInstance(triangleCount: Int, triangleName: String) = TriangleTwoFragment().apply {
            this.triangleName = triangleName
            this.triangleCount = triangleCount
        }

        @JvmStatic
        fun newInstance() = TriangleTwoFragment().apply { }
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
        val lyt = binding.lytTextField
        lyt.removeAllViews() //clear all components
        inputLayouts.clear()
        for (i in 0 until triangleCount) {
            val textInputLayout = addTextInputLayout(i, requireView().context)
            lyt.addView(textInputLayout)
            inputLayouts.add(textInputLayout)
        }

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
}
