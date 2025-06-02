package com.example.staysense.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.staysense.R
import com.example.staysense.database.UserSession

import com.example.staysense.ui.profile.history.HistoryAdapter
import com.example.staysense.ui.profile.history.HistoryViewModel
import com.example.staysense.ui.profile.history.HistoryViewModelFactory

class PredictHistoryFragment : Fragment() {

    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_predict_history, container, false)
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
    }

}