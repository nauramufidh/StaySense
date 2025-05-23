package com.example.staysense.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ChartResponse(

	@field:SerializedName("bar_chart")
	val barChart: List<BarChartItem?>? = null,

	@field:SerializedName("information")
	val information: Information? = null,

	@field:SerializedName("pie_chart")
	val pieChart: PieChart? = null
) : Parcelable

@Parcelize
data class Information(

	@field:SerializedName("average_churn_rate")
	val averageChurnRate: String? = null,

	@field:SerializedName("total_customers")
	val totalCustomers: Int? = null,

	@field:SerializedName("total_not_churn")
	val totalNotChurn: Int? = null,

	@field:SerializedName("total_churn")
	val totalChurn: Int? = null
) : Parcelable

@Parcelize
data class BarChartItem(

	@field:SerializedName("month")
	val month: String? = null,

	@field:SerializedName("churn_rate")
	val churnRate: Double? = null
) : Parcelable

@Parcelize
data class PieChart(

	@field:SerializedName("churn")
	val churn: Int? = null,

	@field:SerializedName("not_churn")
	val notChurn: Int? = null
) : Parcelable
