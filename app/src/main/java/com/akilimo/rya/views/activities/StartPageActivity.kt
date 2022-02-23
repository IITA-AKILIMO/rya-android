package com.akilimo.rya.views.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.akilimo.rya.databinding.ActivityHomeStepperBinding
import com.akilimo.rya.databinding.ActivityStartPageBinding


class StartPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
