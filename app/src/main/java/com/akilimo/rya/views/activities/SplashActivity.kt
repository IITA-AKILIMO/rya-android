package com.akilimo.rya.views.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


class SplashActivity : AppCompatActivity() {

    val LOG_TAG: String = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponent()
    }

    private fun initComponent() {
        try {
            val background = object : Thread() {
                override fun run() {
                    launchActivity()
                }
            }
            background.start()
        } catch (ex: Exception) {
            launchActivity()
        }
    }

    private fun launchActivity() {
        val intent = Intent(this@SplashActivity, HomeStepperActivity::class.java)
        startActivity(intent)
        finish()
    }
}
