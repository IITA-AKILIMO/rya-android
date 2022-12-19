package com.akilimo.rya.views.fragments.assessment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.databinding.FragmentYieldEstimateBinding
import com.akilimo.rya.views.fragments.BaseStepFragment
import com.stepstone.stepper.VerificationError

/**
 * A simple [Fragment] subclass.
 * Use the [YieldEstimateFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class YieldEstimateFragment : BaseStepFragment() {
    private var ctx: Context? = null
    private var _binding: FragmentYieldEstimateBinding? = null
    private var database: AppDatabase? = null

    private val binding get() = _binding!!


    companion object {
        @JvmStatic
        fun newInstance() = YieldEstimateFragment()
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYieldEstimateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val yieldEntity = database?.fieldYieldDao()?.findOne()
        if (yieldEntity != null) {
            binding.txtYieldValue.text = yieldEntity.yieldLabel
            binding.txtYieldDesc.text = yieldEntity.fieldYieldDesc
            if (yieldEntity.imageId != null) {
                binding.imgYieldImage.setImageResource(yieldEntity.imageId)
            }
        }
    }

    override fun verifyStep(): VerificationError? {
        return null
    }

    override fun onSelected() {}

    override fun onError(error: VerificationError) {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
