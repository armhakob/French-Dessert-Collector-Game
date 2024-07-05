package com.example.frenchdessertcollectorgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class RankingsFragment:Fragment(R.layout.rankings_page) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.rankings_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val unitsTextView = view.findViewById<TextView>(R.id.units)
        val units = arguments?.getInt("unitsPassed") ?: -1
        unitsTextView.text = units.toString()

        val retryButton = view.findViewById<Button>(R.id.retryButton)

        retryButton.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, HomeFragment())
                .addToBackStack(null)
                .commit()
        }

    }

    companion object {
        fun newInstance(unitsPassed: Int): RankingsFragment {
            val fragment = RankingsFragment()
            val args = Bundle().apply {
                putInt("unitsPassed", unitsPassed)
            }
            fragment.arguments = args
            return fragment
        }
    }
}