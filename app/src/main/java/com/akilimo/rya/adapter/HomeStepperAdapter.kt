package com.akilimo.rya.adapter

import android.content.Context
import androidx.annotation.IntRange
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.stepstone.stepper.viewmodel.StepViewModel

class HomeStepperAdapter(
    supportFragmentManager: FragmentManager?,
    private val ctx: Context,
    private val fragmentArray: List<Fragment>
) : MyStepperAdapter(supportFragmentManager, ctx, fragmentArray) {

    override fun getViewModel(@IntRange(from = 0) position: Int): StepViewModel {
        val builder = StepViewModel.Builder(ctx)
        if (position == 0) {
            builder.setBackButtonLabel("Cancel")
        } else if (position == count - 1) {
            builder.setEndButtonLabel("Continue")
        }
        return builder.create()
    }
}
