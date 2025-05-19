package com.example.staysense.ui.home

import android.graphics.Color
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
import com.anychart.chart.common.listener.Event
import com.anychart.chart.common.listener.ListenersInterface
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import com.example.staysense.data.api.ApiConfig
import com.example.staysense.data.response.PieChart
import com.example.staysense.databinding.FragmentHomeBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var chartUpdateJob: Job? = null
    private var pieChart = PieChart()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel.uploadLiveData.observe(viewLifecycleOwner) { success ->
            if (success == true) {
                Log.d("RateFragment", "Upload success detected, re-fetching chart data...")
                getChartData()
                sharedViewModel.setUploadSuccess(false)
            }
        }

        getChartData()

    }

    private fun getChartData(){
        val api = ApiConfig.getApiService()

        viewLifecycleOwner.lifecycleScope.launch {
            showLoadingChart(true)
            try {
                Log.d("HomeFragment", "Fetching chart data...")
                val response = api.getCharts()
                if (response.isSuccessful) {
                    Log.d("HomeFragment", "Response successful")
                    response.body()?.pieChart?.let { pieChartData ->
                        withContext(Dispatchers.Main) {
                            setupPieChart(pieChartData)
                        }
                    } ?: run {
                        Log.e("HomeFragment", "No pieChart data found in response")
                    }
                } else {
                    Log.e("HomeFragment", "Error fetching data: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "Network error: ${e.message}")
            } finally {
                showLoadingChart(false)
            }

        }

    }

    private fun setupPieChart(pieChartData: PieChart) {
        val chart = binding.churnPiechart

        val churn = pieChartData.churn?.toFloat() ?: 0f
        val notChurn = pieChartData.notChurn?.toFloat() ?: 0f

        val entries = listOf(
            PieEntry(churn, "Churn"),
            PieEntry(notChurn, "Not Churn")
        )

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = listOf(
            ColorTemplate.rgb("#E3EEB2"),
            ColorTemplate.rgb("#71C0BB")
        )
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f
        dataSet.valueTextSize = 14f
        dataSet.valueTextColor = Color.BLACK
        val data = PieData(dataSet)

        chart.data = data
        chart.description.isEnabled = false
        chart.setUsePercentValues(true)
        chart.centerText = "Churn Pie Chart"
        chart.setEntryLabelColor(Color.BLACK)
        chart.setEntryLabelTextSize(12f)
        chart.animateY(1000)

        chart.invalidate()
    }

    private fun showLoadingChart(isLoading: Boolean){
        binding.progressBarPieChart.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.cvPiechart.alpha = if (isLoading) 0.3f else 1f
    }

    override fun onResume() {
        super.onResume()
        Log.d("HomeFragment", "Fragment visible, fetching chart data...")

        view?.post {
            if (sharedViewModel.uploadLiveData.value == true) {
                getChartData()
                sharedViewModel.setUploadSuccess(false)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}