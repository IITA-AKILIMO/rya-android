package com.akilimo.rya.views.fragments.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akilimo.rya.databinding.FragmentOnboardingSeventeenBinding
import com.akilimo.rya.views.fragments.BaseStepFragment

/**
 * A simple [Fragment] subclass.
 * Use the [OnboardingSeventeenFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OnboardingSeventeenFragment : BaseStepFragment() {


    private var ctx: Context? = null
    private var _binding: FragmentOnboardingSeventeenBinding? = null


    private val binding get() = _binding!!

    companion object {
        /**
         * Use this factory method to create a new instance of this fragment
         *
         * @return A new instance of fragment [OnboardingSeventeenFragment].
         */
        @JvmStatic
        fun newInstance() = OnboardingSeventeenFragment()
    }

    override fun onAttach(_ctx: Context) {
        super.onAttach(_ctx)
        this.ctx = context
    }


    override fun loadFragmentLayout(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingSeventeenBinding.inflate(inflater, container, false)
        return binding.root
    }

}
