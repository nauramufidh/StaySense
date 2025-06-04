package com.example.staysense.data.api

import com.example.staysense.data.response.ChartResponse
import com.example.staysense.data.response.ClusteringResponseItem
import com.example.staysense.data.response.DataCostumerResponse
import com.example.staysense.data.response.Information
import com.example.staysense.data.response.InformationResponse
import com.example.staysense.data.response.PredictResponse
import com.example.staysense.data.response.UploadResponse
import com.example.staysense.data.response.WordCloudRequest
import com.example.staysense.data.response.WordCloudResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface ApiService {

    @Headers("Content-type: application/json")
    @POST("predict")
    fun inputData(
        @Body requestBody: DataCostumerResponse
    ): Call<PredictResponse>

    @Multipart
    @POST("upload")
    fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("id") userId: RequestBody
    ): Call<UploadResponse>

    @GET("dashboard/chart")
    suspend fun getCharts(
        @Query("id") userId: String
    ): Response<ChartResponse>

    @GET("dashboard/informations")
    suspend fun getInformations(
        @Query("id") userId: String
    ): Response<InformationResponse>

    @Headers("Content-type: application/json")
    @POST("wordcloud")
    suspend fun inputWordCloud(
        @Body requestBody: WordCloudRequest
    ): WordCloudResponse

    @Multipart
    @POST("/wordcloud")
    fun generateWordCloudWithFile(
        @Part file: MultipartBody.Part?,
        @Part("text") text: RequestBody?
    ): Call<WordCloudResponse>

    @GET("cluster/chart")
    suspend fun getClustering(): List<ClusteringResponseItem>

}
