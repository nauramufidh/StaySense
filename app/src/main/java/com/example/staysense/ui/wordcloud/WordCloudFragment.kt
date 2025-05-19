package com.example.staysense.ui.wordcloud

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.staysense.R
import com.bumptech.glide.Glide
import com.example.staysense.data.api.ApiConfig
import com.example.staysense.data.response.ClusteringResponseItem
import com.example.staysense.data.response.WordCloudRequest
import com.example.staysense.data.response.WordCloudResponse
import com.example.staysense.databinding.FragmentWordCloudBinding
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WordCloudFragment : Fragment() {
    private var _binding: FragmentWordCloudBinding? = null
    private val binding get() = _binding!!

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

        lifecycleScope.launch {
            delay(200)
            getClusteringData()
        }
    }

    private fun sendWordCloudRequest(text: String) {
        val requestBody = WordCloudRequest(
            useModel = false,
            text = text
        )
        val client = ApiConfig.getApiService()
        showLoadingChartWc(true)

        client.inputWordCloud(requestBody).enqueue(object : Callback<WordCloudResponse> {
            override fun onResponse(
                call: Call<WordCloudResponse>,
                response: Response<WordCloudResponse>
            ) {
                showLoadingChartWc(false)
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

    private fun showLoadingChartWc(isLoading: Boolean){
        binding.progressBarWordcloud.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.cvWordcloud.alpha = if (isLoading) 0.3f else 1f
    }

    private fun getClusteringData() {
        val client = ApiConfig.getApiService()
        showLoadingChartCluster(true)

        client.getClustering().enqueue(object : Callback<List<ClusteringResponseItem>> {
            override fun onResponse(
                call: Call<List<ClusteringResponseItem>>,
                response: Response<List<ClusteringResponseItem>>
            ) {
                showLoadingChartCluster(false)
                if (response.isSuccessful) {
                    val clusteringData = response.body()
                    if (!clusteringData.isNullOrEmpty()) {
                        setupClusteringChart(clusteringData)
                    }
                } else {
                    Log.e("Clustering", "Response error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ClusteringResponseItem>>, t: Throwable) {
                showLoadingChartCluster(false)
                Log.e("Clustering", "Request failed: ${t.message}")
            }
        })
    }

    private fun setupClusteringChart(dataList: List<ClusteringResponseItem>) {
        if (!isAdded || _binding == null) return

        val barChart = binding.barChartClustering
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        dataList.forEachIndexed { index, item ->
            entries.add(BarEntry(index.toFloat(), item.count.toFloat()))
            labels.add(item.cluster)
        }

        val dataSet = BarDataSet(entries, "Reason")
        val colors = listOf(
            ContextCompat.getColor(requireContext(), R.color.teal),
            ContextCompat.getColor(requireContext(), R.color.navy_pudar),
            ContextCompat.getColor(requireContext(), R.color.sage),
            ContextCompat.getColor(requireContext(), R.color.yellow_pudar),
            ContextCompat.getColor(requireContext(), R.color.lime_green),
            ContextCompat.getColor(requireContext(), R.color.grayish_lime),
        )

        dataSet.colors = colors

        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.BLACK
        dataSet.setDrawValues(true)

        val data = BarData(dataSet)
        data.barWidth = 0.7f

        barChart.apply {
            this.data = data
            description.isEnabled = false
            animateY(1000)
            setScaleEnabled(false)
            setFitBars(true)
            setDrawValueAboveBar(true)

            axisLeft.isEnabled = false
            axisLeft.axisMinimum = 0f
            axisRight.isEnabled = true

            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(labels)
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                setDrawGridLines(false)
                labelCount = labels.size
            }

            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawAxisLine(false)

            legend.isEnabled = false
            invalidate()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoadingChartCluster(isLoading: Boolean) {
        binding.progressBarClustering.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.barChartClustering.alpha = if (isLoading) 0.3f else 1f
    }

}
