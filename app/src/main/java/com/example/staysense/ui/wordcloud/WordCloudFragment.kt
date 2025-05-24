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
import com.example.staysense.databinding.FragmentWordCloudBinding
import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            getClusteringData()
        }
    }

    private fun sendWordCloudRequest(text: String) {
        val requestBody = WordCloudRequest(
            useModel = false,
            text = text
        )
        val client = ApiConfig.getApiService()

        viewLifecycleOwner.lifecycleScope.launch {
            showLoadingChartWc(true)
            try {
                val response = withContext(Dispatchers.IO) {
                    client.inputWordCloud(requestBody)
                }

                showLoadingChartWc(false)

                val imageUrl = response.imageUrl
                if (!imageUrl.isNullOrEmpty()) {
                    val uniqueUrl = "$imageUrl?t=${System.currentTimeMillis()}"
                    Glide.with(requireContext())
                        .load(uniqueUrl)
                        .into(binding.ivWordcloud)
                }
            } catch (e: Exception) {
                showLoadingChartWc(false)
                Log.e("WordCloud", "Request failed: ${e.message}")
            }
        }
    }

    private fun showLoadingChartWc(isLoading: Boolean) {
        _binding?.let { binding ->
            binding.progressBarWordcloud.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.cvWordcloud.alpha = if (isLoading) 0.3f else 1f
        }
    }

    private fun getClusteringData() {
        viewLifecycleOwner.lifecycleScope.launch {
            showLoadingChartCluster(true)
            try {
                val client = ApiConfig.getApiService()
                val response = withContext (Dispatchers.IO) {
                    client.getClustering()
                }

                showLoadingChartCluster(false)
                if (response.isNotEmpty()) {
                    setupClusteringChart(response)
                }
            }catch (e: Exception){
                showLoadingChartCluster(false)
                Log.e("Clustering", "Request failed: ${e.message}")
            }
        }
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

        val colors = listOf(
            ContextCompat.getColor(requireContext(), R.color.teal),
            ContextCompat.getColor(requireContext(), R.color.sage),
            ContextCompat.getColor(requireContext(), R.color.yellow_pudar),
            ContextCompat.getColor(requireContext(), R.color.lime_green),
            ContextCompat.getColor(requireContext(), R.color.grayish_lime),
        )

        val legendLabels = listOf(
            "0 - Don't Know",
            "1 - Competitor Made better offer, better devices",
            "2 - Limited range, service dissatisfaction",
            "3 - Attitude support person",
            "4 - Offered data, higher speed, extra data changes"
        )

        val dataSet = BarDataSet(entries, "")
        dataSet.colors = colors
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.BLACK
        dataSet.setDrawValues(true)

        val data = BarData(dataSet)
        data.barWidth = 0.7f

        val legendEntries = legendLabels.mapIndexed { index, label ->
            LegendEntry().apply {
                this.label = label
                this.formColor = colors.getOrElse(index) { Color.GRAY }
                this.form = Legend.LegendForm.SQUARE
            }
        }

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
            axisRight.setDrawGridLines(false)

            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(labels)
                position = XAxis.XAxisPosition.BOTTOM
                granularity = 1f
                setDrawGridLines(false)
                labelCount = labels.size
            }

            axisLeft.setDrawGridLines(false)
            axisLeft.setDrawAxisLine(false)

            barChart.setExtraBottomOffset(10f)
            legend.isEnabled = false
            invalidate()
        }

        val legendContainer = binding.legendLayout
        legendContainer.removeAllViews()

        legendLabels.forEachIndexed { index, label ->
            val itemLayout = LinearLayout(requireContext()).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(12, 8, 12, 8)
                gravity = Gravity.CENTER_VERTICAL
            }

            val colorBox = View(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(30, 30).apply {
                    marginEnd = 12
                }
                setBackgroundColor(colors.getOrElse(index) { Color.GRAY })
            }

            val labelText = TextView(requireContext()).apply {
                text = label
                setTextColor(Color.BLACK)
                textSize = 12f
            }

            itemLayout.addView(colorBox)
            itemLayout.addView(labelText)
            legendContainer.addView(itemLayout)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoadingChartCluster(isLoading: Boolean) {
        _binding?.let { binding ->
            binding.progressBarClustering.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.cvClustering.alpha = if (isLoading) 0.3f else 1f
        }
    }


}
