package com.example.espacocultural

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class ArtistInfoPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.artist_info_page)

        val returnButton = findViewById<Button>(R.id.returnButton)

        returnButton.setOnClickListener {
            changeScreen(this, ArtistsPage::class.java)
        }
    }

    fun changeScreen(activity: Activity, clasS: Class<*>?) {
        val intent = Intent(activity, clasS)
        startActivity(intent)
        activity.finish()
        activity.overridePendingTransition(0, 0)
    }
}