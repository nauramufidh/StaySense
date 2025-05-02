package com.example.staysense.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ChartResponse(

	@field:SerializedName("bar_chart")
	val barChart: List<BarChartItem?>? = null,

	@field:SerializedName("pie_chart")
	val pieChart: PieChart? = null
) : Parcelable

@Parcelize
data class PieChart(

	@field:SerializedName("churn")
	val churn: String? = null,

	@field:SerializedName("not_churn")
	val notChurn: String? = null
) : Parcelable

@Parcelize
data class BarChartItem(

	@field:SerializedName("month")
	val month: String? = null,

	@field:SerializedName("churn_rate")
	val churnRate: String? = null
) : Parcelable
