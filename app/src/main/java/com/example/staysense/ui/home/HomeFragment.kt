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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}