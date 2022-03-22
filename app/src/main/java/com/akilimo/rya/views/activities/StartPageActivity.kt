package com.akilimo.rya.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.akilimo.rya.databinding.ActivityHomeStepperBinding
import com.akilimo.rya.databinding.ActivityStartPageBinding
import com.blogspot.atifsoftwares.animatoolib.Animatoo


class StartPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
//            val intent = Intent(this, HomeStepperActivity::class.java)
            val intent = Intent(this, PlantTrianglesActivity::class.java)
            startActivity(intent)
            Animatoo.animateSlideLeft(this)
//            finish()
        }
    }
}
