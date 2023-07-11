package com.akilimo.rya.views.fragments.triangle

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.databinding.FragmentPlantCountBinding
import com.akilimo.rya.entities.FieldInfoEntity
import com.akilimo.rya.utils.StringToNumberFactory
import com.akilimo.rya.views.fragments.BaseStepFragment
import com.stepstone.stepper.VerificationError

/**
 * A simple [Fragment] subclass.
 * Use the [TriangleTwoPlantCountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TriangleTwoPlantCountFragment : TriangleOnePlantCountFragment() {

    companion object {
        @JvmStatic
        fun newInstance() = TriangleTwoPlantCountFragment()
    }

    private val binding get() = _binding!!

    override fun onSelected() {
        super.onSelected()
        fieldInfoEntity = database?.fieldInfoDao()?.findOne()

        if (fieldInfoEntity != null) {
            plantCount = fieldInfoEntity?.triangle2PlantCount!!
        }

        binding.txtTrianglePlantCount.editText?.setText(plantCount.toString())
    }

    override fun verifyStep(): VerificationError? {

        plantCount =
            StringToNumberFactory.stringToInt(binding.txtTrianglePlantCount.editText?.text.toString())
        if (plantCount > 0) {
            fieldInfoEntity?.triangle2PlantCount = plantCount
            database?.fieldInfoDao()?.update(fieldInfoEntity = fieldInfoEntity!!)
            return null
        }

        return VerificationError("Provide a valid plant count of at least 1")
    }

}
