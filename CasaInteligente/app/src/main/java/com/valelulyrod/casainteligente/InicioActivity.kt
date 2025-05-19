package com.valelulyrod.casainteligente

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class InicioActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var tipoUsuario: String
    private lateinit var nombreUsuario: String

    private lateinit var btnCocina: Button
    private lateinit var btnSala: Button
    private lateinit var btnPersiana: Button
    private lateinit var btnAlarma: Button
    private lateinit var textBienvenida: TextView
    private lateinit var btnAlcoba: Button
    private lateinit var btnComedor: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        supportActionBar?.hide()

        tipoUsuario = intent.getStringExtra("tipoUsuario") ?: ""
        nombreUsuario = intent.getStringExtra("nombreUsuario") ?: ""

        dbRef = FirebaseDatabase.getInstance().reference

        textBienvenida = findViewById(R.id.textBienvenida)
        btnCocina = findViewById(R.id.btnCocina)
        btnSala = findViewById(R.id.btnSala)
        btnPersiana = findViewById(R.id.btnPersiana)
        btnAlarma = findViewById(R.id.btnAlarma)
        btnAlcoba = findViewById(R.id.btnAlcoba)
        btnComedor = findViewById(R.id.btnComedor)


        textBienvenida.text = "Hola, $nombreUsuario 👋"

        cargarPermisosYConfigurar()
    }

    private fun cargarPermisosYConfigurar() {
        val permisosRef = dbRef.child("usuarios").child(tipoUsuario).child(nombreUsuario).child("permisos")

        permisosRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                configurarBoton(btnCocina, "cocina", snapshot.child("luz_cocina").getValue(Boolean::class.java) == true)
                configurarBoton(btnSala, "sala", snapshot.child("luz_sala").getValue(Boolean::class.java) == true)
                configurarBoton(btnPersiana, "persiana", snapshot.child("persiana").getValue(Boolean::class.java) == true)
                configurarBoton(btnAlarma, "alarma", snapshot.child("alarma").getValue(Boolean::class.java) == true)
                configurarBoton(btnAlcoba, "alcoba", snapshot.child("luz_alcoba").getValue(Boolean::class.java) == true)
                configurarBoton(btnComedor, "comedor", snapshot.child("luz_comedor").getValue(Boolean::class.java) == true)
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error al cargar permisos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun configurarBoton(boton: Button, comando: String, habilitado: Boolean) {
        if (habilitado) {
            boton.visibility = View.VISIBLE
            boton.setOnClickListener {
                enviarComando(comando)
            }
        } else {
            boton.visibility = View.GONE
        }
    }

    private fun enviarComando(dispositivo: String) {
        val comandosRef = dbRef.child("comandos").child(dispositivo)
        comandosRef.setValue(true)

        val log = "$nombreUsuario activó $dispositivo"
        dbRef.child("logs").push().setValue(log)

        Toast.makeText(this, "Comando enviado: $dispositivo", Toast.LENGTH_SHORT).show()
    }
}
