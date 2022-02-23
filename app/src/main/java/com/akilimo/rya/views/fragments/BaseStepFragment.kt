package com.akilimo.rya.views.fragments

import android.content.Context
import com.akilimo.rya.views.fragments.BaseStepFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError

abstract class BaseStepFragment : Fragment(), Step {

    protected var LOG_TAG = this::class.java.simpleName

    protected var verificationError: VerificationError? = null

    private var ctx: Context? = null
    protected var dataIsValid = false

    override fun onAttach(_ctx: Context) {
        super.onAttach(_ctx)
        this.ctx = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return loadFragmentLayout(inflater, container, savedInstanceState)
    }

    protected abstract fun loadFragmentLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
}
