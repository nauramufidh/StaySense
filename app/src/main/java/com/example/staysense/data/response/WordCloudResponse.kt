package com.example.staysense.data.response

import com.google.gson.annotations.SerializedName

data class WordCloudResponse(

	@field:SerializedName("image_url")
	val imageUrl: String
)
