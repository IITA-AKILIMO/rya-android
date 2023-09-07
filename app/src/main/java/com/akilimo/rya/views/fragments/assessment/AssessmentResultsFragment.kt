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
import com.akilimo.rya.utils.FieldComputations
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

    private val fc = FieldComputations()
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

        binding.btnRetry.setOnClickListener { refreshEstimateData() }
    }

    override fun onSelected() {
        super.onSelected()
        refreshEstimateData()
    }

    private fun refreshEstimateData() {
        val userInfo = database?.userInfoDao()?.findOne()
        val fieldInfo = database?.fieldInfoDao()?.findOne()
        val yieldPrecision = database?.yieldPrecisionDao()?.findOne()
        val triArea = fc.triangleArea(sideLength = 5.0)

        val plantCount = yieldPrecision?.plantCount

        val triangleOneRootMass =
            database?.plantTriangleDao()?.findPlantRootMass("One", limit = plantCount!!)
        val triangleTwoRootMass =
            database?.plantTriangleDao()?.findPlantRootMass("Two", limit = plantCount!!)
        val triangleThreeRootMass =
            database?.plantTriangleDao()?.findPlantRootMass("Three", limit = plantCount!!)


        val rootWeightMeanTri1 = fc.computeAverage(triangleOneRootMass!!.toDoubleArray())
        val rootYieldPerTonneTri1 = fc.rootYieldPerTonne(
            plantCount = fieldInfo!!.triangle1PlantCount,
            triangleArea = triArea,
            meanRootWeightKg = rootWeightMeanTri1
        )

        val rootWeightMeanTri2 = fc.computeAverage(triangleTwoRootMass!!.toDoubleArray())
        val rootYieldPerTonneTri2 = fc.rootYieldPerTonne(
            plantCount = fieldInfo.triangle2PlantCount,
            triangleArea = triArea,
            meanRootWeightKg = rootWeightMeanTri2
        )

        val rootWeightMeanTri3 = fc.computeAverage(triangleThreeRootMass!!.toDoubleArray())
        val rootYieldPerTonneTri3 = fc.rootYieldPerTonne(
            plantCount = fieldInfo.triangle3PlantCount,
            triangleArea = triArea,
            meanRootWeightKg = rootWeightMeanTri3
        )

        val rootTonneYields = doubleArrayOf(
            rootYieldPerTonneTri1,
            rootYieldPerTonneTri2,
            rootYieldPerTonneTri3
        )

        val averageTonneYield = fc.computeAverage(rootTonneYields)
        val roundedYieldHa = fc.roundToNDecimalPlaces(averageTonneYield, 1)
        val rootYieldStandardDev = fc.computeSampleStandardDeviation(yieldValues = rootTonneYields)
        val roundedSdHa = fc.roundToNDecimalPlaces(rootYieldStandardDev, 2)

    }

    private fun refreshEstimateDataOld() {
        val userInfo = database?.userInfoDao()?.findOne()
        val fieldInfo = database?.fieldInfoDao()?.findOne()
        val yieldPrecision = database?.yieldPrecisionDao()?.findOne()

        val plantCount = yieldPrecision?.plantCount

        val triangleOneRootMass =
            database?.plantTriangleDao()?.findPlantRootMass("One", limit = plantCount!!)
        val triangleTwoRootMass =
            database?.plantTriangleDao()?.findPlantRootMass("Two", limit = plantCount!!)
        val triangleThreeRootMass =
            database?.plantTriangleDao()?.findPlantRootMass("Three", limit = plantCount!!)


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

        with(binding) {

            if (!shimmerViewContainer.isShimmerStarted) {
                shimmerViewContainer.startShimmer()
            }

            shimmerWidgetGroup.visibility = View.VISIBLE
            errorWidgetGroup.visibility = View.GONE
            widgetGroup.visibility = View.GONE


            val estimate = apiInterface?.computeEstimate(yieldEstimate)
            estimate?.enqueue(object : Callback<YieldEstimate> {
                override fun onResponse(
                    call: Call<YieldEstimate>, response: Response<YieldEstimate>
                ) {
                    if (response.isSuccessful) {
                        val myResp: YieldEstimate? = response.body()
                        if (myResp != null) {
                            val results = DoubleArray(myResp.result.size)
                            for ((index, result) in myResp.result.withIndex()) {
                                results[index] = result
                            }
                            val theMedian = median(results)
                            tonnageResults.text = "Estimated production:\n$theMedian tonnes"
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
                    } else {
                        shimmerWidgetGroup.visibility = View.GONE
                        errorWidgetGroup.visibility = View.VISIBLE
                        if (!lottieAnimation.isAnimating) {
                            lottieAnimation.playAnimation()
                        }
                        Toast.makeText(ctx, response.message(), Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<YieldEstimate>, t: Throwable) {
                    shimmerWidgetGroup.visibility = View.GONE
                    errorWidgetGroup.visibility = View.VISIBLE
                    if (!lottieAnimation.isAnimating) {
                        lottieAnimation.playAnimation()
                    }
                    Toast.makeText(ctx, t.message, Toast.LENGTH_SHORT).show()
                }

            })
        }
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
                } else {
                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerWidgetGroup.visibility = View.GONE
                    binding.errorWidgetGroup.visibility = View.VISIBLE
                    if (!binding.lottieAnimation.isAnimating) {
                        binding.lottieAnimation.playAnimation()
                    }
                    Toast.makeText(ctx, response.message(), Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<GeneratePlotResp>, t: Throwable) {
                binding.shimmerViewContainer.stopShimmer()
                binding.shimmerWidgetGroup.visibility = View.GONE
                binding.errorWidgetGroup.visibility = View.VISIBLE
                if (!binding.lottieAnimation.isAnimating) {
                    binding.lottieAnimation.playAnimation()
                }

                Toast.makeText(ctx, t.message, Toast.LENGTH_SHORT).show()
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
                    binding.shimmerWidgetGroup.visibility = View.GONE
                    binding.widgetGroup.visibility = View.VISIBLE
                } else {
                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerWidgetGroup.visibility = View.GONE
                    binding.errorWidgetGroup.visibility = View.VISIBLE
                    if (!binding.lottieAnimation.isAnimating) {
                        binding.lottieAnimation.animate()
                    }
                    Toast.makeText(ctx, "Unable to load plot image", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, throwable: Throwable) {
                binding.shimmerViewContainer.stopShimmer()
                binding.shimmerWidgetGroup.visibility = View.GONE
                binding.errorWidgetGroup.visibility = View.VISIBLE
                if (!binding.lottieAnimation.isAnimating) {
                    binding.lottieAnimation.playAnimation()
                }
                Toast.makeText(ctx, throwable.message, Toast.LENGTH_SHORT).show()
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
