package com.example.staysense.ui.wordcloud

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.staysense.databinding.FragmentWordCloudBinding
import com.example.staysense.R
import com.jolenechong.wordcloud.WordCloud


class WordCloudFragment : Fragment() {
    private var _binding: FragmentWordCloudBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWordCloudBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val wordCloudContainer = view.findViewById<FrameLayout>(R.id.wordCloudView)

        val wordCloudView = WordCloud(requireContext(), null)

        wordCloudContainer.addView(wordCloudView)

        wordCloudView.setWords(
            arrayListOf(
                "human",
                "tasks",
                "tasks",
                "AI",
                "AI",
                "AI",
                "AI",
                "systems",
                "systems",
                "makan",
                "tasks",
                "AI",
                "AI",
                "AI",
                "AI",
                "systems",
                "sabun",
                "tasks",
                "AI",
                "AI",
                "AI",
                "AI",
                "systems",
                "mandi",
                "tasks",
                "AI",
                "AI",
                "AI",
                "AI",
                "systems",
                "systems",
                "tasks",
                "AI",
                "yayayay",
                "AI",
                "happy",
                "systems",
                "sad",
            ),
        )
    }
}