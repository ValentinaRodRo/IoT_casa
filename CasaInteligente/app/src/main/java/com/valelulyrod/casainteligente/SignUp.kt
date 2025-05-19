package com.valelulyrod.casainteligente

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var editNombre: EditText
    private lateinit var editCorreo: EditText
    private lateinit var spinnerTipo: Spinner
    private lateinit var btnSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()

        editNombre = findViewById(R.id.editNombre)
        editCorreo = findViewById(R.id.editCorreo)
        spinnerTipo = findViewById(R.id.spinnerTipo)
        btnSignUp = findViewById(R.id.sing)

        // Tipos de usuario
        val tipos = arrayOf("padres", "hijos", "invitados")
        spinnerTipo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipos)

        btnSignUp.setOnClickListener {
            val nombre = editNombre.text.toString().trim()
            val correo = editCorreo.text.toString().trim()
            val tipo = spinnerTipo.selectedItem.toString()

            if (nombre.isNotEmpty() && correo.isNotEmpty()) {
                val permisos = getPermisosPorTipo(tipo)
                val usuario = mapOf(
                    "nombre" to nombre,
                    "email" to correo,
                    "permisos" to permisos
                )

                // Guarda en la ruta /usuarios/<tipo>/<nombre>
                val ref = FirebaseDatabase.getInstance().getReference("usuarios/$tipo/$nombre")
                ref.setValue(usuario).addOnSuccessListener {
                    Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getPermisosPorTipo(tipo: String): Map<String, Boolean> {
        return when (tipo) {
            "padres" -> mapOf(
                "camara" to true,
                "alarma" to true,
                "luz_cocina" to true,
                "luz_sala" to true,
                "luz_comedor" to true,
                "luz_alcoba" to true,
                "persiana" to true
            )
            "hijos" -> mapOf(
                "camara" to false,
                "alarma" to false,
                "luz_cocina" to true,
                "luz_sala" to true,
                "luz_comedor" to true,
                "luz_alcoba" to true,
                "persiana" to false
            )
            "invitados" -> mapOf(
                "camara" to false,
                "alarma" to false,
                "luz_cocina" to true,
                "luz_sala" to false,
                "luz_comedor" to true,
                "luz_alcoba" to false,
                "persiana" to false
            )
            else -> emptyMap()
        }
    }
}

