package com.akilimo.rya.views.fragments.assessment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akilimo.rya.AppDatabase
import com.akilimo.rya.databinding.FragmentAssessmentResultsBinding
import com.akilimo.rya.rest.ApiInterface
import com.akilimo.rya.utils.FieldComputations
import com.akilimo.rya.views.fragments.BaseStepFragment


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

    private val fc = FieldComputations()
    private var recText = ""
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

        binding.btnRetry.setOnClickListener {
            val uri = Uri.parse("smsto:")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.putExtra("sms_body", recText)
            startActivity(intent)
        }
    }

    override fun onSelected() {
        super.onSelected()
        refreshEstimateData()
    }

    private fun refreshEstimateData() {
        val userInfo = database?.userInfoDao()?.findOne()
        val fieldInfo = database?.fieldInfoDao()?.findOne()
        val yieldPrecision = database?.yieldPrecisionDao()?.findOne()

        val landSizeHa = fieldInfo?.fieldSize!!
        val rootUnitPrice = fieldInfo.sellingPrice
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

        val rootYieldLowerConfidenceBound = fc.computeLowerConfidenceBound(
            averageTonneYield,
            rootYieldStandardDev
        )
        val roundedLowerConfidenceHa = fc.roundToNDecimalPlaces(rootYieldLowerConfidenceBound, 1)

        val rootYieldUpperConfidenceBound = fc.computeUpperConfidenceBound(
            averageTonneYield,
            rootYieldStandardDev
        )
        val roundedUpperConfidenceHa = fc.roundToNDecimalPlaces(rootYieldUpperConfidenceBound, 1)

        val totalRootProduction = fc.computeTotalRootProduction(
            fieldSize = landSizeHa,
            rootYield = averageTonneYield
        )

        val roundedTotalRootProductionHa = fc.roundToNDecimalPlaces(totalRootProduction, 0)

        val rootProductionLowerCB = fc.computeRootProductionConfidenceBound(
            fieldSize = landSizeHa,
            rootYieldLowerCB = rootYieldLowerConfidenceBound
        )
        val roundedRootProductionLowerCBHa = fc.roundToNDecimalPlaces(rootProductionLowerCB, 0)

        val rootProductionUpperCB = fc.computeRootProductionConfidenceBound(
            fieldSize = landSizeHa,
            rootYieldLowerCB = rootYieldUpperConfidenceBound
        )
        val roundedRootProductionUpperCBHa = fc.roundToNDecimalPlaces(rootProductionUpperCB, 0)

        val totalCropValue = fc.computeTotalCropValue(
            totalRootProd = roundedTotalRootProductionHa,
            rootUnitPrice = rootUnitPrice
        )

        val totalCropValueUpperBound = fc.computeTotalCropValue(
            totalRootProd = roundedRootProductionUpperCBHa,
            rootUnitPrice = rootUnitPrice
        )
        val roundedTotalCropValueUpper = fc.roundToNDecimalPlaces(totalCropValueUpperBound)

        val totalCropValueLowerBound = fc.computeTotalCropValue(
            totalRootProd = roundedRootProductionLowerCBHa,
            rootUnitPrice = rootUnitPrice
        )

        val roundedTotalCropValueLower = fc.roundToNDecimalPlaces(totalCropValueLowerBound)


        recText =
            generateAssessmentText(
                areaUnit = fieldInfo.areaUnit,
                currencyName = userInfo?.currencyName,
                yieldPerHectare = averageTonneYield,
                totalCropValue = totalCropValue,
                totalRootProduction = totalRootProduction,
                rootProductionLowerCB = rootProductionLowerCB,
                rootProductionUpperCB = rootProductionUpperCB
            )
        with(binding) {
            tonnageResults.text = recText
        }

    }

    private fun generateAssessmentText(
        areaUnit: String,
        currencyName: String?,
        yieldPerHectare: Double,
        totalCropValue: Double,
        totalRootProduction: Double,
        rootProductionLowerCB: Double,
        rootProductionUpperCB: Double
    ): String {


        val yieldPerHectareRounded = fc.roundToNDecimalPlaces(yieldPerHectare)
        val totalRootProductionRounded = fc.roundToNDecimalPlaces(totalRootProduction, 1)
        val rootProductionLowerCBRounded = fc.roundToNDecimalPlaces(rootProductionLowerCB, 1)
        val rootProductionUpperCBRounded = fc.roundToNDecimalPlaces(rootProductionUpperCB, 1)
        val totalCropValueRounded = fc.formatToString(totalCropValue)

        return "Your expected yield is $yieldPerHectareRounded t/$areaUnit. " +
                "Production is $totalRootProductionRounded tonnes " +
                "(between $rootProductionLowerCBRounded and $rootProductionUpperCBRounded), " +
                "and estimated value is $totalCropValueRounded $currencyName."

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
