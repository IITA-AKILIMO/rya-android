package com.akilimo.rya.utils

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.pow
import kotlin.math.sqrt

class FieldComputations {

    var df: DecimalFormat = DecimalFormat("#.00")

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

    fun computeAverage(arrayDouble: DoubleArray): Double {
        var sum = 0.0
        for (doubleValue in arrayDouble) {
            sum += doubleValue
        }

        return sum / arrayDouble.size
    }

    fun rootYieldPerTonne(plantCount: Int, triangleArea: Double, meanRootWeightKg: Double): Double {
        val const = 10 //gotten from conversion of meters and KG
        return (meanRootWeightKg * plantCount) / triangleArea * const
    }

    fun computeSampleStandardDeviation(yieldValues: DoubleArray): Double {
        val average = computeAverage(yieldValues)
        val sumOfSquareDiffs = yieldValues.sumOf { (it - average).pow(2) }

        val variance = sumOfSquareDiffs / (yieldValues.size - 1)

        return sqrt(variance)
    }

    fun computeUpperConfidenceBound(averageTonneYield: Double, sDev: Double): Double {
        val upperConfidence = averageTonneYield + (2 * sDev)
        if (upperConfidence <= 0.0) {
            return 0.0
        }
        return upperConfidence
    }

    fun computeLowerConfidenceBound(averageTonneYield: Double, sDev: Double): Double {
        val upperConfidence = averageTonneYield - (2 * sDev)
        if (upperConfidence <= 0.0) {
            return 0.0
        }
        return upperConfidence
    }

    fun computeTotalRootProduction(fieldSize: Double, rootYield: Double): Double {
        return fieldSize * rootYield
    }

    fun computeRootProductionConfidenceBound(
        fieldSize: Double,
        rootYieldLowerCB: Double
    ): Double {
        return fieldSize * rootYieldLowerCB
    }

    fun computeTotalCropValue(totalRootProd: Double, rootUnitPrice: Double): Double {
        return totalRootProd * rootUnitPrice
    }

    fun formatToString(value: Double, format: String = "#,##0"): String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        return DecimalFormat(format).format(value)
    }
}
