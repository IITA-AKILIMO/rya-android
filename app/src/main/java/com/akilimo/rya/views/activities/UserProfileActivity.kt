package com.akilimo.rya.views.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.databinding.ActivityUserProfile2Binding
import com.akilimo.rya.utils.PrefillCurrency


class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfile2Binding
    private var database: AppDatabase? = null


    private var countryCode: String = ""
    private var fullMobileNumber: String = ""
    private var currencyCode: String = ""
    private var areUnit: String = "ha"

    private var designationList: MutableList<String>? = null
    private var languageList: MutableList<String>? = null
    private var areaUnitList: MutableList<String>? = null
    private var currencyList: MutableList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfile2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)


        designationList = ArrayList()
        designationList?.add("Farmer")
        designationList?.add("Extensionist")
        designationList?.add("Researcher")
        designationList?.add("Other")

        languageList = ArrayList()
        languageList?.add("English")

        areaUnitList = ArrayList()
        areaUnitList?.add("Hectare")
        areaUnitList?.add("Acre")



        @Suppress("USELESS_CAST") with(binding.lytUserProfile) {
            countryCodePicker.registerCarrierNumberEditText(edtPhone)

            designationDropdown.item = designationList as List<Any>?
            languageDropdown.item = languageList as List<Any>?
            areaUnitDropdowm.item = areaUnitList as List<Any>?

            countryCodePicker.setOnCountryChangeListener {
                countryCode = countryCodePicker.selectedCountryNameCode
            }

            languageDropdown.setSelection(0)

            designationDropdown.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        adapterView: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        Toast.makeText(
                            this@UserProfileActivity,
                            designationList!![position],
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                    }

                }
        }
    }
}
