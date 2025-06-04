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
import com.example.staysense.data.api.ApiConfig
import com.example.staysense.data.api.ApiService
import com.example.staysense.data.response.Information
import com.example.staysense.data.response.InformationResponse
import com.example.staysense.data.response.PieChart
import com.example.staysense.database.UserSession
import com.example.staysense.databinding.FragmentHomeBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var apiService: ApiService

//    private var chartUpdateJob: Job? = null
//    private var pieChart = PieChart()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        apiService = ApiConfig.getApiService()

        binding.btnRefresh.setOnClickListener{
            Log.d("HomeFragment", "Refreshing Chart...")
            getChartData()
            Toast.makeText(requireContext(), "Chart updated successfully", Toast.LENGTH_SHORT).show()
        }

        sharedViewModel.uploadLiveData.observe(viewLifecycleOwner) { success ->
            if (success == true) {
                Log.d("HomeFragment", "Upload success detected, re-fetching chart data...")
                getChartData()
                setupInformation()
                sharedViewModel.setUploadSuccess(false)
            }
        }

        binding.homeFragment.setOnRefreshListener {
            refreshData()
        }

        getChartData()
        setupInformation()

    }

    private fun getChartData(){
        viewLifecycleOwner.lifecycleScope.launch {
            showLoadingChart(true)
            try {
                val userId = UserSession.getUserId(requireContext()) ?: ""
                Log.d("HomeFragment", "Fetching chart data...")
                val response = apiService.getCharts(userId)
                if (response.isSuccessful) {
                    Log.d("HomeFragment", "Response successful")
                    response.body()?.pieChart?.let { pieChartData ->
                        setupPieChart(pieChartData)
                        animateChartUpdate()
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
                binding.homeFragment.isRefreshing = false
            }
        }
    }

    private fun setupPieChart(pieChartData: PieChart) {
        val chart = binding.churnPiechart

//        val churn = pieChartData.churn?.toFloat() ?: 0f
//        val notChurn = pieChartData.notChurn?.toFloat() ?: 0f

        val churn = pieChartData.churn?.replace("%", "")?.toFloatOrNull() ?: 0f
        val notChurn = pieChartData.notChurn?.replace("%", "")?.toFloatOrNull() ?: 0f

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

    private fun setupInformation() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                showLoadingInformation(true)
                val userId = UserSession.getUserId(requireContext()) ?: ""
                val response = ApiConfig.getApiService().getInformations(userId)

                if (response.isSuccessful) {
                    val informationResponse = response.body()
                    Log.d("HomeFragment", "Received InformationResponse: $informationResponse")

                    informationResponse?.let { information ->
                        displayInformation(information)
                    } ?: run {
                        Log.e("HomeFragment", "Information data is empty")
                    }
                } else {
                    Log.e("HomeFragment", "Error fetching information: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("HomeFragment", "Error: ${e.message}")
            } finally {
                showLoadingInformation(false)
                binding.homeFragment.isRefreshing = false
            }
        }
    }

    private fun displayInformation(information: InformationResponse){
        binding.tvTotalChurn.text = "${information.information?.totalChurn ?: 0}"
        binding.tvTotalCust.text = "${information.information?.totalCustomers ?: 0}"
    }

    private fun showLoadingChart(isLoading: Boolean){
        binding.progressBarPieChart.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.cvPiechart.alpha = if (isLoading) 0.3f else 1f
    }

    private fun showLoadingInformation(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.progressBarTotChurn.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.cvTotalCust.alpha = if (isLoading) 0.3f else 1f
        binding.cvTotalChurn.alpha = if (isLoading) 0.3f else 1f
    }

    private fun refreshData(){
        Log.d("HomeFragment", "Refreshing both chart and information...")
        getChartData()
        setupInformation()
    }

    override fun onResume() {
        super.onResume()
        Log.d("HomeFragment", "Fragment visible, fetching chart data...")

        view?.post {
            if (sharedViewModel.uploadLiveData.value == true) {
                getChartData()
                setupInformation()
                sharedViewModel.setUploadSuccess(false)
            }
        }

    }

    private fun animateChartUpdate() {
        binding.cvPiechart.apply {
            animate().alpha(0.5f).setDuration(150).withEndAction {
                animate().alpha(1f).setDuration(150).start()
            }.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}