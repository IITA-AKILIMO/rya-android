package com.akilimo.rya.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.akilimo.rya.R;
import com.akilimo.rya.adapter.MyStepperAdapter
import com.akilimo.rya.databinding.ActivityHomeStepperBinding
import com.akilimo.rya.rest.ApiInterface
import com.akilimo.rya.rest.FuelrodApiInterface
import com.akilimo.rya.utils.MySharedPreferences
import com.stepstone.stepper.StepperLayout

class AssessmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeStepperBinding
    private lateinit var apiInterface: ApiInterface
    private lateinit var fuelrodApiInterface: FuelrodApiInterface
    private lateinit var prefs: MySharedPreferences

    private lateinit var stepperAdapter: MyStepperAdapter
    private lateinit var mStepperLayout: StepperLayout

    private val fragmentArray: MutableList<Fragment> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
