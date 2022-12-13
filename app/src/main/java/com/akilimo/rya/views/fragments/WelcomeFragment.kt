package com.akilimo.rya.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akilimo.rya.databinding.FragmentWelcomeBinding

/**
 * A simple [Fragment] subclass.
 * Use the [WelcomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WelcomeFragment : BaseStepFragment() {
    private var ctx: Context? = null
    private var _binding: FragmentWelcomeBinding? = null

    private val binding get() = _binding!!

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment [WelcomeFragment].
         */
        @JvmStatic
        fun newInstance() = WelcomeFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.ctx = context
    }

    override fun loadFragmentLayout(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }
}
