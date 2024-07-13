package com.example.frenchdessertcollectorgame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class UsernameFragment:Fragment(R.layout.username_page) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.username_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editText = view.findViewById<EditText>(R.id.nameEditText)
        val buttonStart = view.findViewById<Button>(R.id.button)


        editText.clearFocus()
        buttonStart.setOnClickListener{
            val name = editText.text.toString()
            if (name.isNotEmpty()){
                a.name = name
//                val gameFragment = GameFragment.newInstance(name)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.flFragment, GameFragment())
                    .addToBackStack(null)
                    .commit()
                Log.d("Name: ", name)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please enter your name.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}