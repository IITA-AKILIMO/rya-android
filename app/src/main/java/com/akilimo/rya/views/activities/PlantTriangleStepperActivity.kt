package com.akilimo.rya.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.R
import com.akilimo.rya.adapter.MyStepperAdapter
import com.akilimo.rya.databinding.ActivityHomeStepperBinding
import com.akilimo.rya.views.fragments.*
import com.akilimo.rya.views.fragments.ui.TriangleFragment
import com.akilimo.rya.views.fragments.ui.TriangleThreeFragment
import com.akilimo.rya.views.fragments.ui.TriangleTwoFragment
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError

class PlantTriangleStepperActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeStepperBinding
    private lateinit var stepperAdapter: MyStepperAdapter
    private lateinit var mStepperLayout: StepperLayout
    private var database: AppDatabase? = null
    private var plantcount = 0

    private val fragmentArray: MutableList<Fragment> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeStepperBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)
        val yieldClass = database?.yieldPrecisionDao()?.findOne()
        mStepperLayout = binding.stepperLayout

        binding.stepperLayout.setListener(object : StepperLayout.StepperListener {
            override fun onCompleted(completeButton: View?) {
                val intent = Intent(this@PlantTriangleStepperActivity, AssessmentActivity::class.java)
                startActivity(intent)
                Animatoo.animateSwipeLeft(this@PlantTriangleStepperActivity)
            }

            override fun onError(verificationError: VerificationError?) {
            }

            override fun onStepSelected(newStepPosition: Int) {
            }

            override fun onReturn() {
            }

        })

        stepperAdapter = MyStepperAdapter(supportFragmentManager, applicationContext, fragmentArray)
        mStepperLayout.adapter = stepperAdapter

        if (yieldClass != null) {
            plantcount = yieldClass.plantCount
            fragmentArray.add(TriangleFragment.newInstance(plantcount / 3, "one"))
            fragmentArray.add(TriangleTwoFragment.newInstance(plantcount / 3, "two"))
            fragmentArray.add(TriangleThreeFragment.newInstance(plantcount / 3, "three"))
        }

    }
}
