package com.akilimo.rya.views.fragments.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akilimo.rya.databinding.FragmentOnboardingTwentyFiveBinding
import com.akilimo.rya.views.fragments.BaseStepFragment

/**
 * A simple [Fragment] subclass.
 * Use the [OnboardingTwentyFiveFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OnboardingTwentyFiveFragment : BaseStepFragment() {


    private var ctx: Context? = null
    private var _binding: FragmentOnboardingTwentyFiveBinding? = null


    private val binding get() = _binding!!

    companion object {
        /**
         * Use this factory method to create a new instance of this fragment
         *
         * @return A new instance of fragment [OnboardingTwentyFiveFragment].
         */
        @JvmStatic
        fun newInstance() = OnboardingTwentyFiveFragment()
    }

    override fun onAttach(_ctx: Context) {
        super.onAttach(_ctx)
        this.ctx = context
    }


    override fun loadFragmentLayout(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingTwentyFiveBinding.inflate(inflater, container, false)
        return binding.root
    }

}
