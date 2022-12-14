package com.akilimo.rya.views.fragments.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akilimo.rya.databinding.FragmentOnboardingFourteenBinding
import com.akilimo.rya.views.fragments.BaseStepFragment

/**
 * A simple [Fragment] subclass.
 * Use the [OnboardingFourteenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OnboardingFourteenFragment : BaseStepFragment() {


    private var ctx: Context? = null
    private var _binding: FragmentOnboardingFourteenBinding? = null


    private val binding get() = _binding!!

    companion object {
        /**
         * Use this factory method to create a new instance of this fragment
         *
         * @return A new instance of fragment [OnboardingFourteenFragment].
         */
        @JvmStatic
        fun newInstance() = OnboardingFourteenFragment()
    }

    override fun onAttach(_ctx: Context) {
        super.onAttach(_ctx)
        this.ctx = context
    }


    override fun loadFragmentLayout(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingFourteenBinding.inflate(inflater, container, false)
        return binding.root
    }

}
