package com.akilimo.rya.views.activities.triangle

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.adapter.MyStepperAdapter
import com.akilimo.rya.databinding.MyStepperLayoutBinding
import com.akilimo.rya.views.activities.AssessmentActivity
import com.akilimo.rya.views.fragments.ui.TriangleThreeFragment
import com.akilimo.rya.views.fragments.ui.TriangleTwoFragment
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError

@Deprecated("To be removed")
class PlantTriangleStepperActivity : AppCompatActivity() {
    private lateinit var binding: MyStepperLayoutBinding
    private lateinit var stepperAdapter: MyStepperAdapter
    private lateinit var mStepperLayout: StepperLayout
    private var database: AppDatabase? = null
    private var plantCount = 0
    private var triangleCount = 0
    private var triangleOnePlantCount = 0
    private var triangleTwoPlantCount = 0
    private var triangleThreePlantCount = 0

    private val fragmentArray: MutableList<Fragment> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyStepperLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)
        val yieldClass = database?.yieldPrecisionDao()?.findOne()
        val fieldInfoEntity = database?.fieldInfoDao()?.findOne()
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
                if (fragmentArray.size <= 0) {
                    return
                }
            }

            override fun onReturn() {
            }

        })

        if (fieldInfoEntity != null) {
            triangleOnePlantCount = fieldInfoEntity.triangle1PlantCount
            triangleTwoPlantCount = fieldInfoEntity.triangle2PlantCount
            triangleThreePlantCount = fieldInfoEntity.triangle3PlantCount
        }

        stepperAdapter = MyStepperAdapter(supportFragmentManager, applicationContext, fragmentArray)
        mStepperLayout.adapter = stepperAdapter
    }
}
