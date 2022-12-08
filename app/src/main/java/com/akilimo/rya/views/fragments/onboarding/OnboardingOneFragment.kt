package com.akilimo.rya.views.fragments.onboarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akilimo.rya.R
import com.akilimo.rya.databinding.FragmentOnboardingOneBinding
import com.akilimo.rya.databinding.FragmentYieldClassBinding
import com.akilimo.rya.views.fragments.BaseStepFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OnboardingOneFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OnboardingOneFragment : BaseStepFragment() {


    private var ctx: Context? = null
    private var _binding: FragmentOnboardingOneBinding? = null


    private val binding get() = _binding!!

    companion object {
        /**
         * Use this factory method to create a new instance of this fragment
         *
         * @return A new instance of fragment OnboardingOneFragment.
         */
        @JvmStatic
        fun newInstance() = OnboardingOneFragment()
    }

    override fun onAttach(_ctx: Context) {
        super.onAttach(_ctx)
        this.ctx = context
    }


    override fun loadFragmentLayout(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingOneBinding.inflate(inflater, container, false)
        return binding.root
    }

}
