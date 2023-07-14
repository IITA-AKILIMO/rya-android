package com.akilimo.rya.utils

import org.junit.Assert.*
import org.junit.Test

internal class FieldComputationsTest {
    private val fc = FieldComputations()

    val hectareToAcre = 2.47105
    val landSizeHa = 10.0

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
    fun compute_root_yield_per_hectare_and_acre() {
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

        //average tonne yield RY
        val averageTonneYield = fc.computeAverage(rootTonneYields)
        val roundedYield = fc.roundToNDecimalPlaces(averageTonneYield, 1)
        assertEquals(26.7, roundedYield, 0.0)

        //root yield standard deviation RySd
        val rootYieldStandardDev = fc.computeSampleStandardDeviation(yieldValues = rootTonneYields)
        val roundedSd = fc.roundToNDecimalPlaces(rootYieldStandardDev, 2)
        assertEquals(4.44, roundedSd, 0.0)

        val rootYieldLowerConfidenceBound = fc.computeLowerConfidenceBound(
            averageTonneYield,
            rootYieldStandardDev
        )
        val roundedLowerConfidence = fc.roundToNDecimalPlaces(rootYieldLowerConfidenceBound, 1)
        assertEquals(17.8, roundedLowerConfidence, 0.0)

        val negativeLower = fc.computeLowerConfidenceBound(
            1.5,
            4.8
        )
        assertEquals(0.0, negativeLower, 0.0)

        val rootYieldUpperConfidenceBound = fc.computeUpperConfidenceBound(
            averageTonneYield,
            rootYieldStandardDev
        )
        val roundedUpperConfidence = fc.roundToNDecimalPlaces(rootYieldUpperConfidenceBound, 1)
        assertEquals(35.5, roundedUpperConfidence, 0.0)

        val totalRootProduction = fc.computeTotalRootProduction(
            fieldSizeHa = landSizeHa,
            rootYield = averageTonneYield
        )

        val roundedTotalRootProduction = fc.roundToNDecimalPlaces(totalRootProduction, 0)
        assertEquals(267.0, roundedTotalRootProduction, 0.0)

        val rootProductionUpperCB = fc.computeRootProductionConfidenceBound(
            fieldSizeHa = landSizeHa,
            rootYieldLowerCB = rootYieldLowerConfidenceBound
        )
        val roundedRootProductionUpperCB = fc.roundToNDecimalPlaces(rootProductionUpperCB, 0)
        assertEquals(178.0, roundedRootProductionUpperCB, 0.0)

        val rootProductionLowerCB = fc.computeRootProductionConfidenceBound(
            fieldSizeHa = landSizeHa,
            rootYieldLowerCB = rootYieldUpperConfidenceBound
        )
        val roundedRootProductionLowerCB = fc.roundToNDecimalPlaces(rootProductionLowerCB, 0)
        assertEquals(355.0, roundedRootProductionLowerCB, 0.0)

    }
}
