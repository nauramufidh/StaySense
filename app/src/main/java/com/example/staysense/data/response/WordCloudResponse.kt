package com.example.staysense.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WordCloudResponse(

	@field:SerializedName("image_url")
	val imageUrl: String
) : Parcelable
