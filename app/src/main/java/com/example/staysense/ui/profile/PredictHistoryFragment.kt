package com.example.staysense.ui.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.staysense.R
import com.example.staysense.database.UserSession
import com.example.staysense.databinding.FragmentPredictHistoryBinding
import com.example.staysense.databinding.FragmentProfileBinding

import com.example.staysense.ui.profile.history.HistoryAdapter
import com.example.staysense.ui.profile.history.HistoryViewModel
import com.example.staysense.ui.profile.history.HistoryViewModelFactory
import kotlinx.coroutines.launch

class PredictHistoryFragment : Fragment() {

    private var _binding: FragmentPredictHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPredictHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewHistory)
        adapter = HistoryAdapter { historyItem ->
            val bundle = Bundle().apply {
                putInt("historyId", historyItem.id)
            }

            val navController = findNavController()
            if (historyItem.source == "Manual Input") {
                navController.navigate(R.id.action_predictHistory_to_detailManualFragment, bundle)
            } else {
                navController.navigate(R.id.action_predictHistory_to_detailUploadFragment, bundle)
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val userId = UserSession.getUserId(requireContext()) ?: return

        viewModel = ViewModelProvider(
            this,
            HistoryViewModelFactory(requireActivity().application)
        )[HistoryViewModel::class.java]

        viewModel.loadHistory(userId)

        viewModel.combinedHistory.observe(viewLifecycleOwner) { historyList ->
            adapter.submitList(historyList)
        }

        binding.ibDelete.setOnClickListener {
            showDialogDelete(userId)
            Log.d("ToolbarClick", "Material Toolbar Clicked")
        }
    }

    private fun deleteHistory(userId: String) {
        val historyViewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)

        lifecycleScope.launch {
            showLoading(true)
            historyViewModel.deleteHistory(userId)
            Toast.makeText(requireContext(), "History Deleted Successfully", Toast.LENGTH_SHORT).show()

            viewModel.loadHistory(userId)

            viewModel.combinedHistory.observe(viewLifecycleOwner){ historyList ->
                adapter.submitList(historyList)
            }
            showLoading(false)
        }

    }

    private fun showDialogDelete(userId: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.delete_history)
            .setMessage(R.string.ask_delete_all_history)
            .setPositiveButton(R.string.agree) { _, _ ->
                deleteHistory(userId)
            }
            .setNegativeButton(R.string.disagree, null).show()
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}


