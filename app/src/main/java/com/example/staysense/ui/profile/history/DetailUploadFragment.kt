package com.example.staysense.ui.profile.history

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.staysense.R
import com.example.staysense.database.PredictionDatabase
import com.example.staysense.database.UploadHistoryDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.net.toUri


class DetailUploadFragment : Fragment() {

    private lateinit var uploadHistoryDao: UploadHistoryDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_upload, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uploadHistoryDao = PredictionDatabase.getInstance(requireContext()).uploadHistoryDao()

        val id = arguments?.getInt("historyId") ?: return

        lifecycleScope.launch {
            val data = withContext(Dispatchers.IO) {
                uploadHistoryDao.getUploadHistoryById(id)
            }

            data?.let {
                val fileUrl = it.fileUrl
                val fileUrlTV = view.findViewById<TextView>(R.id.tv_file_url_history_upload)
                fileUrlTV.text = fileUrl

                fileUrlTV.setOnClickListener {
                    openUrl(fileUrl)
                }

                view.findViewById<TextView>(R.id.tv_timestamp_history_upload).text = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date(it.timestamp))
                view.findViewById<TextView>(R.id.tv_totalcust_history_upload).text = it.totalCustomers.toString()
                view.findViewById<TextView>(R.id.tv_churncount_history_upload).text = it.churnCount.toString()
                view.findViewById<TextView>(R.id.tv_notchurncount_history_upload).text = it.notChurnCount.toString()
                view.findViewById<TextView>(R.id.tv_churnrate_history_upload).text = it.churnRate.toString()

            }
        }
    }

    private fun openUrl(url: String){
        if (URLUtil.isValidUrl(url)){
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            startActivity(intent)
        } else{
            Toast.makeText(requireContext(), "Invalid URL", Toast.LENGTH_SHORT).show()
        }
    }


}