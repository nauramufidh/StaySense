package com.example.staysense.ui.wordcloud

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.staysense.data.response.WordCloudResponse
import com.example.staysense.database.UserSession
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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WordCloudFragment : Fragment() {
    private var _binding: FragmentWordCloudBinding? = null
    private val binding get() = _binding!!

    private var selectedFileUri: Uri? = null

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

        binding.cvWordcloud.setOnClickListener { checkPermissionAndPickFile() }
        binding.btnUploadWc.setOnClickListener {
            val uri = selectedFileUri
            if (uri != null){
                uploadWC()
            }else{
                Toast.makeText(requireContext(), "Please choose file first", Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launch {
            getClusteringData()
        }
    }

    private fun sendWordCloudRequest(text: String) {
        val userId = UserSession.getUserId(requireContext()) ?: ""
        val requestBody = WordCloudRequest(
            userId = userId ,
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
    
    @Suppress("DEPRECATION")
    private fun checkPermissionAndPickFile() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pickFile()
        } else if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            pickFile()
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }
    }

    @SuppressLint("Range")
    private fun getFileNameFromUri(context: Context, uri: Uri): String {
        var name = "unknown"
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                name = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            }
        }
        return name
    }

    private fun pickFile() {
        val mimeTypes = arrayOf(
            "text/csv",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        )

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        filePickerLauncher.launch(intent)
    }

    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedFileUri = uri
                val fileName = getFileNameFromUri(requireContext(), uri)
                binding.tvSelectedFileWc.text = fileName
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun uploadWC() {
        val uri = selectedFileUri ?: return

        val contentResolver = requireContext().contentResolver
        val fileName = getFileNameFromUri(requireContext(), uri)
        val inputStream = contentResolver.openInputStream(uri) ?: return
        val requestBody = inputStream.readBytes().toRequestBody(
            contentResolver.getType(uri)?.toMediaTypeOrNull()
        )

        val filePart = MultipartBody.Part.createFormData(
            "file",
            fileName,
            requestBody
        )

        val textRequestBody = "".toRequestBody("text/plain".toMediaTypeOrNull())

        showLoadingChartWc(true)

        val client = ApiConfig.getApiService().generateWordCloudWithFile(filePart, textRequestBody)
        client.enqueue(object : Callback<WordCloudResponse> {
            override fun onResponse(
                call: Call<WordCloudResponse>,
                response: Response<WordCloudResponse>
            ) {
                showLoadingChartWc(false)
                if (response.isSuccessful) {
                    val imageUrl = response.body()?.imageUrl
                    if (!imageUrl.isNullOrEmpty()) {
                        val uniqueUrl = "$imageUrl?t=${System.currentTimeMillis()}"
                        Glide.with(requireContext())
                            .load(uniqueUrl)
                            .into(binding.ivWordcloud)
                    } else {
                        Toast.makeText(requireContext(), "Image URL kosong", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Upload gagal: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<WordCloudResponse>, t: Throwable) {
                binding.progressBarWordcloud.visibility = View.GONE
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
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
            ContextCompat.getColor(requireContext(), R.color.navy_pudar),
            ContextCompat.getColor(requireContext(), R.color.dark_cyan)
        )

        val legendLabels = listOf(
            "0 - Limited Services & Device Issues",
            "1 - Customer Support Dissatisfaction",
            "2 - Data Offers & Extra Charges",
            "3 - Faster Competitor Speeds",
            "4 - Product/Service Dissatisfaction",
            "5 - Unclear/Unknown Reason",
            "6 - Better Offers from Competitors"
        )

        val dataSet = BarDataSet(entries, "")
        dataSet.colors = colors
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.BLACK
        dataSet.setDrawValues(true)

        val data = BarData(dataSet)
        data.barWidth = 0.7f

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
