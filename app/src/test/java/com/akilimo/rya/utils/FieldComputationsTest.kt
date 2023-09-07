package com.akilimo.rya.utils

import org.junit.Assert.*
import org.junit.Test

internal class FieldComputationsTest {
    private val fc = FieldComputations()

    private val hectareToAcre = 2.47105
    private val landSizeHa = 10.0
    private val rootUnitPrice = 30000.0

    @Test
    fun calculate_triangle_area() {
        val triArea = fc.triangleArea(sideLength = 5.0)
        assertNotEquals(0, triArea)

        val roundedTriArea = fc.roundToNDecimalPlaces(triArea)
        assertEquals(10.83, roundedTriArea, 0.0)
    }

    @Test
    fun calculate_average_root_weight_tr1() {
        val triArea = fc.triangleArea(sideLength = 5.0)
        val rootWeight = doubleArrayOf(2.5, 1.9, 3.0)
        val rootWeightMean = fc.computeAverage(rootWeight)

        val roundedRootWeight = fc.roundToNDecimalPlaces(rootWeightMean, 6)
        assertEquals(2.466667, roundedRootWeight, 0.0)


        val rootYieldPerTonne = fc.rootYieldPerTonne(
            plantCount = 13,
            triangleArea = triArea,
            meanRootWeightKg = rootWeightMean
        )
        val roundedRootYield = fc.roundToNDecimalPlaces(rootYieldPerTonne, 5)
        assertEquals(29.62192, roundedRootYield, 0.0)
    }

    @Test
    fun calculate_average_root_weight_tr2() {
        val triArea = fc.triangleArea(sideLength = 5.0)
        val rootWeight = doubleArrayOf(2.0, 1.0, 4.0)
        val rootWeightMean = fc.computeAverage(rootWeight)

        val roundedValue = fc.roundToNDecimalPlaces(rootWeightMean, 6)
        assertEquals(2.333333, roundedValue, 0.0)

        val rootYieldPerTonne = fc.rootYieldPerTonne(
            plantCount = 10,
            triangleArea = triArea,
            meanRootWeightKg = rootWeightMean
        )
        val roundedRootYield = fc.roundToNDecimalPlaces(rootYieldPerTonne, 5)
        assertEquals(21.55441, roundedRootYield, 0.0)

    }

    @Test
    fun calculate_average_root_weight_tr3() {
        val triArea = fc.triangleArea(sideLength = 5.0)
        val rootWeight = doubleArrayOf(3.0, 2.5, 3.0)
        val rootWeightMean = fc.computeAverage(rootWeight)

        val roundedValue = fc.roundToNDecimalPlaces(rootWeightMean, 9)
        assertEquals(2.833333333, roundedValue, 0.0)

        val rootYieldPerTonne = fc.rootYieldPerTonne(
            plantCount = 11,
            triangleArea = triArea,
            meanRootWeightKg = rootWeightMean
        )
        val roundedRootYield = fc.roundToNDecimalPlaces(rootYieldPerTonne, 5)
        assertEquals(28.79053, roundedRootYield, 0.0)
    }

