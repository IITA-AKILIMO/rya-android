package com.akilimo.rya.views.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.adapter.MySpinnerAdapter
import com.akilimo.rya.databinding.ActivityUserProfile2Binding
import com.akilimo.rya.entities.CurrencyEntity
import com.akilimo.rya.entities.UserInfoEntity
import com.akilimo.rya.utils.MySharedPreferences
import com.google.android.material.snackbar.Snackbar


class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserProfile2Binding
    private var database: AppDatabase? = null


    private var countryCode: String? = null
    private var designation: String? = null
    private var fullMobileNumber: String = ""
    private var currencyCode: String? = null
    private var currencyName: String? = null
    private var areaUnit: String? = "ha"
    private var areaUnitText: String? = "Hectare"
    private var email: String = ""
    private var fullNames: String? = null
    private var language: String = "English"

    private var hasError: Boolean = true

    private var designationList: MutableList<String> = ArrayList()
    private var languageList: MutableList<String> = ArrayList()
    private var areaUnitList: MutableList<String> = ArrayList()
    private var currencyEntityList: MutableList<CurrencyEntity> = ArrayList()
    private var currencyList: MutableList<String> = ArrayList()

    private var currencyAdapter: MySpinnerAdapter? = null
    private var userInfoEntity: UserInfoEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfile2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        database = AppDatabase.getDatabase(this)


        designationList = ArrayList()
        designationList.add("Farmer")
        designationList.add("Extensionist")
        designationList.add("Researcher")
        designationList.add("Other")

        languageList = ArrayList()
        languageList.add("English")

        areaUnitList = ArrayList()
        areaUnitList.add("Hectare")
        areaUnitList.add("Acre")

        currencyEntityList = database!!.currencyDao().getAll()
        userInfoEntity = database?.userInfoDao()?.findOne()

        currencyAdapter = MySpinnerAdapter(this, currencyEntityList)
        for (currency in currencyEntityList) {
            currencyList.add(currency.currencyCode)
        }


        @Suppress("USELESS_CAST") with(binding.lytUserProfile) {
            countryCodePicker.registerCarrierNumberEditText(edtPhone)

            designationDropdown.item = designationList as List<Any>?
            languageDropdown.item = languageList as List<Any>?
            currencyDropdown.item = currencyList as List<Any>?
            areaUnitDropdowm.item = areaUnitList as List<Any>?

            if (userInfoEntity != null) {
                if (!userInfoEntity?.phoneNumber.isNullOrBlank()) {
                    countryCodePicker.fullNumber = userInfoEntity?.phoneNumber
                }

                txtName.editText?.setText(userInfoEntity?.fullNames)
                txtEmail.editText?.setText(userInfoEntity?.email)

                countryCode = userInfoEntity?.countryCode
                currencyCode = userInfoEntity?.currencyCode
                currencyName = userInfoEntity?.currencyName
                areaUnit = userInfoEntity?.areaUnit
                countryCodePicker.setCountryForNameCode(countryCode)

                currencyDropdown.setSelection(currencyList.indexOf(userInfoEntity?.currencyCode))
                languageDropdown.setSelection(languageList.indexOf(userInfoEntity?.language))
                areaUnitDropdowm.setSelection(areaUnitList.indexOf(userInfoEntity?.areaUnitText))
                designationDropdown.setSelection(designationList.indexOf(userInfoEntity?.designation))
            } else {
                languageDropdown.setSelection(0)
                areaUnitDropdowm.setSelection(0)
            }

            countryCodePicker.setOnCountryChangeListener {
                countryCode = countryCodePicker.selectedCountryNameCode
                //get the country currency code
                val currency = getCountryCurrencyCode(countryCodePicker.selectedCountryName)
                if (currency != null) {
                    val index = currencyList.indexOf(currency.currencyCode)
                    currencyDropdown.setSelection(index)
                    currencyName = currency.currencyName
                }

            }


            designationDropdown.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        adapterView: AdapterView<*>?, view: View?, position: Int, id: Long
                    ) {
                        designation = designationList!![position]
                        designationDropdown.errorText = null
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }

            areaUnitDropdowm.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    areaUnitText = areaUnitList[position]
                    if (areaUnitText.equals("acre", true)) {
                        areaUnit = "Acre"
                    } else if (areaUnitText.equals("hectare", true)) {

                        areaUnit = "ha"
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

            currencyDropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?, view: View?, position: Int, id: Long
                ) {
                    currencyCode = currencyList[position]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

            btnSave.setOnClickListener { vw ->
                saveUserInfo(vw)
                //flag preferences as saved
            }
        }
    }

    private fun getCountryCurrencyCode(countryName: String?): CurrencyEntity? {
        return database?.currencyDao()?.findByCountryName(countryName = countryName!!.uppercase())
    }

    private fun saveUserInfo(vw: View) {
        hasError = false
        var errMessage = ""
        with(binding.lytUserProfile) {
            txtEmail.error = null
            txtName.error = null
            txtPhoneNumber.error = null

            fullNames = txtName.editText?.text.toString()
            email = txtEmail.editText?.text.toString()
            fullMobileNumber = countryCodePicker.fullNumber
            countryCode = countryCodePicker.selectedCountryNameCode



            if (fullNames.isNullOrBlank()) {
                hasError = true
                errMessage = "Fill in your names"
                txtName.error = errMessage
            }

            if (countryCode.isNullOrBlank()) {
                errMessage = "Please select a country"
                hasError = true
            }

            if (currencyCode.isNullOrBlank()) {
                errMessage = "Please select a valid currency"
                hasError = true
            }

            if (designation.isNullOrBlank()) {
                hasError = true
                errMessage = "Pick a valid designation"
                designationDropdown.errorText = errMessage
            }

            if (areaUnit.isNullOrBlank()) {
                hasError = true
                errMessage = "Pick a valid area unit"
                designationDropdown.errorText = errMessage
            }

        }

        if (hasError) {
            Snackbar.make(vw, errMessage, Snackbar.LENGTH_LONG).show()
            return
        }
        if (userInfoEntity == null) {
            userInfoEntity = UserInfoEntity(
                id = 1,
                fullNames = fullNames!!,
                countryCode = countryCode!!,
                designation = designation!!,
                phoneNumber = fullMobileNumber,
                areaUnit = areaUnit!!,
                areaUnitText = areaUnitText!!,
                currencyCode = currencyCode!!,
                currencyName = currencyName!!,
                email = email,
                language = language
            )

            database?.userInfoDao()?.insert(userInfoEntity!!)
        } else {
            userInfoEntity?.fullNames = fullNames!!
            userInfoEntity?.countryCode = countryCode!!
            userInfoEntity?.designation = designation!!
            userInfoEntity?.phoneNumber = fullMobileNumber
            userInfoEntity?.areaUnit = areaUnit!!
            userInfoEntity?.areaUnitText = areaUnitText!!
            userInfoEntity?.currencyName = currencyName!!
            userInfoEntity?.currencyCode = currencyCode!!
            userInfoEntity?.email = email
            userInfoEntity?.language = language

            database?.userInfoDao()?.update(userInfoEntity!!)
        }

        Toast.makeText(
            applicationContext, "Profile information updated successfully", Toast.LENGTH_SHORT
        ).show();
        val prefs = MySharedPreferences(this)
        prefs.saveFilledProfileInfo(true)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (!hasError) {
            moveTaskToBack(true)
        }
    }
}
