package com.example.staysense.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

        getChartData()

    }

    private fun getChartData(){
        val api = ApiConfig.getApiService()

        lifecycleScope.launch{
            try {
                val response = withContext(Dispatchers.IO){
                    api.getCharts()
                }

                if (response.isSuccessful) {
                    val chartData = response.body()

                    chartData?.pieChart?.let { pieChart ->
                        setupPieChart()
                    }
                } else {
                    Log.e("ChartDebug", "Status: ${response.code()}, Message: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "Gagal mengambil data chart", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupPieChart() {
        Log.d("ChartData", "Churn: ${pieChart.churn}, Not Churn: ${pieChart.notChurn}")
        val pie = AnyChart.pie()

        val churnValue = pieChart.churn?.toDouble() ?: 0.0
        val notChurnValue = pieChart.notChurn?.toDouble() ?: 0.0

        if (churnValue == 0.0 && notChurnValue == 0.0) {
            Log.d("ChartData", "Both values are zero, displaying chart with 0%")
            val data = listOf(
                ValueDataEntry("Churn", 90),
                ValueDataEntry("Not Churn", 10)
            )
            pie.data(data)
        } else {
            val data = listOf(
                ValueDataEntry("Churn", churnValue),
                ValueDataEntry("Not Churn", notChurnValue)
            )
            pie.data(data)
        }

        val data = listOf(
            ValueDataEntry("Churn", churnValue),
            ValueDataEntry("Not Churn", notChurnValue)
        )

        pie.data(data)
        pie.title("Customer Churn Pie Chart")
        binding.churnPiechart.setChart(pie)

//        val anyChartView = binding.churnPiechart
//        anyChartView.setProgressBar(binding.progressbar)
//
//        Log.d("PieChart", "Binding progressbar: ${binding.progressbar}")
//        Log.d("PieChart", "AnyChartView: ${binding.churnPiechart}")
//
//        val pie = AnyChart.pie()
//
//        pie.setOnClickListener(object : ListenersInterface.OnClickListener(arrayOf("x", "value")) {
//            override fun onClick(event: Event?) {
//                if (event != null) {
//                    Toast.makeText(requireContext(), "${event.getData()["x"]}: ${event.getData()["value"]}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        })
//
//        val data = listOf(
//            ValueDataEntry("Churn", 6371664),
//            ValueDataEntry("Not Churn", 789622),
//        )
//
//        pie.data(data)
////        pie.title("Churn Pie Chart")
////        pie.labels().position("outside")
//
////        pie.legend().title().enabled(true)
////        pie.legend().title().text("Churn").padding(0.0, 0.0, 10.0, 0.0)
//
//        pie.legend()
//            .position("right")
//            .itemsLayout(LegendLayout.VERTICAL)
//            .align(Align.CENTER)
//
//        anyChartView.setChart(pie)
//
//        Log.d("PieChart", "setupPieChart finished")
    }




    override fun onPause() {
        super.onPause()
        chartUpdateJob?.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}