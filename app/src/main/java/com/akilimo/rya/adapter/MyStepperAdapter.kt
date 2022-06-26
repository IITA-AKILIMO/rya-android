package com.akilimo.rya.adapter

import android.content.Context
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter
import android.os.Bundle
import androidx.annotation.IntRange
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.stepstone.stepper.Step
import com.stepstone.stepper.viewmodel.StepViewModel

open class MyStepperAdapter(
    supportFragmentManager: FragmentManager?,
    private val ctx: Context,
    private val fragmentArray: List<Fragment>
) : AbstractFragmentStepAdapter(
    supportFragmentManager!!, ctx
) {
    private val positionKey = "CURRENT_STEP_POSITION_KEY"

    override fun getCount(): Int {
        return fragmentArray.size
    }

    override fun createStep(position: Int): Step {
        val step = fragmentArray[position]
        val bundleParams = Bundle()
        bundleParams.putInt(positionKey, position)
        step.arguments = bundleParams
        return step as Step
    }


    override fun getViewModel(@IntRange(from = 0) position: Int): StepViewModel {
        val builder = StepViewModel.Builder(ctx)
        if(position==0){
            builder.setBackButtonLabel("Cancel")
        }else if(position ==count-1){
            builder.setEndButtonLabel("Finish")
        }
        return builder.create()
    }
}