    @Test
    fun compute_root_yield_per_hectare_acre() {
        val triArea = fc.triangleArea(sideLength = 5.0)
        //First triangle
        val rootWeightTr1 = doubleArrayOf(2.5, 1.9, 3.0)
        val rootWeightMeanTri1 = fc.computeAverage(rootWeightTr1)
        val rootYieldPerTonneTri1 = fc.rootYieldPerTonne(
            plantCount = 13,
            triangleArea = triArea,
            meanRootWeightKg = rootWeightMeanTri1
        )

        //second triangle
        val rootWeightTri2 = doubleArrayOf(2.0, 1.0, 4.0)
        val rootWeightMeanTri2 = fc.computeAverage(rootWeightTri2)
        val rootYieldPerTonneTri2 = fc.rootYieldPerTonne(
            plantCount = 10,
            triangleArea = triArea,
            meanRootWeightKg = rootWeightMeanTri2
        )

        //Third triangle
        val rootWeightTri3 = doubleArrayOf(3.0, 2.5, 3.0)
        val rootWeightMeanTri3 = fc.computeAverage(rootWeightTri3)
        val rootYieldPerTonneTri3 = fc.rootYieldPerTonne(
            plantCount = 11,
            triangleArea = triArea,
            meanRootWeightKg = rootWeightMeanTri3
        )

        val rootTonneYields = doubleArrayOf(
            rootYieldPerTonneTri1,
            rootYieldPerTonneTri2,
            rootYieldPerTonneTri3
        )
        //Post conversion calculations begin below here based on field unit


        //average tonne yield RY
        val averageTonneYield = fc.computeAverage(rootTonneYields)
        val roundedYieldHa = fc.roundToNDecimalPlaces(averageTonneYield, 1)
        val roundedYieldAcre = fc.roundToNDecimalPlaces(averageTonneYield / hectareToAcre, 1)
        assertEquals(26.7, roundedYieldHa, 0.0)
        assertEquals(10.8, roundedYieldAcre, 0.0)

        //root yield standard deviation RySd
        val rootYieldStandardDev = fc.computeSampleStandardDeviation(yieldValues = rootTonneYields)
        val roundedSdHa = fc.roundToNDecimalPlaces(rootYieldStandardDev, 2)
        val roundedSdAcre = fc.roundToNDecimalPlaces(rootYieldStandardDev / hectareToAcre, 2)
        assertEquals(4.44, roundedSdHa, 0.0)
        assertEquals(1.80, roundedSdAcre, 0.0)

        val rootYieldLowerConfidenceBound = fc.computeLowerConfidenceBound(
            averageTonneYield,
            rootYieldStandardDev
        )
        val roundedLowerConfidenceHa = fc.roundToNDecimalPlaces(rootYieldLowerConfidenceBound, 1)
        val roundedLowerConfidenceAcre = fc.roundToNDecimalPlaces(
            rootYieldLowerConfidenceBound / hectareToAcre,
            1
        )
        assertEquals(17.8, roundedLowerConfidenceHa, 0.0)
        assertEquals(7.2, roundedLowerConfidenceAcre, 0.0)

        val negativeLower = fc.computeLowerConfidenceBound(1.5, 4.8)
        assertEquals(0.0, negativeLower, 0.0)

        val rootYieldUpperConfidenceBound = fc.computeUpperConfidenceBound(
            averageTonneYield,
            rootYieldStandardDev
        )
        val roundedUpperConfidenceHa = fc.roundToNDecimalPlaces(rootYieldUpperConfidenceBound, 1)
        val roundedUpperConfidenceAcre = fc.roundToNDecimalPlaces(
            rootYieldUpperConfidenceBound / hectareToAcre,
            1
        )
        assertEquals(35.5, roundedUpperConfidenceHa, 0.0)
        assertEquals(14.4, roundedUpperConfidenceAcre, 0.0)

        val totalRootProduction = fc.computeTotalRootProduction(
            fieldSize = landSizeHa,
            rootYield = averageTonneYield
        )

        val roundedTotalRootProductionHa = fc.roundToNDecimalPlaces(totalRootProduction, 0)
        val roundedTotalRootProductionAcre = fc.roundToNDecimalPlaces(
            totalRootProduction / hectareToAcre, 0
        )
        assertEquals(267.0, roundedTotalRootProductionHa, 0.0)
        assertEquals(108.0, roundedTotalRootProductionAcre, 0.0)

        val rootProductionLowerCB = fc.computeRootProductionConfidenceBound(
            fieldSize = landSizeHa,
            rootYieldLowerCB = rootYieldLowerConfidenceBound
        )
        val roundedRootProductionLowerCBHa = fc.roundToNDecimalPlaces(rootProductionLowerCB, 0)
        val roundedRootProductionLowerCBAcre = fc.roundToNDecimalPlaces(
            rootProductionLowerCB / hectareToAcre, 0
        )
        assertEquals(178.0, roundedRootProductionLowerCBHa, 0.0)
        assertEquals(72.0, roundedRootProductionLowerCBAcre, 0.0)

        val rootProductionUpperCB = fc.computeRootProductionConfidenceBound(
            fieldSize = landSizeHa,
            rootYieldLowerCB = rootYieldUpperConfidenceBound
        )
        val roundedRootProductionUpperCBHa = fc.roundToNDecimalPlaces(rootProductionUpperCB, 0)
        val roundedRootProductionUpperCBAcre = fc.roundToNDecimalPlaces(
            rootProductionUpperCB / hectareToAcre, 0
        )
        assertEquals(355.0, roundedRootProductionUpperCBHa, 0.0)
        assertEquals(144.0, roundedRootProductionUpperCBAcre, 0.0)


        val totalCropValue = fc.computeTotalCropValue(
            totalRootProd = roundedTotalRootProductionHa,
            rootUnitPrice = rootUnitPrice
        )

        assertEquals(8010000.00, totalCropValue, 0.0)

        val totalCropValueUpperBound = fc.computeTotalCropValue(
            totalRootProd = roundedRootProductionUpperCBHa,
            rootUnitPrice = rootUnitPrice
        )
        val roundedTotalCropValueUpper = fc.roundToNDecimalPlaces(totalCropValueUpperBound)
        assertEquals(10650000.00, roundedTotalCropValueUpper, 0.0)

        val totalCropValueLowerBound = fc.computeTotalCropValue(
            totalRootProd = roundedRootProductionLowerCBHa,
            rootUnitPrice = rootUnitPrice
        )

        val roundedTotalCropValueLower = fc.roundToNDecimalPlaces(totalCropValueLowerBound)
        assertEquals(5340000.00, roundedTotalCropValueLower, 0.0)

        //now generate the final text

        val recTextHa =
            "Your expected yield is $roundedYieldHa t/acre. " +
                    "Production is $totalRootProduction tonnes " +
                    "(between $rootProductionLowerCB and $rootProductionUpperCB), " +
                    "and estimated value is $totalCropValue Naira."

        val recTextAcre =
            "Your expected yield is 10.8 t/acre. Production is 21.6 tonnes (between 14.4 and 28.8), and estimated value is 647,000 Naira."
        assertEquals(recTextAcre, recTextHa)

    }
}
