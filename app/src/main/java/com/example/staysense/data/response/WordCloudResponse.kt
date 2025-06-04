package com.example.staysense.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class WordCloudRequest(
	@field:SerializedName("id")
	val userId: String?,

	@field:SerializedName("use_model")
	val useModel: Boolean? = null,

	@field:SerializedName("text")
	val text: String? = null
) : Parcelable

@Parcelize
data class WordCloudResponse(

	@field:SerializedName("image_url")
	val imageUrl: String? = null
) : Parcelable
