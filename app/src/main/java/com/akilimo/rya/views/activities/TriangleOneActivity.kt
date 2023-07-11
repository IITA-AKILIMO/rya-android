package com.akilimo.rya.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.akilimo.rya.adapter.HomeStepperAdapter
import com.akilimo.rya.adapter.MyStepperAdapter
import com.akilimo.rya.databinding.ActivityTriangleOneBinding
import com.akilimo.rya.utils.MySharedPreferences
import com.akilimo.rya.views.fragments.YieldClassFragment
import com.akilimo.rya.views.fragments.triangle.TriangleOneFragment
import com.stepstone.stepper.StepperLayout

class TriangleOneActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTriangleOneBinding
    private lateinit var prefs: MySharedPreferences

    private lateinit var stepperAdapter: MyStepperAdapter
    private lateinit var mStepperLayout: StepperLayout

    private val fragmentArray: MutableList<Fragment> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTriangleOneBinding.inflate(layoutInflater)

        setContentView(binding.root)

        mStepperLayout = binding.stepperLayout

        fragmentArray.add(TriangleOneFragment.newInstance())
        fragmentArray.add(TriangleOneFragment.newInstance())
        fragmentArray.add(TriangleOneFragment.newInstance())

        stepperAdapter =
            HomeStepperAdapter(supportFragmentManager, applicationContext, fragmentArray)
        mStepperLayout.adapter = stepperAdapter
    }
}
