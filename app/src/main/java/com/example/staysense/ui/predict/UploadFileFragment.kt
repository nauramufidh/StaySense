package com.example.staysense.ui.predict


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.staysense.data.api.ApiConfig
import com.example.staysense.data.response.Summary
import com.example.staysense.data.response.UploadResponse
import com.example.staysense.database.PredictionDatabase
import com.example.staysense.database.UploadHistoryEntity
import com.example.staysense.database.UserSession
import com.example.staysense.databinding.FragmentUploadFileBinding
import com.example.staysense.ui.home.RateFragment
import com.example.staysense.ui.home.SharedViewModel
import com.example.staysense.ui.main.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UploadFileFragment : Fragment() {
    private var _binding: FragmentUploadFileBinding? = null
    private val binding get() = _binding!!

    private lateinit var database: PredictionDatabase
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var selectedFileUri: Uri? = null

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUploadFileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = PredictionDatabase.getInstance(requireContext())

        binding.llUploadFile.setOnClickListener { checkPermissionAndPickFile() }
        binding.btnUpload.setOnClickListener {
            val uri = selectedFileUri
            if (uri != null){
                setupUpload(uri)
            }else{
                Toast.makeText(requireContext(), "Please choose file first", Toast.LENGTH_SHORT).show()
            }
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

    private val filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                selectedFileUri = uri
                val fileName = getFileNameFromUri(requireContext(), uri)
                binding.tvSelectedFile.text = fileName
            }
        }
    }

    private fun pickFile() {
        val mimeTypes = arrayOf("text/*", "application/*")

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }

        filePickerLauncher.launch(intent)
    }

    @SuppressLint("SetTextI18n")
    private fun setupUpload(uri: Uri) {
        val context = requireContext()
        val contentResolver = context.contentResolver
        val fileType = contentResolver.getType(uri) ?: "text/csv"

        val inputStream = contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("upload", ".csv", context.cacheDir)
        inputStream?.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        val requestFile = tempFile.asRequestBody(fileType.toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", tempFile.name, requestFile)

        val userId = UserSession.getUserId(requireContext()) ?: ""
        val userIdRequest = userId.toRequestBody("text/plain".toMediaTypeOrNull())

        val api = ApiConfig.getApiService()
        lifecycleScope.launch {
            try {
                showLoading(true)
                val response = withContext(Dispatchers.IO) {
                    api.uploadFile(body, userIdRequest).execute()
                }

                if (response.isSuccessful) {
                    val result = response.body()


                    result?.summary?.let { summary ->

                        saveUploadHistory(userId, summary)

                        Toast.makeText(context, "Upload success!", Toast.LENGTH_LONG).show()

                        binding.overlayDim.visibility = View.VISIBLE
                        binding.flResultUpload.visibility = View.VISIBLE
                        binding.tvTotalcustResult.text = "${summary.totalCustomers ?: 0}"
                        binding.tvTotCustChurnResult.text = "${summary.churnCount ?: 0}"
                        binding.tvTotCustNotChurnResult.text = "${summary.notChurnCount ?: 0}"
                        binding.tvChurnrateResult.text = "${summary.churnRate ?: 0}"

                        binding.btnOk.setOnClickListener {
                            binding.flResultUpload.visibility = View.GONE
                            binding.overlayDim.visibility = View.GONE
                            binding.tvSelectedFile.text = "Please choose a file."
                            selectedFileUri = null

                            Log.d("UploadFragment", "User confirmed upload result, notifying sharedViewModel")
                            sharedViewModel.setUploadSuccess(true)
                        }
                    }
                } else {
                    Toast.makeText(context, "Upload Failed: ${response.code()} ${response.message()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                showLoading(false)
            }
        }
    }

    private suspend fun saveUploadHistory(userId: String, summary: Summary) {
        lifecycleScope.launch(Dispatchers.IO) {
            database.uploadHistoryDao().insert(
                UploadHistoryEntity(
                    userId = userId,
                    source = "file_upload",
                    totalCustomers = summary.totalCustomers ?: 0,
                    churnCount = summary.churnCount ?: 0,
                    notChurnCount = summary.notChurnCount ?: 0,
                    churnRate = summary.churnRate ?: "0",
                    timestamp = System.currentTimeMillis(),
                    fileUrl = summary.fileUrl ?: ""
                )
            )
        }
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }
}