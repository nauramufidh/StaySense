package com.example.staysense.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.chart.common.listener.Event
import com.anychart.chart.common.listener.ListenersInterface
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import com.example.staysense.R
import com.example.staysense.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

        setupPieChart()
//        setupChurnRate()
    }




    private fun setupPieChart() {
        val anyChartView = binding.churnPiechart
        anyChartView.setProgressBar(binding.progressbar)

        val pie = AnyChart.pie()

        pie.setOnClickListener(object : ListenersInterface.OnClickListener(arrayOf("x", "value")) {
            override fun onClick(event: Event?) {
                if (event != null) {
                    Toast.makeText(requireContext(), "${event.getData()["x"]}: ${event.getData()["value"]}", Toast.LENGTH_SHORT).show()
                }
            }
        })

        val data = listOf(
            ValueDataEntry("Yes", 6371664),
            ValueDataEntry("No", 789622),
        )

        pie.data(data)
        pie.title("Churn Pie Chart")
        pie.labels().position("outside")

//        pie.legend().title().enabled(true)
//        pie.legend().title().text("Churn").padding(0.0, 0.0, 10.0, 0.0)

        pie.legend()
            .position("right")
            .itemsLayout(LegendLayout.VERTICAL)
            .align(Align.CENTER)

        anyChartView.setChart(pie)
    }

//    private fun setupChurnRate() {
//        val anyChartView = binding.chartChurnrate
//        anyChartView.setProgressBar(binding.progressbar)
//
//        val lineChart = AnyChart.line()
//
//        cartesian.animation(true)
//        cartesian.padding(10.0, 20.0, 5.0, 20.0)
//
//        cartesian.crosshair().enabled(true)
//        cartesian.crosshair().yLabel(true).yStroke(null, null, null, null, null)
//
//        cartesian.tooltip().positionMode(com.anychart.enums.TooltipPositionMode.POINT)
//        cartesian.title("Trend of Churn Rate Over the Years")
//
//        cartesian.yAxis(0).title("Churn Rate (%)")
//        cartesian.xAxis(0).labels().padding(5.0, 5.0, 5.0, 5.0)
//
//        val seriesData = listOf(
//            CustomDataEntry("2016", 5.2, 6.1, 4.8),
//            CustomDataEntry("2017", 6.0, 6.5, 5.5),
//            CustomDataEntry("2018", 6.8, 7.2, 6.0),
//            CustomDataEntry("2019", 7.1, 8.0, 6.7),
//            CustomDataEntry("2020", 7.5, 8.5, 7.1),
//            CustomDataEntry("2021", 6.9, 7.8, 6.5),
//            CustomDataEntry("2022", 6.2, 7.0, 5.9),
//            CustomDataEntry("2023", 5.5, 6.2, 5.3)
//        )
//
//        val set = com.anychart.data.Set.instantiate()
//        set.data(seriesData)
//
//        val series1Mapping = set.mapAs("{ x: 'x', value: 'value' }")
//        val series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }")
//        val series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }")
//
//        val series1 = cartesian.line(series1Mapping)
//        series1.name("Product A")
//        series1.hovered().markers().enabled(true)
//        series1.hovered().markers().type(com.anychart.enums.MarkerType.CIRCLE).size(4.0)
//        series1.tooltip().position("right").anchor(com.anychart.enums.Anchor.LEFT_CENTER).offsetX(5.0).offsetY(5.0)
//
//        val series2 = cartesian.line(series2Mapping)
//        series2.name("Product B")
//        series2.hovered().markers().enabled(true)
//        series2.hovered().markers().type(com.anychart.enums.MarkerType.CIRCLE).size(4.0)
//        series2.tooltip().position("right").anchor(com.anychart.enums.Anchor.LEFT_CENTER).offsetX(5.0).offsetY(5.0)
//
//        val series3 = cartesian.line(series3Mapping)
//        series3.name("Product C")
//        series3.hovered().markers().enabled(true)
//        series3.hovered().markers().type(com.anychart.enums.MarkerType.CIRCLE).size(4.0)
//        series3.tooltip().position("right").anchor(com.anychart.enums.Anchor.LEFT_CENTER).offsetX(5.0).offsetY(5.0)
//
//        cartesian.legend().enabled(true)
//        cartesian.legend().fontSize(13.0)
//        cartesian.legend().padding(0.0, 0.0, 10.0, 0.0)
//
//        anyChartView.setChart(cartesian)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}