package com.valelulyrod.casainteligente

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var editNombre: EditText
    private lateinit var editCorreo: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button
    private lateinit var dbRef: DatabaseReference
    private lateinit var tipoDetectado: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        editNombre = findViewById(R.id.editNombre) // necesitas agregar este campo en el XML
        editCorreo = findViewById(R.id.editCorreo)
        btnLogin = findViewById(R.id.login)
        btnSignUp = findViewById(R.id.sing)

        dbRef = FirebaseDatabase.getInstance().getReference("usuarios")

        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val nombre = editNombre.text.toString().trim()
            val correo = editCorreo.text.toString().trim()

            if (nombre.isNotEmpty() && correo.isNotEmpty()) {
                buscarUsuario(nombre, correo)
            } else {
                Toast.makeText(this, "Ingresa nombre y correo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun buscarUsuario(nombre: String, correo: String) {
        val tipos = listOf("padres", "hijos", "invitados")

        for (tipo in tipos) {
            dbRef.child(tipo).child(nombre).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val correoRegistrado = snapshot.child("email").value.toString()
                    if (correo == correoRegistrado) {
                        tipoDetectado = tipo
                        Toast.makeText(this, "Bienvenido $nombre", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, InicioActivity::class.java)
                        intent.putExtra("tipoUsuario", tipo)
                        intent.putExtra("nombreUsuario", nombre)
                        startActivity(intent)
                        finish()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error al buscar usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }
}