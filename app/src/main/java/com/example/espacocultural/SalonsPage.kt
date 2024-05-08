package com.example.espacocultural

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.espacocultural.models.GlobalVariables
import com.example.espacocultural.models.Salons

class SalonsPage : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SalonsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.salons_page)

        // Inicializa o RecyclerView
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        // Cria e define o adaptador para o RecyclerView
        val salonsList = getListOfSalons() // Método para obter a lista de salões
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

        addSalon.setOnClickListener {
            outsideCard.visibility = View.VISIBLE
        }

        optionsButton.setOnClickListener {
            // Editar, remover
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

    private fun getListOfSalons(): List<Salons> {
        // Aqui você deve retornar a lista de objetos Salons com os dados do seu banco de dados ou de onde quer que venham os dados
        // Por enquanto, vamos retornar uma lista de exemplos
        val list = mutableListOf<Salons>()
        list.add(Salons(1,"Salão 1", R.drawable.salao_exemplo))
        list.add(Salons(2,"Salão 2", R.drawable.salao_exemplo))
        list.add(Salons(3,"Salão 3", R.drawable.salao_exemplo))
        list.add(Salons(4,"Salão 4", R.drawable.salao_exemplo))
        list.add(Salons(5,"Salão 5", R.drawable.salao_exemplo))
        list.add(Salons(6,"Salão 6", R.drawable.salao_exemplo))
        list.add(Salons(7,"Salão 7", R.drawable.salao_exemplo))
        list.add(Salons(8,"Salão 8", R.drawable.salao_exemplo))
        // Adicione mais salões conforme necessário
        return list
    }
}