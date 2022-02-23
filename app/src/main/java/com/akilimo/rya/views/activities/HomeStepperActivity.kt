package com.akilimo.rya.views.activities

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.akilimo.rya.adapter.MyStepperAdapter
import com.akilimo.rya.data.RemoteConfig
import com.akilimo.rya.databinding.ActivityHomeStepperBinding
import com.akilimo.rya.interfaces.IFragmentCallBack
import com.akilimo.rya.rest.ApiInterface
import com.akilimo.rya.rest.FuelrodApiInterface
import com.akilimo.rya.utils.MySharedPreferences
import com.akilimo.rya.views.fragments.WelcomeFragment
import com.stepstone.stepper.StepperLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeStepperActivity : AppCompatActivity(), IFragmentCallBack {
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
        installSplashScreen()
        binding = ActivityHomeStepperBinding.inflate(layoutInflater)
        apiInterface = ApiInterface.create()
        fuelrodApiInterface = FuelrodApiInterface.create()

        setContentView(binding.root)

        mStepperLayout = binding.stepperLayout

        prefs = MySharedPreferences(this)

        loadConfig()
        createFragmentArray()
        initComponent()
    }


    private fun createFragmentArray() {
        fragmentArray.add(WelcomeFragment.newInstance())
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

    fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    override fun reloadView() {
        TODO("Not yet implemented")
    }
}
