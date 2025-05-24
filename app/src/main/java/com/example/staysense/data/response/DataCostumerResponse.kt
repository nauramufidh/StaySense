package com.example.staysense.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class DataCostumerResponse(

	@SerializedName("online_backup")
	val onlineBackup: Boolean,

	@SerializedName("unlimited_data")
	val unlimitedData: Boolean,

	@SerializedName("satisfaction_score")
	val satisfactionScore: Double,

	@SerializedName("city")
	val city: String,

	@SerializedName("online_security")
	val onlineSecurity: Boolean,

	@SerializedName("cltv")
	val cltv: Int,

	@SerializedName("premium_tech_support")
	val premiumTechSupport: Boolean,

	@SerializedName("number_of_dependents")
	val numberOfDependents: Int,

	@SerializedName("contract")
	val contract: String,

	@SerializedName("internet_service")
	val internetService: Boolean,

	@SerializedName("device_protection_plan")
	val deviceProtectionPlan: Boolean,

	@SerializedName("total_charges")
	val totalCharges: Double,

	@SerializedName("total_revenue")
	val totalRevenue: Double,

	@SerializedName("monthly_charge")
	val monthlyCharge: Double,

	@SerializedName("streaming_tv")
	val streamingTv: Boolean,

	@SerializedName("tenure_in_months")
	val tenureInMonths: Int,

	@SerializedName("streaming_movies")
	val streamingMovies: Boolean,

	@SerializedName("streaming_music")
	val streamingMusic: Boolean,

	@SerializedName("age")
	val age: Int,

	@SerializedName("payment_method")
	val paymentMethod: String,

	@SerializedName("churn_score")
	val churnScore: Int

) : Parcelable

