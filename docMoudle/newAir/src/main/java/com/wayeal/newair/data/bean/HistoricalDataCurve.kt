data class HistoricalDataCurve(
        val data: ArrayList<HistoricalCurvePoint>,
        val result: Int,
        val status: Int,
        val total: Int
)

data class HistoricalCurvePoint(
        val data: ArrayList<HistoricalDataXY>,
        val key: String,
        val rangehigh: String,
        val rangelow: String
)

data class HistoricalDataXY(
        val x: String,
        val y: String
)