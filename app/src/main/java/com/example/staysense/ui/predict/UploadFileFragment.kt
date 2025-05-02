package com.example.staysense.ui.predict


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.staysense.BuildConfig
import com.example.staysense.R
import com.example.staysense.data.api.ApiConfig
import com.example.staysense.data.api.ApiService
import com.example.staysense.data.response.UploadResponse
import com.example.staysense.databinding.FragmentUploadFileBinding
import com.example.staysense.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

class UploadFileFragment : Fragment() {
    private var _binding: FragmentUploadFileBinding? = null
    private val binding get() = _binding!!

    private var selectedFileUri: Uri? = null

//    private val requestPermissionLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.RequestPermission()
//        ) { isGranted: Boolean ->
//            if (isGranted) {
//                Toast.makeText(requireContext(), "Permission request granted", Toast.LENGTH_LONG)
//                    .show()
////                openFilePicker()
//                pickFile()
//            } else {
//                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_LONG)
//                    .show()
//            }
//        }

//    private fun checkPermissionAndPickFile() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            requestPermissionLauncher.launch(android.Manifest.permission.READ_MEDIA_IMAGES)
//        } else {
//            requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
//        }
//    }

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

//    private val allowedMimeTypes = listOf(
//        "text/csv",
//        "application/vnd.ms-excel",
//        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
//    )

//    private val filePickerLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val uri: Uri? = result.data?.data
//                uri?.let {
//                    val mimeType = requireContext().contentResolver.getType(uri)
//                    if (mimeType in allowedMimeTypes) {
//                        Toast.makeText(requireContext(), "File valid: $mimeType", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(requireContext(), "File tidak didukung: $mimeType", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }
//
//    private fun openFilePicker(){
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "*/*"
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, allowedMimeTypes.toTypedArray())
//        filePickerLauncher.launch(intent)
//
//    }

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

    @SuppressLint("SetTextI18n")
    private fun setupUpload(selectedFileUri: Uri) {
        val context = requireContext()
        val contentResolver = context.contentResolver

        val fileType = contentResolver.getType(selectedFileUri) ?: "text/csv"

        val inputStream = contentResolver.openInputStream(selectedFileUri)
        val tempFile = File.createTempFile("upload", ".csv", context.cacheDir)
        inputStream?.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        val requestFile = tempFile.asRequestBody(fileType.toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", tempFile.name, requestFile)

        val api = ApiConfig.getApiService()
        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    api.uploadFile(body).execute()
                }

                if (response.isSuccessful) {
                    val result = response.body()
                    result?.summary?.let {summary ->
                        Toast.makeText(context, "Upload sukses!",Toast.LENGTH_LONG).show()

                        binding.overlayDim.visibility = View.VISIBLE
                        binding.flResultUpload.visibility = View.VISIBLE

                        binding.tvTotalcustResult.text = "${summary.totalCustomers ?: 0}"
                        binding.tvTotCustChurnResult.text = "${summary.churnCount ?: 0}"
                        binding.tvTotCustNotChurnResult.text = "${summary.notChurnCount ?: 0}"
                        binding.tvChurnrateResult.text = "${summary.totalCustomers ?: 0}%"

                        binding.btnOk.setOnClickListener {
                            findNavController().popBackStack(MainActivity::class.java, false)
                        }
                    }
                } else {
                    Toast.makeText(context, "Upload gagal: ${response.code()} ${response.message()}",Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }
}