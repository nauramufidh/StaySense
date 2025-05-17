package com.example.staysense.data.response

import com.google.gson.annotations.SerializedName

data class ClusteringResponse(

	@field:SerializedName("ClusteringResponse")
	val clusteringResponse: List<ClusteringResponseItem>
)

data class ClusteringResponseItem(

	@field:SerializedName("cluster")
	val cluster: String,

	@field:SerializedName("count")
	val count: String,

	@field:SerializedName("description")
	val description: String
)
