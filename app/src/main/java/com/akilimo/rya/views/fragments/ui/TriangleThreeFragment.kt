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
import com.akilimo.rya.views.fragments.BaseFragment
import com.akilimo.rya.views.fragments.BaseStepFragment
import com.google.android.material.snackbar.Snackbar
import com.stepstone.stepper.VerificationError


private const val PLANT_COUNT = "plant_count"
private const val TRIANGLE_NAME = "triangle_name"

/**
 * A simple [Fragment] subclass.
 * Use the [TriangleThreeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TriangleThreeFragment : BaseStepFragment() {
    var triangleCount: Int = 0
    var triangleName: String? = null

    private var _binding: FragmentTriangleBinding? = null
    private var ctx: Context? = null

    private var database: AppDatabase? = null
    private val binding get() = _binding!!

    private val editTexts: MutableList<AppCompatEditText> = arrayListOf()

    companion object {
        @JvmStatic
        fun newInstance() = TriangleThreeFragment().apply { }

        /**
         * @param plantCount Parameter 1.
         * @return A new instance of fragment TriangleFragment.
         */
        @JvmStatic
        fun newInstance(plantCount: Int, triangleName: String) =
            TriangleThreeFragment().apply {
                arguments = Bundle().apply {
                    putInt(PLANT_COUNT, plantCount)
                    putString(TRIANGLE_NAME, triangleName)
                }
            }
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTriangleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onSelected() {
        super.onSelected()
        //let us build the inputs
        val lyt = binding.lytTextField
        lyt.removeAllViews() //clear all components
        for (i in 0 until triangleCount) {
            val editText = AppCompatEditText(requireView().context)
            editText.id = i
            editText.width = ViewGroup.LayoutParams.MATCH_PARENT
            editText.height = ViewGroup.LayoutParams.WRAP_CONTENT
            editText.minHeight = resources.getDimension(R.dimen.dimen_48).toInt()
            editText.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER

            editText.hint = getString(R.string.lbl_root_weight_hint)
            editTexts.add(editText)
            lyt.addView(editText)
        }

        //get saved values and add them to text fields
        var plantNumber = 1
        for (inputField in editTexts) {
            val plantTriangle = database?.plantTriangleDao()
                ?.findOneByTriangleNameAndPlantName(triangleName!!, "plant$plantNumber")
            if (plantTriangle != null) {
                inputField.setText(plantTriangle.rootWeight.toString())
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
        for (inputField in editTexts) {
            val rootWeightString = inputField.editableText.toString()
            val rootWeight = StringToNumberFactory.stringToDouble(rootWeightString)
            inputValid = rootWeight > 0
            if (inputValid) {
                //save this value to the database
                inputField.error = null
                plantTrianglesMeasurement.add(
                    PlantTriangleEntity(
                        triangleName = triangleName!!,
                        plantName = "plant$plantNumber",
                        rootWeight = rootWeight
                    )
                )
                plantNumber++
            } else {
                inputField.error = "Provide correct plant root weight"
                inputField.requestFocus()
                break //no need to loop all through
            }
        }

        if (!inputValid) {
            return VerificationError("Provide correct plant root weight")
        }

        database?.plantTriangleDao()?.insertAll(plantTrianglesMeasurement)
        return verificationError
    }

    override fun onError(error: VerificationError) {
        val snackBar = Snackbar.make(
            binding.constraintLayout, error.errorMessage,
            Snackbar.LENGTH_SHORT
        )

        snackBar.setAction("RETRY") {
            snackBar.dismiss()
        }
        snackBar.show()
    }

    @Deprecated("To be removd")
    fun validateInput(): Boolean {
        return false
    }
}
