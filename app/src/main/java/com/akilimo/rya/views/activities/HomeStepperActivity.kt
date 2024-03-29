package com.akilimo.rya.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.akilimo.rya.adapter.HomeStepperAdapter
import com.akilimo.rya.databinding.ActivityHomeStepperBinding
import com.akilimo.rya.utils.MySharedPreferences
import com.akilimo.rya.views.activities.triangle.TriangleOneActivity
import com.akilimo.rya.views.fragments.*
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError

class HomeStepperActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeStepperBinding
    private lateinit var prefs: MySharedPreferences

    private lateinit var stepperAdapter: HomeStepperAdapter
    private lateinit var mStepperLayout: StepperLayout


    private val fragmentArray: MutableList<Fragment> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        installSplashScreen()
        binding = ActivityHomeStepperBinding.inflate(layoutInflater)

        setContentView(binding.root)

        mStepperLayout = binding.stepperLayout

        mStepperLayout.setListener(object : StepperLayout.StepperListener {
            override fun onCompleted(completeButton: View?) {
                val intent = Intent(
                    this@HomeStepperActivity, TriangleOneActivity::class.java
                )
                startActivity(intent)
                Animatoo.animateSwipeLeft(this@HomeStepperActivity)
            }

            override fun onError(verificationError: VerificationError?) {
                //evaluate last step and change the finish label
            }

            override fun onStepSelected(newStepPosition: Int) {
                //evaluate last step and change the finish label
            }

            override fun onReturn() {
                //evaluate last step and change the finish label
            }

        })


        prefs = MySharedPreferences(this)

        fragmentArray.add(YieldClassFragment.newInstance())
        fragmentArray.add(FieldInfoFragment.newInstance())
        fragmentArray.add(CassavaPriceFragment.newInstance())
        fragmentArray.add(PlantingPeriodFragment.newInstance())
        fragmentArray.add(PrecisionFragment.newInstance())

        stepperAdapter =
            HomeStepperAdapter(supportFragmentManager, applicationContext, fragmentArray)
        mStepperLayout.adapter = stepperAdapter
    }
}
