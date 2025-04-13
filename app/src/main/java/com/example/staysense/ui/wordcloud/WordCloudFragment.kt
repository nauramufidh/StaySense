package com.example.staysense.ui.wordcloud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.CategoryValueDataEntry
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.charts.TagCloud
import com.anychart.scales.OrdinalColor
import com.example.staysense.databinding.FragmentWordCloudBinding


class WordCloudFragment : Fragment() {
    private var _binding: FragmentWordCloudBinding? = null
    private val binding get() = _binding!!

    private lateinit var anyChartView: AnyChartView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWordCloudBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        anyChartView = binding.acWordCloudView

        anyChartView.post {
            setupTagCloud()
        }
    }

    private fun setupTagCloud() {
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

        val data = listOf(
            CategoryValueDataEntry("China", "asia", 1383220000),
            CategoryValueDataEntry("India", "asia", 1316000000),
            CategoryValueDataEntry("United States", "america", 324982000),
            CategoryValueDataEntry("Indonesia", "asia", 263510000),
        )

        tagCloud.data(data as List<DataEntry>)
        anyChartView.setChart(tagCloud)
    }
}