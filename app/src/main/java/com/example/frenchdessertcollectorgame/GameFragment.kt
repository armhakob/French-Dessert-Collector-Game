package com.example.frenchdessertcollectorgame

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment

class GameFragment:Fragment(R.layout.game_page), GameView.GameListener, TimerCallback{

    private var countDownTimer: CountDownTimer? = null
    private lateinit var timerTextView: TextView
    private lateinit var pauseButton: ImageButton
    private var remainingTime: Long = 30 * 1000L
    private var remainingTimeMillis: Long = 30 * 1000L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.game_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hp = view.findViewById<TextView>(R.id.heartPoint)
        hp.text = "❤ x ${a.heartPoint}"
        pauseButton = view.findViewById(R.id.pauseButton)

        timerTextView = view.findViewById(R.id.timerTextView)
        startCountdown(30)

        pauseButton.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .add(R.id.flFragment, PauseFragment())
                .addToBackStack(null)
                .commit()
            stopCountdown()
        }

        //Rectangles
        val gameView: GameView = view.findViewById(R.id.gameView)
        gameView.setGameListener(this)
        gameView.setTimerAdderListener {
//            viewModel.addTimerTime(5)
            //timerAdd(it) //for heart
        }

//        viewModel.time.observe {
//
//        }
        gameView.setTimerCallback(object: TimerCallback{
            override fun getTime(): Long {
                return remainingTimeMillis - remainingTime
            }

            override fun addTime(timeMillis: Long) {
                addTimeToCountdown(timeMillis)
            }
        })
        gameView.invalidate()
    }
    private fun startCountdown(seconds: Long) {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(seconds * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60
                timerTextView.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                timerTextView.text = "00:00"
                parentFragmentManager.beginTransaction()
                    .replace(R.id.flFragment, GameOverFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }
        countDownTimer?.start()
    }

    fun stopCountdown() {
        countDownTimer?.cancel()
        timerTextView.text = "Stopped"
    }
    private fun addTimeToCountdown(timeMillis: Long) {
        startCountdown(remainingTimeMillis + timeMillis)
    }
    override fun onAllBitmapsDeleted(unitsPassed: Int) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.flFragment, RankingsFragment())

            .addToBackStack(null)
            .commit()
    }

    override fun getTime(): Long {
        return remainingTime
    }

    override fun addTime(additionalTime: Long) {
        remainingTime += additionalTime
        startCountdown(remainingTime)
        Log.d("Time", "AAAA: $remainingTime")
    }

}
