package com.akilimo.rya.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.AppDatabase.Companion.getDatabase
import com.akilimo.rya.databinding.ActivityHomeStepperBinding
import com.akilimo.rya.databinding.ActivityStartPageBinding
import com.blogspot.atifsoftwares.animatoolib.Animatoo


class StartPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //clear the database
        clearDatabase()
        binding.btnStart.setOnClickListener {
            val intent = Intent(this, HomeStepperActivity::class.java)
//            val intent = Intent(this, AssessmentActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
            finish()
        }
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
