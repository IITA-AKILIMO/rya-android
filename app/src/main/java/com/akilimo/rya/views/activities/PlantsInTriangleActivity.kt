package com.akilimo.rya.views.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.databinding.ActivityPlantsInTriangleBinding
import com.akilimo.rya.entities.FieldInfoEntity
import com.akilimo.rya.utils.PrefillCurrency
import com.akilimo.rya.utils.StringToNumberFactory
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.google.android.material.textfield.TextInputLayout

class PlantsInTriangleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlantsInTriangleBinding

    private var triangle1PlantCount: Int = -1
    private var triangle2PlantCount: Int = -1
    private var triangle3PlantCount: Int = -1
    private var hasError = true
    private var fieldInfoEntity: FieldInfoEntity? = null
    private var database: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
//        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityPlantsInTriangleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)

        fieldInfoEntity = database?.fieldInfoDao()?.findOne()

        if (fieldInfoEntity != null) {
            triangle1PlantCount = fieldInfoEntity?.triangle1PlantCount!!
            triangle2PlantCount = fieldInfoEntity?.triangle2PlantCount!!
            triangle3PlantCount = fieldInfoEntity?.triangle3PlantCount!!
            refreshPlantTriangleCountData(fieldInfoEntity)
        }

        with(binding) {
            txtPlantCountTri1.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    txtPlantCountTri1.error = null
                }

                override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //this will not be implemented
                }

                override fun afterTextChanged(editable: Editable?) {
                    triangle1PlantCount = extractNumberValue(editable)
                    validateInput(triangle1PlantCount, txtPlantCountTri1)
                }
            })

            txtPlantCountTri2.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    txtPlantCountTri2.error = null
                }

                override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //this will not be implemented
                }

                override fun afterTextChanged(editable: Editable?) {
                    triangle2PlantCount = extractNumberValue(editable)
                    validateInput(triangle2PlantCount, txtPlantCountTri2)
                }
            })

            txtPlantCountTri3.editText?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence?, p1: Int, p2: Int, p3: Int
                ) {
                    txtPlantCountTri3.error = null
                }

                override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //this will not be implemented
                }

                override fun afterTextChanged(editable: Editable?) {
                    triangle3PlantCount = extractNumberValue(editable)
                    validateInput(triangle3PlantCount, txtPlantCountTri3)
                }
            })


            btnContinue.setOnClickListener {
                if (!hasError) {
                    if (fieldInfoEntity == null) {
                        fieldInfoEntity = FieldInfoEntity(id = 1)
                    }
                    fieldInfoEntity?.triangle1PlantCount = triangle1PlantCount
                    fieldInfoEntity?.triangle2PlantCount = triangle2PlantCount
                    fieldInfoEntity?.triangle3PlantCount = triangle3PlantCount

                    database?.fieldInfoDao()?.insert(fieldInfoEntity!!)

                    val intent = Intent(
                        this@PlantsInTriangleActivity, PlantTriangleStepperActivity::class.java
                    )
                    startActivity(intent)
                    Animatoo.animateSwipeLeft(this@PlantsInTriangleActivity)
                    finish()
                }
            }
        }
    }

    private fun extractNumberValue(editable: Editable?): Int {
        return StringToNumberFactory.stringToInt(editable.toString())
    }

    private fun refreshPlantTriangleCountData(fieldInfoEntity: FieldInfoEntity?) {
        with(binding) {
            if (fieldInfoEntity != null) {
                with(fieldInfoEntity) {
                    if (triangle1PlantCount > 0) {
                        txtPlantCountTri1.editText?.setText(triangle1PlantCount.toString())
                    }

                    if (triangle2PlantCount > 0) {
                        txtPlantCountTri2.editText?.setText(triangle2PlantCount.toString())
                    }

                    if (triangle3PlantCount > 0) {
                        txtPlantCountTri3.editText?.setText(triangle3PlantCount.toString())
                    }
                }
                hasError = false
            }
        }
    }

    private fun validateInput(intValue: Int, inputLayout: TextInputLayout) {
        if (intValue <= 0) {
            hasError = true
            inputLayout.error = "Enter a value greater than 0"
            return
        }
        hasError = false
    }
}
