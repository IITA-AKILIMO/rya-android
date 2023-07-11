package com.akilimo.rya.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.akilimo.rya.adapter.MyStepperAdapter
import com.akilimo.rya.databinding.ActivityTriangleOneBinding
import com.akilimo.rya.utils.MySharedPreferences
import com.akilimo.rya.views.fragments.triangle.TriangleOnePlantCountFragment
import com.akilimo.rya.views.fragments.ui.TriangleFragment
import com.google.android.material.snackbar.Snackbar
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError

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

        fragmentArray.add(TriangleOnePlantCountFragment.newInstance())
        fragmentArray.add(TriangleFragment.newInstance())
        fragmentArray.add(TriangleOnePlantCountFragment.newInstance())

        stepperAdapter =
            MyStepperAdapter(supportFragmentManager, applicationContext, fragmentArray)
        mStepperLayout.adapter = stepperAdapter

        binding.stepperLayout.setListener(object : StepperLayout.StepperListener {
            override fun onCompleted(completeButton: View?) {
                TODO("Not yet implemented")
            }

            override fun onError(verificationError: VerificationError) {
                val snackBar = Snackbar.make(
                    binding.stepperLayout, verificationError.errorMessage, Snackbar.LENGTH_SHORT
                )

                snackBar.setAction("RETRY") {
                    snackBar.dismiss()
                }
                snackBar.show()
            }

            override fun onStepSelected(newStepPosition: Int) {
                if (fragmentArray.isEmpty()) {
                    return
                }
            }

            override fun onReturn() {
                /* Will not be implemented */
            }

        })
    }
}
