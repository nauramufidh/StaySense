package com.example.staysense.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UploadResponse(

	@field:SerializedName("summary")
	val summary: Summary? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class Summary(

	@field:SerializedName("file_url")
	val fileUrl: String? = null,

	@field:SerializedName("input_source")
	val inputSource: String? = null,

	@field:SerializedName("not_churn_count")
	val notChurnCount: Int? = null,

	@field:SerializedName("filename")
	val filename: String? = null,

	@field:SerializedName("month")
	val month: String? = null,

	@field:SerializedName("churn_count")
	val churnCount: Int? = null,

	@field:SerializedName("total_customers")
	val totalCustomers: Int? = null,

	@field:SerializedName("churn_rate")
	val churnRate: String? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
) : Parcelable
