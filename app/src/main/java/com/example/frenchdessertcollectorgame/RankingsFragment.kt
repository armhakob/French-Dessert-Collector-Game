package com.example.frenchdessertcollectorgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RankingsFragment:Fragment(R.layout.rankings_page) {
    private lateinit var scoreViewModel: ScoreViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rankings_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retryButton = view.findViewById<Button>(R.id.retryButton)

        scoreViewModel = ViewModelProvider(requireActivity()).get(ScoreViewModel::class.java)
        val score = Score(
            "",
            a.name,
            a.time,
            a.totalScore.toString(),
        )
        scoreViewModel.addUserScore(score)

        val recyclerView: RecyclerView = view.findViewById(R.id.rankingList)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val adapter = ResultAdapter(requireContext(), scoreViewModel.scoreList)
        recyclerView.adapter = adapter

        retryButton.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, HomeFragment())
                .addToBackStack(null)
                .commit()
        }

    }

}