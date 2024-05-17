package com.example.espacocultural

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.InputFilter
import android.util.Base64
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.espacocultural.models.Arts
import com.example.espacocultural.models.GlobalVariables
import com.example.espacocultural.models.Salons
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.io.ByteArrayOutputStream

class SalonsPage : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SalonsAdapter
    private lateinit var addImage: ImageView

    private var selectedImage: Boolean = false
    private var inOptions: Boolean = false

    val list = mutableListOf<Salons>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.salons_page)

        val db = FirebaseFirestore.getInstance()

        // Inicializa o RecyclerView
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Cria e define o adaptador para o RecyclerView
        val salonsList = list // Método para obter a lista de salões
        adapter = SalonsAdapter(salonsList)
        recyclerView.adapter = adapter

        // Botões Barra Superior & Adicionar
        val optionsButton: ConstraintLayout = findViewById(R.id.options_button) // Botão de Editar e Remover
        val addSalon: RelativeLayout = findViewById(R.id.add) // Botão para adicionar salão

        // Card de criar salão
        val outsideCard: FrameLayout = findViewById(R.id.salon_creation_background) // Layout do card
        val leaveButton: RelativeLayout = findViewById(R.id.leave_card) // Botão de sair do card
        val errorPrevention: FrameLayout = findViewById(R.id.error_prevention_background) // Card de prevenção de erros

        if (GlobalVariables.isAdmin) {
            optionsButton.visibility = View.VISIBLE
            addSalon.visibility = View.VISIBLE
        } else {
            optionsButton.visibility = View.GONE
            addSalon.visibility = View.GONE
        }

        // Clicou no "+" para adicionar salão
        addSalon.setOnClickListener {
            outsideCard.visibility = View.VISIBLE

            addImage = findViewById(R.id.add_image)
            val salonNumber: EditText = findViewById(R.id.salon_creation_number)
            val createSalon: Button = findViewById(R.id.create_salon)

            configEditTextToOnlyInteger(salonNumber)

            // Abre a galeria do celular e seleciona imagem
            addImage.setOnClickListener {
                openGallery()
            }

            createSalon.setOnClickListener {
                // Adiciona número e imagem, fazer if (se não tiver imagem ou texto, não adicionar)
                if (addImage.drawable != null && salonNumber.text.toString() != "") {
                    val salon = mapOf(
                        "Numero" to salonNumber.text.toString(),
                        "imagem" to imageViewToBase64(addImage)
                    )

                    db.collection("saloes").document("salao " + salonNumber.text.toString())
                        .set(salon)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Salão criado com sucesso!", Toast.LENGTH_SHORT).show()
                        }

                    salonsList.add(
                        Salons(
                            salonNumber.text.toString().toInt(),
                            "Salão " + salonNumber.text.toString(),
                            addImage.drawable,
                            false
                        )
                    )

                    recyclerView.adapter?.notifyItemInserted(salonsList.size - 1)

                    val ignore = mapOf(
                        "ignore" to "ignore"
                    )

                    val docRef = db.collection("saloes").document("salao " + salonNumber.text.toString())
                    docRef.collection("obras").document("ignore")
                        .set(ignore)

                    outsideCard.visibility = View.GONE
                    addImage.setImageDrawable(null)
                } else {
                    Toast.makeText(this, "O salão está sem imagem ou nome!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        optionsButton.setOnClickListener {
            // Editar, remover (pegar os botões do RecyclerView)
            if (!inOptions) {
                salonsList.forEach { it.showOptions = true }
                adapter.notifyDataSetChanged()

                val optionsImage: ImageView = findViewById(R.id.options_image)
                optionsImage.setImageResource(R.drawable.x_white)

                val params = optionsImage.layoutParams
                val density = resources.displayMetrics.density

                params.width = (20 * density).toInt()
                params.height = (20 * density).toInt()

                optionsImage.layoutParams = params
            } else {
                salonsList.forEach { it.showOptions = false }
                adapter.notifyDataSetChanged()

                val optionsImage: ImageView = findViewById(R.id.options_image)
                optionsImage.setImageResource(R.drawable.options)

                val params = optionsImage.layoutParams
                val density = resources.displayMetrics.density

                params.width = (30 * density).toInt()
                params.height = (30 * density).toInt()

                optionsImage.layoutParams = params
            }

            inOptions = !inOptions
        }

        leaveButton.setOnClickListener {
            // Prevenção de erros
            errorPrevention.visibility = View.VISIBLE
        }

        val cancelButton: Button = findViewById(R.id.cancel_button) // Botão para cancelar a operação
        val confirmButton: Button = findViewById(R.id.confirm_button) // Botão para confirmar a operação

        cancelButton.setOnClickListener {
            // Cancela a saída e volta à tela de adicionar salão
            errorPrevention.visibility = View.GONE
        }

        confirmButton.setOnClickListener {
            // Confirma a saída e volta à tela dos salões
            errorPrevention.visibility = View.GONE
            outsideCard.visibility = View.GONE
        }

        val createSalon: Button = findViewById(R.id.create_salon) // Botão para adicionar o salão dentro do RecyclerView

        createSalon.setOnClickListener {
            // Adiciona o salão no RecyclerView
        }

        // Botões Barra Inferior
        val homeButton = findViewById<Button>(R.id.homeButton)
        val qrButton = findViewById<Button>(R.id.qrButton)
        val settingsButton = findViewById<Button>(R.id.settingsButton)

        homeButton.setOnClickListener{
            changeScreen(this, HomePage::class.java)
        }

        qrButton.setOnClickListener{
            changeScreen(this, QrPage::class.java)
        }

        settingsButton.setOnClickListener{
            changeScreen(this, SettingsPage::class.java)
        }
    }

    fun changeScreen(activity: Activity, clasS: Class<*>?) {
        GlobalVariables.lastPage = activity::class.java
        val intent = Intent(activity, clasS)
        startActivity(intent)
        activity.finish()
        activity.overridePendingTransition(0, 0); // Definindo nenhuma animação
    }

//    private fun getListOfSalons(): List<Salons> {
//        // Aqui você deve retornar a lista de objetos Salons com os dados do seu banco de dados ou de onde quer que venham os dados
//        // Por enquanto, vamos retornar uma lista de exemplos
//        val list = mutableListOf<Salons>()
//        //list.add(Salons(1,"Salão 1", R.drawable.salao_exemplo))
//
//        // Adicione mais salões conforme necessário
//        return list
//    }

    private fun openGallery() {
        // Cria um intent para abrir a galeria
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        // Inicia a atividade da galeria com o resultado esperado
        resultLauncher.launch(intent)
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // Imagem selecionada com sucesso
            val data: Intent? = result.data
            val selectedImageUri = data?.data

            // Carrega a imagem selecionada no ImageView usando Glide
            selectedImageUri?.let {
                Glide.with(this)
                    .load(it)
                    .into(addImage)

                selectedImage = true
            }
        }
    }

    private fun imageViewToBase64(imageView: ImageView): String {
        // Obtém o drawable da imageView
        val drawable = imageView.drawable as BitmapDrawable
        // Obtém o bitmap do drawable
        val bitmap = drawable.bitmap
        // Converte o bitmap em uma string Base64
        return bitmapToBase64(bitmap)
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun configEditTextToOnlyInteger(editText: EditText) {
        val inputFilter = InputFilter { source, start, end, dest, dstart, dend ->
            for (i in start until end) {
                if (!Character.isDigit(source[i])) {
                    return@InputFilter ""
                }
            }
            null
        }

        val maxLength = 9 // Defina o número máximo de dígitos permitidos (opcional)

        // Aplica o filtro de entrada para aceitar apenas números inteiros
        editText.filters = arrayOf(inputFilter)

        // Define o número máximo de caracteres (opcional)
        editText.filters += InputFilter.LengthFilter(maxLength)
    }
}