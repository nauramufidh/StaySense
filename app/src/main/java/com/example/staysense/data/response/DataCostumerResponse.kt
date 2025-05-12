package com.example.staysense.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class DataCostumerResponse(

	@SerializedName("online_backup")
	val onlineBackup: Int,

	@SerializedName("unlimited_data")
	val unlimitedData: Int,

	@SerializedName("satisfaction_score")
	val satisfactionScore: Int,

	@SerializedName("city")
	val city: Int,

	@SerializedName("online_security")
	val onlineSecurity: Int,

	@SerializedName("cltv")
	val cltv: Int,

	@SerializedName("premium_tech_support")
	val premiumTechSupport: Int,

	@SerializedName("number_of_dependents")
	val numberOfDependents: Int,

	@SerializedName("contract")
	val contract: Int,

	@SerializedName("internet_service")
	val internetService: Int,

	@SerializedName("device_protection_plan")
	val deviceProtectionPlan: Int,

	@SerializedName("total_charges")
	val totalCharges: Int,

	@SerializedName("total_revenue")
	val totalRevenue: Int,

	@SerializedName("monthly_charge")
	val monthlyCharge: Int,

	@SerializedName("streaming_tv")
	val streamingTv: Int,

	@SerializedName("tenure_in_months")
	val tenureInMonths: Int,

	@SerializedName("streaming_movies")
	val streamingMovies: Int,

	@SerializedName("streaming_music")
	val streamingMusic: Int,

	@SerializedName("age")
	val age: Int,

	@SerializedName("payment_method")
	val paymentMethod: Int,

	@SerializedName("churn_score")
	val churnScore: Int

) : Parcelable

