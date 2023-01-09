package com.akilimo.rya.views.fragments.assessment

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.databinding.FragmentAssessmentResultsBinding
import com.akilimo.rya.entities.EstimateResultsEntity
import com.akilimo.rya.entities.FieldInfoEntity
import com.akilimo.rya.entities.UserInfoEntity
import com.akilimo.rya.enums.PrecisionTypes
import com.akilimo.rya.rest.ApiInterface
import com.akilimo.rya.rest.request.GeneratePlot
import com.akilimo.rya.rest.request.RyaEstimate
import com.akilimo.rya.rest.request.RyaPlot
import com.akilimo.rya.rest.response.GeneratePlotResp
import com.akilimo.rya.rest.response.YieldEstimate
import com.akilimo.rya.views.fragments.BaseStepFragment
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.math.roundToInt


/**
 * A simple [Fragment] subclass.
 * Use the [AssessmentResultsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AssessmentResultsFragment(private val ryaEndpoint: String) : BaseStepFragment() {

    private var ctx: Context? = null
    private var _binding: FragmentAssessmentResultsBinding? = null
    private var database: AppDatabase? = null
    private var apiInterface: ApiInterface? = null
    private val guid = "test"

    private val binding get() = _binding!!


    companion object {
        @JvmStatic
        fun newInstance(ryaEndpoint: String) = AssessmentResultsFragment(ryaEndpoint)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.ctx = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = AppDatabase.getDatabase(ctx!!)
    }

    override fun loadFragmentLayout(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssessmentResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiInterface = ApiInterface.create(ryaEndpoint)
        refreshEstimateData()
    }

    override fun onSelected() {
        super.onSelected()
        refreshEstimateData()
    }

    private fun refreshEstimateData() {
        val userInfo = database?.userInfoDao()?.findOne()
        val fieldInfo = database?.fieldInfoDao()?.findOne()
        val yieldPrecision = database?.yieldPrecisionDao()?.findOne()

        val plantCount = yieldPrecision?.plantCount

        val triangleOneRootMass =
            database?.plantTriangleDao()?.findPlantRootMass("one", limit = plantCount!!)
        val triangleTwoRootMass =
            database?.plantTriangleDao()?.findPlantRootMass("two", limit = plantCount!!)
        val triangleThreeRootMass =
            database?.plantTriangleDao()?.findPlantRootMass("three", limit = plantCount!!)


        val plantRootMass: MutableList<Double> = ArrayList()
        plantRootMass.addAll(triangleOneRootMass!!)
        plantRootMass.addAll(triangleTwoRootMass!!)
        plantRootMass.addAll(triangleThreeRootMass!!)

        val estimates = RyaEstimate(
            fieldArea = fieldInfo?.fieldSize!!,
            areaUnit = userInfo?.areaUnit!!,
            precisionType = PrecisionTypes.valueOf(yieldPrecision?.yieldPrecision!!),
            plantCounts = listOf(
                fieldInfo.triangle1PlantCount,
                fieldInfo.triangle2PlantCount,
                fieldInfo.triangle3PlantCount
            ),
            plantRms = plantRootMass,
        )

        computeYieldEstimate(userInfo = userInfo, yieldEstimate = estimates, fieldInfo = fieldInfo)
    }

    private fun computeYieldEstimate(
        userInfo: UserInfoEntity,
        yieldEstimate: RyaEstimate,
        fieldInfo: FieldInfoEntity
    ) {
        val estimate = apiInterface?.computeEstimate(yieldEstimate)
        estimate?.enqueue(object : Callback<YieldEstimate> {
            override fun onResponse(call: Call<YieldEstimate>, response: Response<YieldEstimate>) {
                if (response.isSuccessful) {
                    val myResp: YieldEstimate? = response.body()
                    if (myResp != null) {
                        val totalElements = myResp.result.size
                        val results = DoubleArray(totalElements)
                        for ((index, result) in myResp.result.withIndex()) {
                            results[index] = result
                        }
                        val theMedian = median(results)
                        binding.tonnageResults.text = "Estimated production:\n$theMedian tonnes"
                        //next generate the plots
                        val estimateResults = EstimateResultsEntity(
                            currency = userInfo.currencyCode,
                            tonnagePrice = fieldInfo.sellingPrice,
                            tonnageEstimate = theMedian.toDouble(),
                            fieldArea = fieldInfo.fieldSize,
                            fileNameFull = "",
                            fileNameLean = ""
                        )

                        generatePlots(fieldInfo, myResp.result, estimateResults)
                    }
                }
            }

            override fun onFailure(call: Call<YieldEstimate>, t: Throwable) {
                Toast.makeText(ctx, "Unable to compute the estimate", Toast.LENGTH_SHORT).show()
            }

        })
    }


    private fun generatePlots(
        fieldInfo: FieldInfoEntity, result: List<Double>,
        estimateResults: EstimateResultsEntity
    ) {
        val generatePlot = GeneratePlot(
            results = result,
            fieldArea = fieldInfo.fieldSize,
            areaUnit = fieldInfo.areaUnit,
            fileName = guid.toString()
        )
        if (!binding.shimmerViewContainer.isShimmerStarted) {
            binding.shimmerViewContainer.startShimmer()
        }
        binding.shimmerViewContainer.visibility = View.VISIBLE
        binding.widgetGroup.visibility = View.GONE

        val plots = apiInterface?.generatePlots(generatePlot)
        plots?.enqueue(object : Callback<GeneratePlotResp> {
            override fun onResponse(
                call: Call<GeneratePlotResp>, response: Response<GeneratePlotResp>
            ) {
                if (response.isSuccessful) {
                    val plotBody = response.body()
                    if (plotBody != null) {
                        val plotImages = plotBody.plotImages[0]
                        //Save to the database
                        estimateResults.fileNameFull = plotImages.fileNameFull
                        estimateResults.fileNameLean = plotImages.fileNameLean
                        //save the data
                        val data = database?.estimateResultsDao()?.findOne()
                        if (data != null) {
                            database?.estimateResultsDao()?.deleteEstimate(data)
                        }
                        database?.estimateResultsDao()?.insert(estimateResults)
                        renderPlot(RyaPlot(plotImages.fileNameFull))
                    }
                }
            }

            override fun onFailure(call: Call<GeneratePlotResp>, t: Throwable) {
                Toast.makeText(
                    ctx, "Unable to load the plots, please try again", Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun renderPlot(ryaPlot: RyaPlot) {
        val imageView = binding.yieldPlotImage


        val plotReader = apiInterface?.readPlot(ryaPlot)

        plotReader?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.body() != null) {
                    val bytes = response.body()!!.bytes()
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//                  imageView.setImage(ImageSource.bitmap(bitmap))
                    imageView.setImageBitmap(bitmap)
                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerViewContainer.visibility = View.GONE
                    binding.widgetGroup.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<ResponseBody>, throwable: Throwable) {
                Toast.makeText(
                    ctx, "Unable to load plot data", Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    override fun onResume() {
        super.onResume()
        binding.shimmerViewContainer.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        binding.shimmerViewContainer.stopShimmer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun median(results: DoubleArray): Int {
        val totalElements = results.size
        // sort array
        Arrays.sort(results)
        // get count of scores
        // check if total number of scores is even
        val median: Double = if (totalElements % 2 == 0) {
            val sumOfMiddleElements =
                results[totalElements / 2].plus(results[totalElements / 2 - 1])
            // calculate average of middle elements
            sumOfMiddleElements / 2
        } else {
            results[totalElements / 2]
        }
        return median.roundToInt()
    }

}
