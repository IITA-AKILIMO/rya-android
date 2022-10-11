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

    private val binding get() = _binding!!

    companion object {
        /**
         * @param triangleCount Parameter 1.
         * @return A new instance of fragment TriangleFragment.
         */
        @JvmStatic
        fun newInstance(triangleCount: Int, triangleName: String) =
            TriangleTwoFragment().apply {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
}
