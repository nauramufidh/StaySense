package com.example.staysense.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.staysense.data.api.ApiConfig
import com.example.staysense.data.api.ApiService
import com.example.staysense.data.response.BarChartItem
import com.example.staysense.data.response.Information
import com.example.staysense.data.response.InformationResponse
import com.example.staysense.data.response.TotalPredictionsPerMonthItem
import com.example.staysense.database.UserSession
import com.example.staysense.databinding.FragmentRateBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class RateFragment : Fragment() {
    private var _binding: FragmentRateBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var apiService : ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRateBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = ApiConfig.getApiService()

        binding.btnRefresh.setOnClickListener {
            Log.d("RateFragment", "Refreshing chart data...")
            fetchChurnRateData()
            Toast.makeText(requireContext(), "Chart updated successfully", Toast.LENGTH_SHORT).show()
        }

        sharedViewModel.uploadLiveData.observe(viewLifecycleOwner) { success ->
            if (success == true) {
                Log.d("RateFragment", "Upload success detected, re-fetching chart data...")
                fetchChurnRateData()
                setupInformation()
                sharedViewModel.setUploadSuccess(false)
            }
        }

        binding.sflRate.setOnRefreshListener {
            refreshData()
        }

        view.postDelayed({
            fetchChurnRateData()
            setupInformation()
        }, 1000)

    }

    private fun fetchChurnRateData() {
        viewLifecycleOwner.lifecycleScope.launch {
            showLoadingChart(true)
            try {
                Log.d("RateFragment", "Fetching chart data...")
                val userId = UserSession.getUserId(requireContext()) ?: ""
                val response = apiService.getCharts(userId)
                if (response.isSuccessful) {
                    Log.d("RateFragment", "Response successful")
                    response.body()?.barChart?.let { barChartData ->
                        Log.d("RateFragment", "Bar chart data received with ${barChartData.size} items")
                        displayBarChart(barChartData)
                        animateChartUpdate()
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
                binding.sflRate.isRefreshing = false
            }
        }
    }

    private fun displayBarChart(barChartData: List<BarChartItem?>) {
        val barChart = binding.churnRatechart

//        val sortedByDate = barChartData.sortedBy { it?.month }

        val entries = barChartData.mapIndexed { index, item ->
            BarEntry(index.toFloat(), item?.churnRate?.toFloat() ?: 0f)
        }

        val labels = barChartData.map { it?.month ?: "" }

        val dataSet = BarDataSet(entries, "Churn Rate")
        dataSet.color = "#E3EEB2".toColorInt()
        dataSet.valueTextSize = 12f

        val data = BarData(dataSet)
        data.barWidth = 0.4f

        val xAxis = barChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawGridLines(false)
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.textSize = 12f

        val leftAxis = barChart.axisLeft
        leftAxis.axisMinimum = 0f
        leftAxis.textSize = 12f
        barChart.axisRight.isEnabled = false
        leftAxis.setDrawGridLines(false)

        barChart.data = data
        barChart.setFitBars(true)
        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.setScaleEnabled(false)
        barChart.animateY(1000)
        barChart.setExtraOffsets(10f, 20f, 10f, 10f)

        barChart.invalidate()
    }

    private fun setupInformation() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                showLoadingInformation(true)
                val userId = UserSession.getUserId(requireContext()) ?: ""
                val response = ApiConfig.getApiService().getInformations(userId)

                if (response.isSuccessful) {
                    val informationResponse = response.body()
                    Log.d("Informations", "Received Information: $informationResponse")
                    val currentMonth = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())

                    informationResponse?.information?.totalPredictionsPerMonth?.let { totalPredictionsList ->
                        val predictionForCurrentMonth = totalPredictionsList.find { it?.month?.equals(currentMonth, ignoreCase = true) == true }

                        predictionForCurrentMonth?.let {
                            val totalPredictions = it.totalPredictions
                            displayInformation(totalPredictions)
                        } ?: run {
                            Log.e("Informations", "No predictions for the current month")
                            binding.tvTotalPredicted.text = "0"
                        }
                    } ?: run {
                        Log.e("Informations", "Information data is empty")
                    }
                } else {
                    Log.e("Informations", "Error fetching information: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Informations", "Error: ${e.message}")
            } finally {
                showLoadingInformation(false)
                binding.sflRate.isRefreshing = false
            }
        }
    }

    private fun refreshData(){
        fetchChurnRateData()
        setupInformation()
    }

    private fun displayInformation(totalPredictions: Int?) {
        if (totalPredictions != null) {
            binding.tvTotalPredicted.text = "$totalPredictions"
        } else {
            binding.tvTotalPredicted.text = "0"
        }
    }

    private fun showLoadingChart(isLoading: Boolean){
        binding.progressBarRateChart.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.cvChurnrate.alpha = if (isLoading) 0.3f else 1f
    }

    private fun showLoadingInformation(isLoading: Boolean) {
        binding.progressBarTotPred.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.cvTotalPredictions.alpha = if (isLoading) 0.3f else 1f
    }

    private fun animateChartUpdate() {
        binding.cvChurnrate.apply {
            animate().alpha(0.5f).setDuration(150).withEndAction {
                animate().alpha(1f).setDuration(150).start()
            }.start()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("RateFragment", "Fragment visible, fetching chart data...")

        if (sharedViewModel.uploadLiveData.value == true) {
            fetchChurnRateData()
            setupInformation()
            sharedViewModel.setUploadSuccess(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}