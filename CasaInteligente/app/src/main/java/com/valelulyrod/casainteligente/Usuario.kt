package com.valelulyrod.casainteligente

data class Usuario(
    var name: String = "",
    var email: String = "",
    var uid: String = "",
    var nivelActual: String = "A1",
    var moduloActual: Int = 1,
    var temaActual: Int = 1,
    var permisos: Map<String, Boolean> = mapOf(
        "camara" to false,
        "alarma" to false,
        "luz_cocina" to false,
        "luz_sala" to false,
        "persiana" to false
    )
)