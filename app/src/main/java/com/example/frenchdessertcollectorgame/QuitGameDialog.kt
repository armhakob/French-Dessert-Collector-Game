package com.example.frenchdessertcollectorgame

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class QuitGameDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Quit the game?")
                .setPositiveButton("Quit") { dialog, id ->
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.flFragment, GameOverFragment())
                        .addToBackStack(null)
                        .commit()
                }
                .setNegativeButton("Cancel") { dialog, id ->
                    // User cancelled the dialog.
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}