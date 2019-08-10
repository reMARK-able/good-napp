package com.gmail.remarkable.development.goodnapp

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var setTarget: ImageButton
    lateinit var timeTarget: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Init views
        setTarget = findViewById(R.id.set_cccTarget_button)
        timeTarget = findViewById(R.id.time_cccTarget)

        setTarget.setOnClickListener { setTime() }
    }

    // Mock of a method to see the changes to the layout
    private fun setTime() {
        timeTarget.text = "06:30"
    }
}
