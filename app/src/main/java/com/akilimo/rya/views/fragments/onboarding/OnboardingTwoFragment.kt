package com.akilimo.rya.views.fragments.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akilimo.rya.databinding.FragmentOnboardingTwoBinding
import com.akilimo.rya.views.activities.HomeStepperActivity
import com.akilimo.rya.views.fragments.BaseStepFragment
import com.blogspot.atifsoftwares.animatoolib.Animatoo

/**
 * A simple [Fragment] subclass.
 * Use the [OnboardingTwoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OnboardingTwoFragment : BaseStepFragment() {


    private var ctx: Context? = null
    private var _binding: FragmentOnboardingTwoBinding? = null


    private val binding get() = _binding!!

    companion object {
        /**
         * Use this factory method to create a new instance of this fragment
         *
         * @return A new instance of fragment [OnboardingOneFragment].
         */
        @JvmStatic
        fun newInstance() = OnboardingTwoFragment()
    }

    override fun onAttach(_ctx: Context) {
        super.onAttach(_ctx)
        this.ctx = context
    }


    override fun loadFragmentLayout(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSkip.setOnClickListener { _ ->
            val intent = Intent(
                activity, HomeStepperActivity::class.java
            )
            startActivity(intent)
            Animatoo.animateSlideLeft(activity)
        }
    }

}
