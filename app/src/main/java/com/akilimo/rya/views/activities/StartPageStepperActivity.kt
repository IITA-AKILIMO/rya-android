package com.akilimo.rya.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.akilimo.rya.adapter.HomeStepperAdapter
import com.akilimo.rya.databinding.ActivityStartPageStepperBinding
import com.akilimo.rya.views.fragments.WelcomeFragment
import com.akilimo.rya.views.fragments.onboarding.*
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError

private val fragmentArray: MutableList<Fragment> = arrayListOf()


class StartPageStepperActivity : AppCompatActivity() {

    private lateinit var stepperAdapter: HomeStepperAdapter
    private lateinit var mStepperLayout: StepperLayout

    private lateinit var binding: ActivityStartPageStepperBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartPageStepperBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mStepperLayout = binding.startPageStepperLayout

        mStepperLayout.setListener(object : StepperLayout.StepperListener {
            /**
             * Called when all of the steps were completed successfully
             *
             * @param completeButton the complete button that was clicked to complete the flow
             */
            override fun onCompleted(completeButton: View?) {
                val intent = Intent(this@StartPageStepperActivity, HomeStepperActivity::class.java)
                startActivity(intent)
                Animatoo.animateSlideLeft(this@StartPageStepperActivity)
            }

            /**
             * Called when a verification error occurs for one of the steps
             *
             * @param verificationError verification error
             */
            override fun onError(verificationError: VerificationError?) {
            }

            /**
             * Called when the current step position changes
             *
             * @param newStepPosition new step position
             */
            override fun onStepSelected(newStepPosition: Int) {
            }

            /**
             * Called when the Previous step button was pressed while on the first step
             * (the button is not present by default on first step).
             */
            override fun onReturn() {
            }

        })

        fragmentArray.add(WelcomeFragment.newInstance())
        fragmentArray.add(OnboardingOneFragment.newInstance())
        fragmentArray.add(OnboardingTwoFragment.newInstance())
        fragmentArray.add(OnboardingThreeFragment.newInstance())
        fragmentArray.add(OnboardingFourFragment.newInstance())
        fragmentArray.add(OnboardingFiveFragment.newInstance())
        fragmentArray.add(OnboardingSixFragment.newInstance())
        fragmentArray.add(OnboardingSevenFragment.newInstance())
        fragmentArray.add(OnboardingEightFragment.newInstance())
        fragmentArray.add(OnboardingNineFragment.newInstance())
        fragmentArray.add(OnboardingTenFragment.newInstance())
        fragmentArray.add(OnboardingElevenFragment.newInstance())
        fragmentArray.add(OnboardingTwelveFragment.newInstance())
        fragmentArray.add(OnboardingThirteenFragment.newInstance())
        fragmentArray.add(OnboardingFourteenFragment.newInstance())
        fragmentArray.add(OnboardingFifteenFragment.newInstance())
        fragmentArray.add(OnboardingSixteenFragment.newInstance())
        fragmentArray.add(OnboardingSeventeenFragment.newInstance())
        fragmentArray.add(OnboardingEighteenFragment.newInstance())
        fragmentArray.add(OnboardingNineteenFragment.newInstance())
        fragmentArray.add(OnboardingTwentyFragment.newInstance())
        fragmentArray.add(OnboardingTwentyOneFragment.newInstance())
        fragmentArray.add(OnboardingTwentyTwoFragment.newInstance())
        fragmentArray.add(OnboardingTwentyThreeFragment.newInstance())
        fragmentArray.add(OnboardingTwentyFourFragment.newInstance())
        fragmentArray.add(OnboardingTwentyFiveFragment.newInstance())
        fragmentArray.add(OnboardingTwentySixFragment.newInstance())
        stepperAdapter =
            HomeStepperAdapter(supportFragmentManager, applicationContext, fragmentArray)
        mStepperLayout.adapter = stepperAdapter

    }
}
