package com.example.staysense.ui.wordcloud

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.CategoryValueDataEntry
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.charts.TagCloud
import com.anychart.scales.OrdinalColor
import com.example.staysense.R
import com.bumptech.glide.Glide
import com.example.staysense.data.api.ApiConfig
import com.example.staysense.data.response.WordCloudRequest
import com.example.staysense.data.response.WordCloudResponse
import com.example.staysense.databinding.FragmentWordCloudBinding
import com.github.mikephil.charting.charts.HorizontalBarChart
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WordCloudFragment : Fragment() {
    private var _binding: FragmentWordCloudBinding? = null
    private val binding get() = _binding!!

    private lateinit var anyChartView: AnyChartView
    private lateinit var clusteringchartView: HorizontalBarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWordCloudBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmitWord.setOnClickListener {
            val inputText = binding.etWordInput.text.toString()

            if (inputText.isNotBlank()){
                sendWordCloudRequest(inputText)
            }
        }
    }

    private fun sendWordCloudRequest(text: String) {
        val requestBody = WordCloudRequest(
            useModel = false,
            text = text
        )
        val client = ApiConfig.getApiService()
        showLoadingChart(true)

        client.inputWordCloud(requestBody).enqueue(object : Callback<WordCloudResponse> {
            override fun onResponse(
                call: Call<WordCloudResponse>,
                response: Response<WordCloudResponse>
            ) {
                showLoadingChart(false)
                if (response.isSuccessful) {
                    val imageUrl = response.body()?.imageUrl
                    if (!imageUrl.isNullOrEmpty()) {
                        val uniqueUrl = "$imageUrl?t=${System.currentTimeMillis()}"

                        Glide.with(requireContext())
                            .load(uniqueUrl)
                            .into(binding.ivWordcloud)
                    }
                } else {
                    Log.e("WordCloud", "Response error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<WordCloudResponse>, t: Throwable) {

                Log.e("WordCloud", "Request failed: ${t.message}")
            }
        })
    }

    private fun showLoadingChart(isLoading: Boolean){
        binding.progressBarWordcloud.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.cvWordcloud.alpha = if (isLoading) 0.3f else 1f
    }

}
