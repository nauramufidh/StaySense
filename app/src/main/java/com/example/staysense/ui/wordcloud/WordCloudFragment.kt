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
import com.example.staysense.databinding.FragmentWordCloudBinding
import com.github.mikephil.charting.charts.HorizontalBarChart


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

        val wordCloudChart = binding.acWordCloudView
//        val clusteringFragment = ClusteringChartFragment()
//        childFragmentManager.beginTransaction()
//            .replace(R.id.clusterChartContainer, clusteringFragment)
//            .commit()

        wordCloudChart.post {
            setupTagCloud(wordCloudChart)
        }
    }

    private fun setupTagCloud(anyChartView: AnyChartView) {
//        childFragmentManager.beginTransaction()
//            .replace(R.id.clusterChartContainer, ClusteringChartFragment())
//            .commit()

        val tagCloud: TagCloud = AnyChart.tagCloud()
        tagCloud.title("World Cloud")

        val ordinalColor = OrdinalColor.instantiate()
        ordinalColor.colors(
            arrayOf("#26959f", "#f18126", "#3b8ad8", "#60727b", "#e24b26")
        )
        tagCloud.colorScale(ordinalColor)
        tagCloud.angles(arrayOf(-90.0, 0.0, 90.0))

        tagCloud.colorRange().enabled(true)
        tagCloud.colorRange().colorLineSize(15.0)
        tagCloud.background().fill("#F6F8D5")

        val rawData = listOf(
            CategoryValueDataEntry("China", "asia", 1),
            CategoryValueDataEntry("India", "asia", 1),
            CategoryValueDataEntry("India", "asia", 1),
            CategoryValueDataEntry("India", "asia", 1),
            CategoryValueDataEntry("India", "asia", 1),
            CategoryValueDataEntry("United States", "america", 1),
            CategoryValueDataEntry("Indonesia", "asia", 1),
            CategoryValueDataEntry("Indonesia", "asia", 1),
            CategoryValueDataEntry("Indonesia", "asia", 1),
            CategoryValueDataEntry("Indonesia", "asia", 1),
            CategoryValueDataEntry("Indonesia", "asia", 1),
            CategoryValueDataEntry("Korea", "asia", 1),
            CategoryValueDataEntry("New York", "america", 1)
        )

        val groupedData = rawData.groupBy { it.getValue("x") as String }
            .map { (country, entries) ->
                val region = entries.first().getValue("category") as String
                val totalCount = entries.sumOf { it.getValue("value").toString().toInt() }
                CategoryValueDataEntry(country, region, totalCount)
            }

        tagCloud.data(groupedData as List<DataEntry>)
        anyChartView.setChart(tagCloud)

        Log.d("TagCloud", "Grouped data size: ${groupedData.size}")
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
