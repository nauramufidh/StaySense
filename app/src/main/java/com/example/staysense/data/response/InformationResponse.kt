package com.example.staysense.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class InformationResponse(

	@field:SerializedName("information")
	val information: Information? = null
) : Parcelable

@Parcelize
data class Information(

	@field:SerializedName("total_predictions_per_month")
	val totalPredictionsPerMonth: List<TotalPredictionsPerMonthItem?>? = null,

	@field:SerializedName("total_customers")
	val totalCustomers: Int? = null,

	@field:SerializedName("total_not_churn")
	val totalNotChurn: Int? = null,

	@field:SerializedName("total_churn")
	val totalChurn: Int? = null
) : Parcelable

@Parcelize
data class TotalPredictionsPerMonthItem(

	@field:SerializedName("total_predictions")
	val totalPredictions: Int? = null,

	@field:SerializedName("month")
	val month: String? = null
) : Parcelable
