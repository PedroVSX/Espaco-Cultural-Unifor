package com.example.espacocultural

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ArtistsPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.artists_page)

        val artistButton1 = findViewById<Button>(R.id.artistButton1)
        val returnButton = findViewById<Button>(R.id.returnButton)

        artistButton1.setOnClickListener {
            changeScreen(this, ArtistInfoPage::class.java)
        }

        returnButton.setOnClickListener {
            changeScreen(this, HomePage::class.java)
        }
    }

    fun changeScreen(activity: Activity, clasS: Class<*>?) {
        val intent = Intent(activity, clasS)
        startActivity(intent)
        activity.finish()
        activity.overridePendingTransition(0, 0)
    }
}