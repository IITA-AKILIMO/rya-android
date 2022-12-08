package com.akilimo.rya.views.fragments.onboarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akilimo.rya.R
import com.akilimo.rya.databinding.FragmentOnboardingOneBinding
import com.akilimo.rya.databinding.FragmentOnboardingThreeBinding
import com.akilimo.rya.databinding.FragmentYieldClassBinding
import com.akilimo.rya.views.fragments.BaseStepFragment

/**
 * A simple [Fragment] subclass.
 * Use the [OnboardingThreeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OnboardingThreeFragment : BaseStepFragment() {


    private var ctx: Context? = null
    private var _binding: FragmentOnboardingThreeBinding? = null


    private val binding get() = _binding!!

    companion object {
        /**
         * Use this factory method to create a new instance of this fragment
         *
         * @return A new instance of fragment OnboardingOneFragment.
         */
        @JvmStatic
        fun newInstance() = OnboardingThreeFragment()
    }

    override fun onAttach(_ctx: Context) {
        super.onAttach(_ctx)
        this.ctx = context
    }


    override fun loadFragmentLayout(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingThreeBinding.inflate(inflater, container, false)
        return binding.root
    }

}
