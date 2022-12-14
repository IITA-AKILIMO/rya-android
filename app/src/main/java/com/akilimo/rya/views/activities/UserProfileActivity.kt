package com.akilimo.rya.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.akilimo.rya.databinding.ActivityUserProfile2Binding


class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfile2Binding

    private var countryCode: String = ""
    private var fullMobileNumber: String = ""
    private var currencyCode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfile2Binding.inflate(layoutInflater)
        setContentView(binding.root)




        with(binding.lytUserProfile) {
            countryCodePicker.setOnCountryChangeListener {
                countryCode = countryCodePicker.selectedCountryNameCode
            }

            countryCodePicker.registerCarrierNumberEditText(edtPhone)
        }
    }
}
