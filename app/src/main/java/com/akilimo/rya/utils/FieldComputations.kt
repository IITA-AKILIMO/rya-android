package com.akilimo.rya.utils

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.sqrt

class FieldComputations {

    /**
     * Calculates the area of a triangle with equilateral sides.
     * @param sideLength The length of side A of the triangle.
     * @return The calculated area of the triangle.
     */
    fun triangleArea(sideLength: Double): Double {
        return this.triangleArea(sideLength, sideLength, sideLength)
    }


    /**
     * Calculates the area of a triangle using Heron's formula.
     * @param sideA The length of side A of the triangle.
     * @param sideB The length of side B of the triangle.
     * @param sideC The length of side C of the triangle.
     * @return The calculated area of the triangle.
     */
    private fun triangleArea(sideA: Double, sideB: Double, sideC: Double): Double {
        val semiPerimeter = (sideA + sideB + sideC) / 2

        return sqrt(semiPerimeter * (semiPerimeter - sideA) * (semiPerimeter - sideB) * (semiPerimeter - sideC))
    }

    fun roundToNDecimalPlaces(numberToRound: Double, decimalPlaces: Int = 2): Double {
        val bigDecimal = BigDecimal(numberToRound)
        return bigDecimal.setScale(decimalPlaces, RoundingMode.HALF_EVEN).toDouble()
    }

    fun averageRootWeight(rootWeights: DoubleArray): Double {
        var weightSum = 0.0
        for (rootWeight in rootWeights) {
            weightSum += rootWeight
        }

        return weightSum / rootWeights.size
    }

    fun rootYieldPerTonne(plantCount: Int, triangleArea: Double, meanRootWeightKg: Double): Double {
        val const = 10 //gotten from conversion of meters and KG
        return (meanRootWeightKg * plantCount) / triangleArea * const
    }
}