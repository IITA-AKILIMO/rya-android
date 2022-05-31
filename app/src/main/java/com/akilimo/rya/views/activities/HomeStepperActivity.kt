package com.akilimo.rya.views.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.akilimo.rya.adapter.MyStepperAdapter
import com.akilimo.rya.rest.response.RemoteConfig
import com.akilimo.rya.databinding.ActivityHomeStepperBinding
import com.akilimo.rya.rest.ApiInterface
import com.akilimo.rya.rest.FuelrodApiInterface
import com.akilimo.rya.utils.MySharedPreferences
import com.akilimo.rya.views.fragments.*
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.stepstone.stepper.StepperLayout
import com.stepstone.stepper.VerificationError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeStepperActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeStepperBinding
    private lateinit var apiInterface: ApiInterface
    private lateinit var fuelrodApiInterface: FuelrodApiInterface
    private lateinit var prefs: MySharedPreferences

    private lateinit var stepperAdapter: MyStepperAdapter
    private lateinit var mStepperLayout: StepperLayout

    private val fragmentArray: MutableList<Fragment> = arrayListOf()


    override fun onAttachFragment(fragment: Fragment) {
        //handle fragment attachment

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        installSplashScreen()
        binding = ActivityHomeStepperBinding.inflate(layoutInflater)
        apiInterface = ApiInterface.create()
        fuelrodApiInterface = FuelrodApiInterface.create()

        setContentView(binding.root)

        mStepperLayout = binding.stepperLayout

        binding.stepperLayout.setListener(object : StepperLayout.StepperListener {
            override fun onCompleted(completeButton: View?) {
                val intent = Intent(this@HomeStepperActivity, PlantTrianglesActivity::class.java)
                startActivity(intent)
                Animatoo.animateSwipeLeft(this@HomeStepperActivity)
            }

            override fun onError(verificationError: VerificationError?) {
            }

            override fun onStepSelected(newStepPosition: Int) {
            }

            override fun onReturn() {
            }

        })


        prefs = MySharedPreferences(this)

        loadConfig()
        createFragmentArray()
        initComponent()
    }


    private fun createFragmentArray() {
        fragmentArray.add(YieldClassFragmentFragment.newInstance())
        fragmentArray.add(FieldInfoFragment.newInstance())
        fragmentArray.add(PlantingPeriodFragment.newInstance())
        fragmentArray.add(PrecisionFragment.newInstance())
        fragmentArray.add(BeforeStartingFragment.newInstance())
    }

    private fun initComponent() {
        stepperAdapter = MyStepperAdapter(supportFragmentManager, applicationContext, fragmentArray)
        mStepperLayout.adapter = stepperAdapter
    }

    private fun loadConfig() {
        val configReader = fuelrodApiInterface.readConfig("rya")


        configReader.enqueue(object : Callback<List<RemoteConfig>> {
            override fun onResponse(
                call: Call<List<RemoteConfig>>,
                response: Response<List<RemoteConfig>>
            ) {
                val configList = response.body()
                if (configList != null) {
                    if (configList.isNotEmpty()) {
                        val remoteConfig = configList[0]
                        prefs.saveApiEndpoint(remoteConfig.configValue)
                    }
                }
            }

            override fun onFailure(call: Call<List<RemoteConfig>>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Unable to load remote configurations",
                    Toast.LENGTH_SHORT
                ).show();
            }
        })
    }

    private fun loadPlots() {
//        val imageView = binding.imageView
//
//
//        val plotReader = apiInterface.readPlots(RyaPlot(fileName = "device_202"))
//
//        plotReader.enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                if (response.body() != null) {
//                    val bytes = response.body()!!.bytes()
//                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//                    imageView.setImage(ImageSource.bitmap(bitmap.rotate(-90f)))
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, throwable: Throwable) {
//                Toast.makeText(
//                    applicationContext,
//                    "Unable to load plot data",
//                    Toast.LENGTH_SHORT
//                ).show();
//            }
//
//        })
    }
}
