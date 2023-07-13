package com.akilimo.rya.utils

import org.junit.Assert.*
import org.junit.Test

internal class FieldComputationsTest {
    private val fc = FieldComputations()

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
        val rootWeightMean = fc.averageRootWeight(rootWeight)

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
        val rootWeightMean = fc.averageRootWeight(rootWeight)

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
        val rootWeightMean = fc.averageRootWeight(rootWeight)

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
}