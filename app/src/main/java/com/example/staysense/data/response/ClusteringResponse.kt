package com.example.staysense.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ClusteringResponse(

	@field:SerializedName("ClusteringResponse")
	val clusteringResponse: List<ClusteringResponseItem>
) : Parcelable

@Parcelize
data class ClusteringResponseItem(

	@field:SerializedName("cluster")
	val cluster: String,

	@field:SerializedName("count")
	val count: String,

	@field:SerializedName("description")
	val description: String
) : Parcelable
