package com.example.espacocultural

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.espacocultural.models.GlobalVariables
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.FirebaseAuthCredentialsProvider
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ArtInfoPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.art_info_page)

        // Botão Voltar
        val returnButton = findViewById<Button>(R.id.returnButton)

        returnButton.setOnClickListener {
            changeScreen(this, GlobalVariables.lastPage)
        }

        // Carregamento das obras
        val progressBar = findViewById<ProgressBar>(R.id.loader)
        progressBar.visibility = View.VISIBLE

        val mainContainer = findViewById<RelativeLayout>(R.id.mainContainer)
        mainContainer.visibility = View.GONE

        // Textos da Obra
        val artName = findViewById<TextView>(R.id.artName)
        val artYear = findViewById<TextView>(R.id.artYear)
        val artAuthor = findViewById<TextView>(R.id.artAuthor)
        val artDescription = findViewById<TextView>(R.id.artDescription)
        val artImage = findViewById<ImageView>(R.id.artImage)

        // Banco de dados
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("obras").document("Mona Lisa")

        docRef.get().addOnSuccessListener {
            if (it != null) {
                val name = it.data?.get("Nome da obra")?.toString()
                val year = it.data?.get("Ano")?.toString()
                val author = it.data?.get("Autor")?.toString()
                val description = it.data?.get("Descrição")?.toString()
                val image = it.data?.get("imagem").toString()

                val imageBitmap = decodeBase64ToBitmap(image)

                artName.text = name
                artYear.text = " - " + year
                artAuthor.text = author
                artDescription.text = description
                displayBitmapInImageView(imageBitmap, artImage)

                progressBar.visibility = View.GONE
                mainContainer.visibility = View.VISIBLE
            }
        }
            .addOnFailureListener {
                Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.VISIBLE
                mainContainer.visibility = View.GONE
            }
    }

    fun changeScreen(activity: Activity, clasS: Class<*>?) {
        val intent = Intent(activity, clasS)
        startActivity(intent)
        activity.finish()
        activity.overridePendingTransition(0, 0)
    }

    // Função para decodificar a string Base64 em um objeto Bitmap
    private fun decodeBase64ToBitmap(base64String: String): Bitmap? {
        val decodedBytes: ByteArray = Base64.decode(base64String, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }

    // Função para exibir o Bitmap em uma ImageView
    private fun displayBitmapInImageView(bitmap: Bitmap?, imageView: ImageView) {
        bitmap?.let {
            imageView.setImageBitmap(it)
        }
    }
}