package com.example.staysense.ui.wordcloud

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.HoverMode
import com.example.staysense.databinding.FragmentClusteringChartBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ClusteringChartFragment : Fragment() {
    private var _binding: FragmentClusteringChartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClusteringChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val clusteringChart = binding.acClusteringView

//        viewLifecycleOwner.lifecycleScope.launch {
//            delay(1000)
//            val clusteringChart = binding.acClusteringView
//            setupClusteringChart(clusteringChart)
//        }
    }

//    private fun setupClusteringChart(clusteringchartView: AnyChartView) {
//        val vertical = AnyChart.vertical()
//
//        vertical.animation(true)
//        vertical.title("Clustering Result")
//
//        val data = mutableListOf<DataEntry>(
//            ValueDataEntry("Services", 23.0),
//            ValueDataEntry("Quality", 35.0),
//            ValueDataEntry("Price", 18.0)
//        )
//
//        val bar = vertical.bar(data)
//        bar.labels().enabled(true)
//        bar.labels().format("{%Value}")
//        bar.name("Clustering")
//        bar.tooltip().titleFormat("{%X}").format("Total words: {%Value}")
//
//        vertical.yScale().minimum(0.0)
//        vertical.xAxis(true)
//        vertical.yAxis(true)
//        vertical.interactivity().hoverMode(HoverMode.BY_X)
//
//        clusteringchartView.setChart(vertical)
//
//    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}