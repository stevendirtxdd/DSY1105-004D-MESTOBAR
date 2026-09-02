package com.example.holamundo1

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // (1) Declaración de variables (Guía 7)
    val nombreUsuario: String = "Ana"
    var edadUsuario: Int = 20
    var promedioNotas: Double = 6.5
    val esMayorDeEdad: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // (3) Invocación de funciones
        val mensajeSaludo = crearSaludo(nombreUsuario, edadUsuario)
        val esMayor = calcularMayoriaEdad(edadUsuario)
        val mensajeFinal = "$mensajeSaludo\n¿Es mayor de edad?: $esMayor\nPromedio: $promedioNotas"

        mostrarResultado(mensajeFinal)
    }

    // (2) Declaración de funciones (Guía 7)
    fun crearSaludo(nombre: String, edad: Int): String {
        return "Hola $nombre, tienes $edad años."
    }

    fun calcularMayoriaEdad(edad: Int): Boolean {
        return edad >= 18
    }

    fun mostrarResultado(mensaje: String) {
        val textView = findViewById<TextView>(R.id.textView)
        textView.text = mensaje
    }
}
