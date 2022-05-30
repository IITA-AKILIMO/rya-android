package com.akilimo.rya.views.fragments.assessment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.databinding.FragmentAssessmentResultsBinding
import com.akilimo.rya.entities.FieldInfoEntity
import com.akilimo.rya.enums.PrecisionTypes
import com.akilimo.rya.rest.ApiInterface
import com.akilimo.rya.rest.request.GeneratePlot
import com.akilimo.rya.rest.request.RyaEstimate
import com.akilimo.rya.rest.response.GeneratePlotResp
import com.akilimo.rya.rest.response.YieldEstimate
import com.akilimo.rya.views.fragments.BaseStepFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger
import java.util.*
import kotlin.math.roundToInt


private const val END_POINT = "rya_endpoint"

/**
 * A simple [Fragment] subclass.
 * Use the [AssessmentResultsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AssessmentResultsFragment(val ryaEndpoint: String) : BaseStepFragment() {

    private var ctx: Context? = null
    private var _binding: FragmentAssessmentResultsBinding? = null
    private var database: AppDatabase? = null
    private var apiInterface: ApiInterface? = null
    private val guid = "test"

    private val binding get() = _binding!!


    companion object {
        @JvmStatic
        fun newInstance(ryaEndpoint: String) =
            AssessmentResultsFragment(ryaEndpoint)
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssessmentResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onSelected() {
        super.onSelected()
        //load database data

        apiInterface = ApiInterface.create(ryaEndpoint)

        val fieldInfo = database?.fieldInfoDao()?.findOne()
        val yieldPrecision = database?.yieldPrecisionDao()?.findOne()

        val plantCount = yieldPrecision?.plantCount
        val plantPerTriangle = database?.plantTriangleDao()?.findPlantsPerTriangle()
        val plantRootMass = database?.plantTriangleDao()?.findPlantRootMass(limit = plantCount!!)

        val estimates = RyaEstimate(
            fieldArea = fieldInfo?.fieldSize!!,
            areaUnit = fieldInfo.areaUnit,
            precisionType = PrecisionTypes.valueOf(yieldPrecision?.yieldPrecision!!),
            plantCounts = plantPerTriangle!!,
            plantRms = plantRootMass!!,
        )


        val estimate = apiInterface?.computeEstimate(estimates)
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
                        binding.tonnageResults.text = "Estimated production: $theMedian tonnes"
                        //next generate the plots
                        generatePlots(fieldInfo, myResp.result)
                    }
                }
            }

            override fun onFailure(call: Call<YieldEstimate>, t: Throwable) {
                Toast.makeText(ctx, "Unable to compute the estimate", Toast.LENGTH_SHORT).show();
            }

        })
    }

    private fun generatePlots(fieldInfo: FieldInfoEntity, result: List<Double>) {
        val generatePlot = GeneratePlot(
            results = result,
            fieldArea = fieldInfo.fieldSize,
            areaUnit = fieldInfo.areaUnit,
            fileName = guid.toString()
        )
        val plots = apiInterface?.generatePlots(generatePlot)
        plots?.enqueue(object : Callback<GeneratePlotResp> {
            override fun onResponse(
                call: Call<GeneratePlotResp>,
                response: Response<GeneratePlotResp>
            ) {
                if (response.isSuccessful) {
                    val k = response.body()
                }
            }

            override fun onFailure(call: Call<GeneratePlotResp>, t: Throwable) {
                Toast.makeText(
                    ctx,
                    "Unable to load the plots, please try again",
                    Toast.LENGTH_SHORT
                ).show();
            }

        })
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
            // get the middle element
            results[totalElements / 2]
        }
        return median.roundToInt()
    }

}
