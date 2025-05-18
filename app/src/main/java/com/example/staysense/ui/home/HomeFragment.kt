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

    private fun setupPieChart(pieChartData: PieChart) {

        Log.d("ChartData", "Churn: ${pieChartData.churn}, Not Churn: ${pieChartData.notChurn}")
        val pie = AnyChart.pie()

        val churnValue = pieChartData.churn?.toDouble() ?: 0.0
        val notChurnValue = pieChartData.notChurn?.toDouble() ?: 0.0

        val data = listOf(
            ValueDataEntry("Churn", churnValue),
            ValueDataEntry("Not Churn", notChurnValue)
        )

        Log.d("ChartData", "Chart data: Churn: $churnValue, Not Churn: $notChurnValue")
        pie.data(data)

        pie.title("Customer Churn Pie Chart")

        binding.churnPiechart.setChart(pie)

//        view?.postDelayed({
//            binding.churnPiechart.setChart(pie)
//        }, 500)


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