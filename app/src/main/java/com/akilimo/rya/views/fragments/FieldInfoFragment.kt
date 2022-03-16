package com.akilimo.rya.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.akilimo.rya.databinding.FragmentFieldInfoBinding
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener
import com.stepstone.stepper.VerificationError


/**
 * A simple [Fragment] subclass.
 * Use the [FieldInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FieldInfoFragment : BaseStepFragment() {

    private var ctx: Context? = null
    private var _binding: FragmentFieldInfoBinding? = null

    private var selectedFieldUnit: String? = null
    private var selectedSellingUnit: String? = null

    private val binding get() = _binding!!

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment FieldInfoFragment.
         */
        @JvmStatic
        fun newInstance() = FieldInfoFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.ctx = context
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return loadFragmentLayout(inflater, container, savedInstanceState)
    }

    override fun loadFragmentLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFieldInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fieldSizePrompt.setOnSpinnerItemSelectedListener(OnSpinnerItemSelectedListener<String?> { oldIndex, oldItem, newIndex, newItem ->
            Toast.makeText(ctx, newItem, Toast.LENGTH_SHORT).show()
        })
    }

    override fun verifyStep(): VerificationError? {
        return verificationError
    }

    override fun onSelected() {
    }

    override fun onError(error: VerificationError) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
