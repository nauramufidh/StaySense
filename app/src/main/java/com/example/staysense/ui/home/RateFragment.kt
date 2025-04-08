package com.example.staysense.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.enums.Anchor
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.example.staysense.R
import com.example.staysense.databinding.FragmentHomeBinding
import com.example.staysense.databinding.FragmentRateBinding


class RateFragment : Fragment() {
    private var _binding: FragmentRateBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRateBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupChurnRate()
    }

    private fun setupChurnRate() {
        val column: Cartesian = AnyChart.column()
        val data = listOf(
            ValueDataEntry("Jan", 5),
            ValueDataEntry("Feb", 8),
            ValueDataEntry("Mar", 4),
            ValueDataEntry("Apr", 6),
            ValueDataEntry("May", 7)
        )
        val series = column.column(data)

        series.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(5.0)
            .format("{%Value}%")

        column.animation(true)
        column.title("Monthly Churn Rate (%)")

        column.yScale().minimum(0.0)

        column.tooltip().positionMode(TooltipPositionMode.POINT)
        column.interactivity().hoverMode("by-x")
        column.xAxis(0).title("Month")
        column.yAxis(0).title("Churn Rate")

        binding.churnPiechart.setChart(column)
    }
}