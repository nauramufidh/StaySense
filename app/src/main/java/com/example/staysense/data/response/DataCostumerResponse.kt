package com.example.staysense.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class DataCostumerResponse(

	@field:SerializedName("online_backup")
	val onlineBackup: String? = null,

	@field:SerializedName("unlimited_data")
	val unlimitedData: String? = null,

	@field:SerializedName("satisfaction_score")
	val satisfactionScore: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("online_security")
	val onlineSecurity: String? = null,

	@field:SerializedName("cltv")
	val cltv: String? = null,

	@field:SerializedName("premium_tech_support")
	val premiumTechSupport: String? = null,

	@field:SerializedName("number_of_dependents")
	val numberOfDependents: String? = null,

	@field:SerializedName("contract")
	val contract: String? = null,

	@field:SerializedName("internet_service")
	val internetService: String? = null,

	@field:SerializedName("device_protection_plan")
	val deviceProtectionPlan: String? = null,

	@field:SerializedName("total_charges")
	val totalCharges: String? = null,

	@field:SerializedName("total_revenue")
	val totalRevenue: String? = null,

	@field:SerializedName("monthly_charge")
	val monthlyCharge: String? = null,

	@field:SerializedName("streaming_tv")
	val streamingTv: String? = null,

	@field:SerializedName("tenure_in_months")
	val tenureInMonths: String? = null,

	@field:SerializedName("streaming_movies")
	val streamingMovies: String? = null,

	@field:SerializedName("streaming_music")
	val streamingMusic: String? = null,

	@field:SerializedName("age")
	val age: String? = null,

	@field:SerializedName("payment_method")
	val paymentMethod: String? = null,

	@field:SerializedName("churn_score")
	val churnScore: String? = null
) : Parcelable
