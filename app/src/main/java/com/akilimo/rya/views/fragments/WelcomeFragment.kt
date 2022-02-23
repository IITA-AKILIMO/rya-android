package com.akilimo.rya.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akilimo.rya.R
import com.akilimo.rya.databinding.FragmentWelcomeBinding
import com.stepstone.stepper.VerificationError

/**
 * A simple [Fragment] subclass.
 * Use the [WelcomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WelcomeFragment : BaseStepFragment() {

    private var _binding: FragmentWelcomeBinding? = null

    private val binding get() = _binding!!

    companion object {
        @JvmStatic
        fun newInstance() = WelcomeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return loadFragmentLayout(inflater, container, savedInstanceState)
    }

    override fun loadFragmentLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun verifyStep(): VerificationError? {
        return verificationError
    }

    override fun onSelected() {}

    override fun onError(error: VerificationError) {}
}
