package com.example.staysense.ui.predict

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.staysense.R
import com.example.staysense.data.api.ApiConfig
import com.example.staysense.data.response.DataCostumerResponse
import com.example.staysense.data.response.PredictResponse
import com.example.staysense.databinding.FragmentInputManualBinding
import com.example.staysense.ui.home.SharedViewModel
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
    private lateinit var etInternetService: AutoCompleteTextView
    private lateinit var etOnlineSecurity: AutoCompleteTextView
    private lateinit var etOnlineBackup: AutoCompleteTextView
    private lateinit var etDeviceProtectionPlan: AutoCompleteTextView
    private lateinit var etPremiumTechSupport: AutoCompleteTextView
    private lateinit var etStreamingTV: AutoCompleteTextView
    private lateinit var etStreamingMovies: AutoCompleteTextView
    private lateinit var etStreamingMusic: AutoCompleteTextView
    private lateinit var etUnlimitedData: AutoCompleteTextView
    private lateinit var etContract: AutoCompleteTextView
    private lateinit var etPaymentMethod: AutoCompleteTextView
    private lateinit var etMonthlyCharge: EditText
    private lateinit var etTotalCharges: EditText
    private lateinit var etTotalRevenue: EditText
    private lateinit var etSatisfactionScore: EditText
    private lateinit var etChurnScore: EditText
    private lateinit var etCltv: EditText
    private lateinit var btnInputManual: Button

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


        setupView(view)
        setupDropdown()
        setupButton()
    }

    private fun setupView(view: View){


        etAge = view.findViewById(R.id.et_input_age)
        etNumberOfDependents = view.findViewById(R.id.et_input_number_of_dependents)
        etCity = view.findViewById(R.id.et_input_city)
        etTenureInMonths = view.findViewById(R.id.et_input_tenure_in_months)
        etInternetService = view.findViewById(R.id.actv_input_internet_service)
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

    }

    private fun setupDropdown(){
        val listPaymentMethod = resources.getStringArray(R.array.list_payment_method)
        val listContract = resources.getStringArray(R.array.list_contract)
        val isBoolean = resources.getStringArray(R.array.isBoolean)

        val paymentMehtodAdapter = ArrayAdapter(requireContext(), R.layout.item_input, listPaymentMethod)
        etPaymentMethod.setAdapter(paymentMehtodAdapter)

        val contractAdapter = ArrayAdapter(requireContext(), R.layout.item_input, listContract)
        etContract.setAdapter(contractAdapter)

        etInternetService.setAdapter(ArrayAdapter(requireContext(), R.layout.item_input, isBoolean))
        etOnlineSecurity.setAdapter(ArrayAdapter(requireContext(), R.layout.item_input, isBoolean))
        etOnlineBackup.setAdapter(ArrayAdapter(requireContext(), R.layout.item_input, isBoolean))
        etDeviceProtectionPlan.setAdapter(ArrayAdapter(requireContext(), R.layout.item_input, isBoolean))
        etPremiumTechSupport.setAdapter(ArrayAdapter(requireContext(), R.layout.item_input, isBoolean))
        etStreamingTV.setAdapter(ArrayAdapter(requireContext(), R.layout.item_input, isBoolean))
        etStreamingMovies.setAdapter(ArrayAdapter(requireContext(), R.layout.item_input, isBoolean))
        etStreamingMusic.setAdapter(ArrayAdapter(requireContext(), R.layout.item_input, isBoolean))
        etUnlimitedData.setAdapter(ArrayAdapter(requireContext(), R.layout.item_input, isBoolean))
    }

    private fun setupButton(){
        btnInputManual.setOnClickListener {
            showLoading(true)

            val data = DataCostumerResponse(
                age = etAge.text.toString().toIntOrNull() ?: 0,
                numberOfDependents = etNumberOfDependents.text.toString().toIntOrNull() ?: 0,
                city = etCity.text.toString(),
                tenureInMonths = etTenureInMonths.text.toString().toIntOrNull() ?: 0,
                internetService = toYesNo(etInternetService.text.toString()),
                onlineSecurity = toYesNo(etOnlineSecurity.text.toString()),
                onlineBackup = toYesNo(etOnlineBackup.text.toString()),
                deviceProtectionPlan = toYesNo(etDeviceProtectionPlan.text.toString()),
                premiumTechSupport = toYesNo(etPremiumTechSupport.text.toString()),
                streamingTv = toYesNo(etStreamingTV.text.toString()),
                streamingMovies = toYesNo(etStreamingMovies.text.toString()),
                streamingMusic = toYesNo(etStreamingMusic.text.toString()),
                unlimitedData = toYesNo(etUnlimitedData.text.toString()),
                contract = etContract.text.toString(),
                paymentMethod = etPaymentMethod.text.toString(),
                monthlyCharge = etMonthlyCharge.text.toString().toDoubleOrNull() ?: 0.0,
                totalCharges = etTotalCharges.text.toString().toDoubleOrNull() ?: 0.0,
                totalRevenue = etTotalRevenue.text.toString().toDoubleOrNull() ?: 0.0,
                satisfactionScore = etSatisfactionScore.text.toString().toDoubleOrNull() ?: 0.0,
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
                    showLoading(false)
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

                        binding.flResultInputManual.visibility = View.VISIBLE
                        binding.overlayDim.visibility = View.VISIBLE

                        btnOkmanual.setOnClickListener {
//                            val intent = Intent(requireContext(), MainActivity::class.java)
//                            startActivity(intent)
//                            activity?.finish()

                            clearInputField()
                            binding.flResultInputManual.visibility = View.GONE
                            binding.overlayDim.visibility = View.GONE

                            sharedViewModel.setUploadSuccess(true)
                        }
                    }
                } else {
                    showLoading(false)
                    Log.e("API_ERROR", response.errorBody()?.string() ?: "Unknown error")
                    Toast.makeText(requireContext(), "Failed get response: ${response.code()}", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                showLoading(false)
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
        binding.actvInputInternetService.setText("")
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

    private fun toYesNo(value: String): String {
        return if (value.equals("Yes", ignoreCase = true)) "Yes" else "No"
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}