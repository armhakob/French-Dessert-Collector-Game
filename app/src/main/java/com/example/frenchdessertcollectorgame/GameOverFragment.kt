package com.example.frenchdessertcollectorgame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class GameOverFragment:Fragment(R.layout.game_over_page) {
    private val pointB = 10
    private val pointM = 20
    private val pointP = 30
    private val pointBone = -10
    private val pointFBone = -20
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.game_over_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nextButton = view.findViewById<Button>(R.id.nextButton)

        val b = view.findViewById<TextView>(R.id.countB)
        b.text = "X ${a.buggeteScore}"
        val bP = view.findViewById<TextView>(R.id.scoreB)
        bP.text = (a.buggeteScore * pointB).toString()
        val m = view.findViewById<TextView>(R.id.countM)
        m.text = "X ${a.macaroneScore}"
        val mP = view.findViewById<TextView>(R.id.scoreM)
        mP.text = (a.macaroneScore * pointM).toString()
        val p = view.findViewById<TextView>(R.id.countP)
        p.text = "X ${a.puffScore}"
        val pP = view.findViewById<TextView>(R.id.scoreP)
        pP.text = (a.puffScore * pointP).toString()
        val bone = view.findViewById<TextView>(R.id.countBone)
        bone.text = "X ${a.boneScore}"
        val boneP = view.findViewById<TextView>(R.id.scoreBone)
        boneP.text = (a.boneScore * pointBone).toString()
        val fBone = view.findViewById<TextView>(R.id.countFBone)
        fBone.text = "X ${a.fishboneScore}"
        val fBoneP = view.findViewById<TextView>(R.id.scoreFBone)
        fBoneP.text = (a.fishboneScore * pointFBone).toString()

        val time = view.findViewById<TextView>(R.id.time)
        time.text = a.time
        val total = view.findViewById<TextView>(R.id.totalScore)
        total.text = a.totalScore.toString()

        Log.d("GAME OVER FRAGMENT", "name: ${a.name}")

        nextButton.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.flFragment, RankingsFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}