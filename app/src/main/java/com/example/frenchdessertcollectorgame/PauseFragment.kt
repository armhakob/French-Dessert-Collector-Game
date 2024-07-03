package com.example.frenchdessertcollectorgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment

class PauseFragment:Fragment(R.layout.pause_page) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pause_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val continueButton = view.findViewById<ImageButton>(R.id.start)
        val restartButton = view.findViewById<ImageButton>(R.id.restart)
        val quitButton = view.findViewById<ImageButton>(R.id.quit)
        quitButton.tag

        continueButton.setOnClickListener{
            setCurrentFragment(GameFragment())
            //TODO: .remove() pause fragment / resume timer
        }

        restartButton.setOnClickListener{
            setCurrentFragment(GameFragment())
        }

        quitButton.setOnClickListener{
            QuitGameDialog().show(parentFragmentManager, "QUIT_GAME_DIALOG")
        }

    }

    private fun setCurrentFragment(fragment: Fragment) =
        parentFragmentManager.beginTransaction()
            .replace(R.id.flFragment, fragment)
            .addToBackStack(null)
            .commit()
}