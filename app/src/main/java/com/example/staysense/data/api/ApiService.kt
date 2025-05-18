package com.example.staysense.data.api

import com.example.staysense.data.response.ChartResponse
import com.example.staysense.data.response.ClusteringResponse
import com.example.staysense.data.response.DataCostumerResponse
import com.example.staysense.data.response.PredictResponse
import com.example.staysense.data.response.UploadResponse
import com.example.staysense.data.response.WordCloudResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface ApiService {

    @Headers("Content-type: application/json")
    @POST("predict")
    fun inputData(
        @Body requestBody: DataCostumerResponse
    ): Call<PredictResponse>

    @Multipart
    @POST("upload")
    fun uploadFile(
        @Part file: MultipartBody.Part
    ): Call<UploadResponse>

    @GET("dashboard/chart")
    suspend fun getCharts(): Response<ChartResponse>

    @Headers("Content-type: application/json")
    @POST("wordcloud")
    fun getWordcloud(
        @Body requestBody: WordCloudResponse
    ) : Call<WordCloudResponse>

    @GET("cluster/chart")
    fun getClustering(): Response<ClusteringResponse>

}
