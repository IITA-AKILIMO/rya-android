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
import com.akilimo.rya.databinding.MyStepperLayoutBinding
import com.akilimo.rya.views.fragments.*
import com.akilimo.rya.views.fragments.ui.TriangleFragment
import com.akilimo.rya.views.fragments.ui.TriangleThreeFragment
import com.akilimo.rya.views.fragments.ui.TriangleTwoFragment
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError

class PlantTriangleStepperActivity : AppCompatActivity() {
    private lateinit var binding: MyStepperLayoutBinding
    private lateinit var stepperAdapter: MyStepperAdapter
    private lateinit var mStepperLayout: StepperLayout
    private var database: AppDatabase? = null
    private var plantcount = 0

    private val fragmentArray: MutableList<Fragment> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyStepperLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)
        val yieldClass = database?.yieldPrecisionDao()?.findOne()
        mStepperLayout = binding.stepperLayout

        binding.stepperLayout.setListener(object : StepperLayout.StepperListener {
            override fun onCompleted(completeButton: View?) {
                val intent =
                    Intent(this@PlantTriangleStepperActivity, AssessmentActivity::class.java)
                startActivity(intent)
                Animatoo.animateSwipeLeft(this@PlantTriangleStepperActivity)
            }

            override fun onError(verificationError: VerificationError?) {
            }

            override fun onStepSelected(newStepPosition: Int) {
                when (val fragment = fragmentArray[newStepPosition]) {
                    is TriangleFragment -> {
                        fragment.triangleCount = plantcount / 3
                        fragment.triangleName = "one"
                    }
                    is TriangleTwoFragment -> {
                        fragment.triangleCount = plantcount / 3
                        fragment.triangleName = "two"
                    }
                    is TriangleThreeFragment -> {
                        fragment.triangleCount = plantcount / 3
                        fragment.triangleName = "three"
                    }
                }
            }

            override fun onReturn() {
            }

        })


        if (yieldClass != null) {
            plantcount = yieldClass.plantCount
            fragmentArray.add(TriangleFragment.newInstance())
            fragmentArray.add(TriangleTwoFragment.newInstance())
            fragmentArray.add(TriangleThreeFragment.newInstance())
        }
        stepperAdapter = MyStepperAdapter(supportFragmentManager, applicationContext, fragmentArray)
        mStepperLayout.adapter = stepperAdapter
    }
}
