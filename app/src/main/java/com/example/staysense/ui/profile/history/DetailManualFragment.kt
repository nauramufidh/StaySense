package com.example.staysense.ui.profile.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.staysense.R
import com.example.staysense.database.PredictionDatabase
import com.example.staysense.database.PredictionHistoryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DetailManualFragment : Fragment() {

    private lateinit var predictionHistoryDao: PredictionHistoryDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_manual, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        predictionHistoryDao = PredictionDatabase.getInstance(requireContext()).predictionHistoryDao()

        val id = arguments?.getInt("historyId") ?: return

        val backButton = view.findViewById<ImageButton>(R.id.btn_back_manual_input)
        backButton.setOnClickListener {
            findNavController().popBackStack()
        }

        lifecycleScope.launch {
            val data = withContext(Dispatchers.IO) {
                predictionHistoryDao.getHistoryById(id)
            }

            data?.let{
                view.findViewById<TextView>(R.id.tv_message_history_manual).text = it.message
                view.findViewById<TextView>(R.id.tv_timestamp_history_manual).text = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(it.timestamp))

                view.findViewById<TextView>(R.id.tv_age_history_manual).text = it.age.toString()
                view.findViewById<TextView>(R.id.tv_nod_history_manual).text = it.numberOfDependents.toString()
                view.findViewById<TextView>(R.id.tv_city_history_manual).text = it.city
                view.findViewById<TextView>(R.id.tv_tim_history_manual).text = it.tenureInMonths.toString()
                view.findViewById<TextView>(R.id.tv_is_history_manual).text = it.internetService
                view.findViewById<TextView>(R.id.tv_os_history_manual).text = it.onlineSecurity
                view.findViewById<TextView>(R.id.tv_ob_history_manual).text = it.onlineBackup
                view.findViewById<TextView>(R.id.tv_dpp_history_manual).text = it.deviceProtectionPlan
                view.findViewById<TextView>(R.id.tv_pts_history_manual).text = it.premiumTechSupport
                view.findViewById<TextView>(R.id.tv_stv_history_manual).text = it.streamingTv
                view.findViewById<TextView>(R.id.tv_smovies_history_manual).text = it.streamingMovies
                view.findViewById<TextView>(R.id.tv_smusic_history_manual).text = it.streamingMusic
                view.findViewById<TextView>(R.id.tv_ud_history_manual).text = it.unlimitedData
                view.findViewById<TextView>(R.id.tv_contract_history_manual).text = it.contract
                view.findViewById<TextView>(R.id.tv_pm_history_manual).text = it.paymentMethod
                view.findViewById<TextView>(R.id.tv_mc_history_manual).text = it.monthlyCharge.toString()
                view.findViewById<TextView>(R.id.tv_tc_history_manual).text = it.totalCharges.toString()
                view.findViewById<TextView>(R.id.tv_tr_history_manual).text = it.totalRevenue.toString()
                view.findViewById<TextView>(R.id.tv_ss_history_manual).text = it.satisfactionScore.toString()
                view.findViewById<TextView>(R.id.tv_cs_history_manual).text = it.churnScore.toString()
                view.findViewById<TextView>(R.id.tv_cltv_history_manual).text = it.cltv.toString()
            }
        }
    }
}