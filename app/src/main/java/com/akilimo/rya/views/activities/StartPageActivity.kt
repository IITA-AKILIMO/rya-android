package com.akilimo.rya.views.activities


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.aemerse.onboard.OnboardAdvanced
import com.aemerse.onboard.OnboardFragment
import com.akilimo.rya.AppDatabase.Companion.getDatabase
import com.akilimo.rya.R
import com.akilimo.rya.databinding.ActivityStartPageBinding
import com.blogspot.atifsoftwares.animatoolib.Animatoo


class StartPageActivity : OnboardAdvanced() {

    private lateinit var binding: ActivityStartPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(
            OnboardFragment.newInstance(
                title = "Rapid yield assessment tool",
                description = "Welcome to the AKILIMO rapid yield assessment tool, this tools offers several advantages for your cassava production",
                resourceId = R.drawable.ic_akilimo_logo,
                titleColor = ContextCompat.getColor(this, R.color.primaryDarkColor),
                descriptionColor = ContextCompat.getColor(this, R.color.primaryColor),
            )
        )

        addSlide(
            OnboardFragment.newInstance(
                title = "Measure yield",
                description = "Measure yield before harvest",
                resourceId = R.raw.tractor,
                titleColor = ContextCompat.getColor(this, R.color.primaryDarkColor),
                descriptionColor = ContextCompat.getColor(this, R.color.primaryColor),
//                backgroundColor = ContextCompat.getColor(this, R.color.primaryColor),
                isLottie = true
            )
        )

        addSlide(
            OnboardFragment.newInstance(
                title = "Negotiate fair market price",
                description = "The assessment results will help you negotiate a fair market price",
                resourceId = R.raw.negotiate,
                titleColor = ContextCompat.getColor(this, R.color.primaryDarkColor),
                descriptionColor = ContextCompat.getColor(this, R.color.primaryColor),
                isLottie = true
            )
        )


        addSlide(
            OnboardFragment.newInstance(
                title = "Improve your cassava knowledge",
                description = "Improve your knowledge about cassava farming to achieve the best yields on your farm",
                resourceId = R.raw.mind,
                titleColor = ContextCompat.getColor(this, R.color.primaryDarkColor),
                descriptionColor = ContextCompat.getColor(this, R.color.primaryColor),
                isLottie = true
            )
        )

    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)

        val intent = Intent(this, HomeStepperActivity::class.java)
        startActivity(intent)
        Animatoo.animateSlideLeft(this@StartPageActivity)
    }

    private fun clearDatabase() {
        try {
            val background = object : Thread() {
                override fun run() {
                    getDatabase(this@StartPageActivity)?.clearAllTables()
                }
            }
            background.start()
        } catch (ex: Exception) {
            println(ex.localizedMessage)
        }
    }
}
