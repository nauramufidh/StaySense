package com.example.staysense.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class PredictResponse(

	@field:SerializedName("prediction")
	val prediction: Prediction? = null
) : Parcelable

@Parcelize
data class Prediction(

	@field:SerializedName("churn_probability")
	val churnProbability: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("is_churn")
	val isChurn: String? = null
) : Parcelable
