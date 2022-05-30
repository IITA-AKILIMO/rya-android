package com.akilimo.rya.views.fragments

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akilimo.rya.interfaces.IFragmentInflater
import com.stepstone.stepper.Step
import com.stepstone.stepper.VerificationError

abstract class BaseStepFragment : BaseFragment(), Step {

    protected var LOG_TAG = this::class.java.simpleName

    protected var verificationError: VerificationError? = null


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

    override fun verifyStep(): VerificationError? {
        return null
    }

    override fun onSelected() {}
    override fun onError(error: VerificationError) {}

    fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
}
