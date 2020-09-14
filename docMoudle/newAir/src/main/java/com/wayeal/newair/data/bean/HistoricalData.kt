import com.google.gson.annotations.SerializedName

data class HistoricalData(
    val `data`: List<HistoricalDatas>,
    val result: Int,
    val status: Int,
    val total: Int
)

data class HistoricalDatas(
    @SerializedName("0103-Avg")
    val hd0103Avg: String,
    @SerializedName("103-Avg")
    val hd103Avg: String,
    @SerializedName("107-Avg")
    val hd107Avg: String,
    @SerializedName("126-Avg")
    val hd126Avg: String,
    @SerializedName("127-Avg")
    val hd127Avg: String,
    @SerializedName("128-Avg")
    val hd128Avg: String,
    @SerializedName("129-Avg")
    val hd129Avg: String,
    @SerializedName("130-Avg")
    val hd130Avg: String,
    @SerializedName("911-Avg")
    val hd911Avg: String,
    @SerializedName("912-Avg")
    val hd912Avg: String,
    @SerializedName("925-Avg")
    val hd925Avg: String,
    @SerializedName("926-Avg")
    val hd926Avg: String,
    @SerializedName("927Avg")
    val hd927Avg: String,
    @SerializedName("B03-Avg")
    val hdB03Avg: String,
    val CountyName: String,
    @SerializedName("DI1-Avg")
    val hdDI1Avg: String,
    val DataTime: String,
    val ID: String,
    val Name: String
)

