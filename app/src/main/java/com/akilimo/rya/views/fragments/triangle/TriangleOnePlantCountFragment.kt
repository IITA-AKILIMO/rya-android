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
 * Use the [TriangleOnePlantCountFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open class TriangleOnePlantCountFragment : BaseStepFragment() {

    protected var ctx: Context? = null
    protected var _binding: FragmentPlantCountBinding? = null
    protected var fieldInfoEntity: FieldInfoEntity? = null
    protected var database: AppDatabase? = null
    protected var plantCount = 0

    private val binding get() = _binding!!

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment TriangleOneFragment.
         */
        @JvmStatic
        fun newInstance() = TriangleOnePlantCountFragment()
    }


    override fun onAttach(_ctx: Context) {
        super.onAttach(_ctx)
        this.ctx = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(ctx!!)

    }

    override fun onSelected() {
        super.onSelected()
        fieldInfoEntity = database?.fieldInfoDao()?.findOne()

        if (fieldInfoEntity != null) {
            plantCount = fieldInfoEntity?.triangle1PlantCount!!
        }

        binding.txtTrianglePlantCount.editText?.setText(plantCount.toString())
    }

    override fun loadFragmentLayout(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlantCountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun verifyStep(): VerificationError? {

        plantCount =
            StringToNumberFactory.stringToInt(binding.txtTrianglePlantCount.editText?.text.toString())
        if (plantCount > 0) {
            fieldInfoEntity?.triangle1PlantCount = plantCount
            database?.fieldInfoDao()?.update(fieldInfoEntity = fieldInfoEntity!!)
            return null
        }

        return VerificationError("Provide a valid plant count of at least 1")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
