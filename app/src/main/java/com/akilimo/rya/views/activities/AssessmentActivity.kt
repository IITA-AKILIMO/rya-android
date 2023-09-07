package com.akilimo.rya.views.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.akilimo.rya.adapter.MyStepperAdapter
import com.akilimo.rya.databinding.ActivityHomeStepperBinding
import com.akilimo.rya.utils.MySharedPreferences
import com.akilimo.rya.views.fragments.assessment.AssessmentResultsFragment
import com.akilimo.rya.views.fragments.assessment.PlotResultsFragment
import com.akilimo.rya.views.fragments.assessment.YieldEstimateFragment
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError

class AssessmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeStepperBinding
    private lateinit var prefs: MySharedPreferences

    private lateinit var stepperAdapter: MyStepperAdapter
    private lateinit var mStepperLayout: StepperLayout

    private val fragmentArray: MutableList<Fragment> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeStepperBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = MySharedPreferences(this)

        fragmentArray.add(YieldEstimateFragment.newInstance())
        fragmentArray.add(AssessmentResultsFragment.newInstance(prefs.loadApiEndpoint()))

        mStepperLayout = binding.stepperLayout
        stepperAdapter = MyStepperAdapter(supportFragmentManager, applicationContext, fragmentArray)
        mStepperLayout.adapter = stepperAdapter
    }
}
