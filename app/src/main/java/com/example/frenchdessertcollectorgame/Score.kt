package com.example.frenchdessertcollectorgame

import androidx.lifecycle.ViewModel

data class Score(
    val rank: String,
    val name: String,
    val time: String,
    val totalScore: String
)

class ScoreViewModel: ViewModel(){
    var scoreList: MutableList<Score> = mutableListOf()

    init {
        scoreList.add(0, Score(rank = "Rank", name = "Name", time = "Time", totalScore = "Score"))
    }

    fun addUserScore(info: Score){
        scoreList.add(1, info)
    }
}
