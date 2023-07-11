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
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.stepstone.stepper.VerificationError
import java.lang.Exception


private const val PLANT_COUNT = "plant_count"
private const val TRIANGLE_NAME = "triangle_name"

/**
 * A simple [Fragment] subclass.
 * Use the [TriangleThreeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TriangleThreeFragment : TriangleFragment() {

    private val binding get() = _binding!!

    companion object {
        /**
         * @param triangleName Parameter 1.
         * @return A new instance of fragment TriangleFragment.
         */
        @JvmStatic
        fun newInstance(triangleName: String = "Three") =
            TriangleThreeFragment().apply {
                this.triangleName = triangleName
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buildDynamicWidgets()
    }

    override fun onSelected() {
        super.onSelected()
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
                    plantCount = fieldInfoEntity.triangle3PlantCount
                }

                for (i in 0 until plantCount) {
                    val textInputLayout = addTextInputLayout(i, requireView().context)
                    lytTextField.addView(textInputLayout)
                    inputLayouts.add(textInputLayout)
                }
                lblTriangleNumber.text = resources.getString(R.string.lbl_triangle_three)
                lblTrianglePlantCount.text = "$plantCount plants"
            }

            loadTriangleData()
        } catch (ex: Exception) {
            //TODO add sentry logging
        }
    }
}
