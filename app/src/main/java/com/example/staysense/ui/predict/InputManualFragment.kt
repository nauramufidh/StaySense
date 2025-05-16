package com.example.staysense.ui.predict

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.staysense.R
import com.example.staysense.data.api.ApiConfig
import com.example.staysense.data.response.DataCostumerResponse
import com.example.staysense.data.response.PredictResponse
import com.example.staysense.databinding.FragmentInputManualBinding
import com.example.staysense.databinding.FragmentUploadFileBinding
import com.example.staysense.ui.home.SharedViewModel
import com.example.staysense.ui.main.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InputManualFragment : Fragment() {

    private var _binding: FragmentInputManualBinding? = null
    private val binding get() = _binding!!

    private lateinit var etAge: EditText
    private lateinit var etNumberOfDependents: EditText
    private lateinit var etCity: EditText
    private lateinit var etTenureInMonths: EditText
    private lateinit var etInternetService: EditText
    private lateinit var etOnlineSecurity: EditText
    private lateinit var etOnlineBackup: EditText
    private lateinit var etDeviceProtectionPlan: EditText
    private lateinit var etPremiumTechSupport: EditText
    private lateinit var etStreamingTV: EditText
    private lateinit var etStreamingMovies: EditText
    private lateinit var etStreamingMusic: EditText
    private lateinit var etUnlimitedData: EditText
    private lateinit var etContract: EditText
    private lateinit var etPaymentMethod: EditText
    private lateinit var etMonthlyCharge: EditText
    private lateinit var etTotalCharges: EditText
    private lateinit var etTotalRevenue: EditText
    private lateinit var etSatisfactionScore: EditText
    private lateinit var etChurnScore: EditText
    private lateinit var etCltv: EditText
    private lateinit var btnInputManual: Button

    private lateinit var flResult: View
    private lateinit var overlayDim: View
    private lateinit var tvMessage: TextView
    private lateinit var tvProbResult: TextView
    private lateinit var tvIsChurnResult: TextView
    private lateinit var btnOkmanual: Button

    private val sharedViewModel: SharedViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInputManualBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flResult = view.findViewById(R.id.fl_result_input_manual)
        flResult.visibility = View.GONE

        overlayDim = view.findViewById(R.id.overlayDim)
        overlayDim.visibility = View.GONE

        etAge = view.findViewById(R.id.et_input_age)
        etNumberOfDependents = view.findViewById(R.id.et_input_number_of_dependents)
        etCity = view.findViewById(R.id.et_input_city)
        etTenureInMonths = view.findViewById(R.id.et_input_tenure_in_months)
        etInternetService = view.findViewById(R.id.et_input_internet_service)
        etOnlineSecurity = view.findViewById(R.id.et_input_online_security)
        etOnlineBackup = view.findViewById(R.id.et_input_online_backup)
        etDeviceProtectionPlan = view.findViewById(R.id.et_input_device_protection_plan)
        etPremiumTechSupport = view.findViewById(R.id.et_input_premium_tech_support)
        etStreamingTV = view.findViewById(R.id.et_input_streaming_tv)
        etStreamingMovies = view.findViewById(R.id.et_input_streaming_movies)
        etStreamingMusic = view.findViewById(R.id.et_input_streaming_music)
        etUnlimitedData = view.findViewById(R.id.et_input_unlimited_data)
        etContract = view.findViewById(R.id.et_input_contract)
        etPaymentMethod = view.findViewById(R.id.et_input_payment_method)
        etMonthlyCharge = view.findViewById(R.id.et_input_monthly_charge)
        etTotalCharges = view.findViewById(R.id.et_input_total_charges)
        etTotalRevenue = view.findViewById(R.id.et_input_total_revenue)
        etSatisfactionScore = view.findViewById(R.id.et_input_satisfaction_score)
        etChurnScore = view.findViewById(R.id.et_input_churn_score)
        etCltv = view.findViewById(R.id.et_input_cltv)
        btnInputManual = view.findViewById(R.id.btn_input_manual)

        tvMessage = view.findViewById(R.id.tv_message_result_input_manual)
        tvProbResult = view.findViewById(R.id.tv_cust_churn_probability_result)
        tvIsChurnResult = view.findViewById(R.id.tv_is_churn_result)
        btnOkmanual = view.findViewById(R.id.btn_ok_input_manual)

        btnInputManual.setOnClickListener {
            showLoading(true)

            val data = DataCostumerResponse(
                age = etAge.text.toString().toIntOrNull() ?: 0,
                numberOfDependents = etNumberOfDependents.text.toString().toIntOrNull() ?: 0,
                city = etCity.text.toString().toIntOrNull() ?: 0,
                tenureInMonths = etTenureInMonths.text.toString().toIntOrNull() ?: 0,
                internetService = etInternetService.text.toString().toIntOrNull() ?: 0,
                onlineSecurity = etOnlineSecurity.text.toString().toIntOrNull() ?: 0,
                onlineBackup = etOnlineBackup.text.toString().toIntOrNull() ?: 0,
                deviceProtectionPlan = etDeviceProtectionPlan.text.toString().toIntOrNull() ?: 0,
                premiumTechSupport = etPremiumTechSupport.text.toString().toIntOrNull() ?: 0,
                streamingTv = etStreamingTV.text.toString().toIntOrNull() ?: 0,
                streamingMovies = etStreamingMovies.text.toString().toIntOrNull() ?: 0,
                streamingMusic = etStreamingMusic.text.toString().toIntOrNull() ?: 0,
                unlimitedData = etUnlimitedData.text.toString().toIntOrNull() ?: 0,
                contract = etContract.text.toString().toIntOrNull() ?: 0,
                paymentMethod = etPaymentMethod.text.toString().toIntOrNull() ?: 0,
                monthlyCharge = etMonthlyCharge.text.toString().toIntOrNull() ?: 0,
                totalCharges = etTotalCharges.text.toString().toIntOrNull() ?: 0,
                totalRevenue = etTotalRevenue.text.toString().toIntOrNull() ?: 0,
                satisfactionScore = etSatisfactionScore.text.toString().toIntOrNull() ?: 0,
                churnScore = etChurnScore.text.toString().toIntOrNull() ?: 0,
                cltv = etCltv.text.toString().toIntOrNull() ?: 0,
            )
            sendDataToServer(data)
        }
    }

    private fun sendDataToServer(data: DataCostumerResponse) {
        val client = ApiConfig.getApiService().inputData(data)
        client.enqueue(object : Callback<PredictResponse> {
            override fun onResponse(call: Call<PredictResponse>, response: Response<PredictResponse>) {
                if (response.isSuccessful) {
                    val prediction = response.body()?.prediction
                    prediction?.let {
                        val message = it.message ?: "-"
                        val prob = if (it.isChurn == true) {
                            it.churnRate ?: "-"
                        } else {
                            it.notChurnRate ?: "-"
                        }
                        val isChurn = if (it.isChurn == true) "Yes" else "No"

                        tvMessage.text = message
                        tvProbResult.text = prob
                        tvIsChurnResult.text = isChurn

                        flResult.visibility = View.VISIBLE
                        overlayDim.visibility = View.VISIBLE

                        btnOkmanual.setOnClickListener {
//                            val intent = Intent(requireContext(), MainActivity::class.java)
//                            startActivity(intent)
//                            activity?.finish()

                            clearInputField()
                            flResult.visibility = View.GONE
                            overlayDim.visibility = View.GONE

                            sharedViewModel.setUploadSuccess(true)
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed get response: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun clearInputField(){
        binding.etInputAge.setText("")
        binding.etInputCity.setText("")
        binding.etInputCltv.setText("")
        binding.etInputContract.setText("")
        binding.etInputChurnScore.setText("")
        binding.etInputDeviceProtectionPlan.setText("")
        binding.etInputInternetService.setText("")
        binding.etInputMonthlyCharge.setText("")
        binding.etInputNumberOfDependents.setText("")
        binding.etInputOnlineBackup.setText("")
        binding.etInputOnlineSecurity.setText("")
        binding.etInputPaymentMethod.setText("")
        binding.etInputPremiumTechSupport.setText("")
        binding.etInputStreamingTv.setText("")
        binding.etInputSatisfactionScore.setText("")
        binding.etInputStreamingMusic.setText("")
        binding.etInputStreamingMovies.setText("")
        binding.etInputTotalCharges.setText("")
        binding.etInputTenureInMonths.setText("")
        binding.etInputTotalRevenue.setText("")
        binding.etInputUnlimitedData.setText("")
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}