package com.example.staysense.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class PredictResponse(

	@field:SerializedName("prediction")
	val prediction: Prediction? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class Prediction(

	@field:SerializedName("not_churn_rate")
	val notChurnRate: String? = null,

	@field:SerializedName("churn_rate")
	val churnRate: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("is_churn")
	val isChurn: Boolean? = null,

	@field:SerializedName("solution")
	val solution: String? = null
) : Parcelable
