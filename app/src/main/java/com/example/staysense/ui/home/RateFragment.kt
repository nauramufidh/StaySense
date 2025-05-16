package com.example.staysense.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.enums.Anchor
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.example.staysense.R
import com.example.staysense.data.api.ApiConfig
import com.example.staysense.data.api.ApiService
import com.example.staysense.data.response.BarChartItem
import com.example.staysense.data.response.ChartResponse
import com.example.staysense.databinding.FragmentHomeBinding
import com.example.staysense.databinding.FragmentRateBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RateFragment : Fragment() {
    private var _binding: FragmentRateBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var apiService : ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRateBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = ApiConfig.getApiService()

        sharedViewModel.uploadLiveData.observe(viewLifecycleOwner) { success ->
            if (success == true) {
                Log.d("RateFragment", "Upload success detected, re-fetching chart data...")
                fetchChurnRateData()
                sharedViewModel.setUploadSuccess(false)
            }
        }

        fetchChurnRateData()
    }

    private fun fetchChurnRateData() {
        viewLifecycleOwner.lifecycleScope.launch {
            showLoadingChart(true)
            try {
                Log.d("RateFragment", "Fetching chart data...")
                val response = apiService.getCharts()
                if (response.isSuccessful) {
                    Log.d("RateFragment", "Response successful")
                    response.body()?.barChart?.let { barChartData ->
                        Log.d("RateFragment", "Bar chart data received with ${barChartData.size} items")
                        displayBarChart(barChartData)
                    } ?: run {
                        Log.e("RateFragment", "No barChart data found in response")
                    }
                } else {
                    Log.e("RateFragment", "Error fetching data: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("RateFragment", "Network error: ${e.message}")
            } finally {
                showLoadingChart(false)
            }
        }
    }


    private fun displayBarChart(barChartData: List<BarChartItem?>){
        Log.d("RateFragment", "Displaying bar chart with ${barChartData.size} data points")
        val column = AnyChart.column()

        val data = barChartData.map {
            Log.d("RateFragment", "Adding data point: month=${it?.month}, churnRate=${it?.churnRate}")
            ValueDataEntry(it?.month ?: "", it?.churnRate ?: 0.0)
        }

        column.data(data)
        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(5.0)
            .format("{%Value}%")

        column.title("Monthly Churn Rate (%)")
        column.yAxis(0).title("Churn Rate")
        column.xAxis(0).title("Month")

        column.animation(true)
        column.yScale().minimum(0.0)

        binding.churnRatechart.setChart(column)
        Log.d("RateFragment", "Bar chart rendered")
    }

    private fun showLoadingChart(isLoading: Boolean){
        binding.progressBarRateChart.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.cvChurnrate.alpha = if (isLoading) 0.3f else 1f
    }

    override fun onResume() {
        super.onResume()
        Log.d("RateFragment", "Fragment visible, fetching chart data...")

        if (sharedViewModel.uploadLiveData.value == true) {
            fetchChurnRateData()
            sharedViewModel.setUploadSuccess(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}