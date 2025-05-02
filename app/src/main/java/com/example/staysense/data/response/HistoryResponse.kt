package com.example.staysense.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class HistoryResponse(

	@field:SerializedName("history")
	val history: List<HistoryItem?>? = null
) : Parcelable

@Parcelize
data class HistoryItem(

	@field:SerializedName("file_url")
	val fileUrl: String? = null,

	@field:SerializedName("not_churn_count")
	val notChurnCount: String? = null,

	@field:SerializedName("filename")
	val filename: String? = null,

	@field:SerializedName("month")
	val month: String? = null,

	@field:SerializedName("customer_data")
	val customerData: String? = null,

	@field:SerializedName("churn_count")
	val churnCount: String? = null,

	@field:SerializedName("churn_probability")
	val churnProbability: String? = null,

	@field:SerializedName("total_customers")
	val totalCustomers: String? = null,

	@field:SerializedName("churn_rate")
	val churnRate: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null,

	@field:SerializedName("is_churn")
	val isChurn: String? = null
) : Parcelable
