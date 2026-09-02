package com.example.holamundo1

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



open class Entrada(
    val id: Int,
    val precio: Double
) {
    open fun mostrarDetalle(): String {
        return "Entrada ID: $id | Precio: $$precio"
    }
}


class EntradaGeneral(
    id: Int,
    precio: Double
) : Entrada(id, precio) {
    override fun mostrarDetalle(): String {
        return "[GENERAL] ID: $id | Precio: $$precio"
    }
}

class EntradaVIP(
    id: Int,
    precio: Double,
    val beneficiosExtra: String
) : Entrada(id, precio) {
    override fun mostrarDetalle(): String {
        return "[VIP] ID: $id | Precio: $$precio | Beneficios: $beneficiosExtra"
    }
}



sealed class EstadoValidacion {
    object Validando : EstadoValidacion()
    data class Valida(val entrada: Entrada) : EstadoValidacion()
    data class NoValida(val mensajeError: String) : EstadoValidacion()
}


class MainActivity : AppCompatActivity() {

    private lateinit var tvConsola: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvConsola = findViewById(R.id.tvConsola)


        ejecutarEvaluacion()
    }

    private fun ejecutarEvaluacion() {
        val salida = StringBuilder()


        val listaEntradas: List<Entrada> = listOf(
            EntradaGeneral(id = 101, precio = 15000.0),
            EntradaVIP(id = 102, precio = 45000.0, beneficiosExtra = "Acceso Lounge + Bebida"),
            EntradaGeneral(id = 103, precio = 15000.0),
            EntradaVIP(id = 104, precio = 50000.0, beneficiosExtra = "Meet & Greet + Estacionamiento"),
            EntradaGeneral(id = 105, precio = 15000.0)
        )

        salida.append("=== DETALLE DE ENTRADAS VENDIDAS ===\n")
        listaEntradas.forEach { entrada ->
            salida.append("${entrada.mostrarDetalle()}\n")
        }
        salida.append("\n")


        val ingresoTotal = listaEntradas.sumOf { it.precio }
        salida.append("Ingreso Total Generado: $$ingresoTotal\n")


        val cantidadVIP = listaEntradas.count { it is EntradaVIP }
        salida.append("Cantidad de Entradas VIP vendidas: $cantidadVIP\n")
        salida.append("\n-----------------------------------\n\n")


        tvConsola.text = salida.toString()


        lifecycleScope.launch {
            val idABuscar = 102


            val estadoInicial: EstadoValidacion = EstadoValidacion.Validando
            if (estadoInicial is EstadoValidacion.Validando) {
                salida.append("Estado actual: Validando entrada $idABuscar en el servidor...\n")
                tvConsola.text = salida.toString()
            }


            val resultado = validarEntrada(idABuscar, listaEntradas)


            when (resultado) {
                is EstadoValidacion.Validando -> {
                    salida.append("El sistema aún está procesando.\n")
                }
                is EstadoValidacion.Valida -> {
                    salida.append("\n✔ RESULTADO: Entrada Válida.\n")
                    salida.append("Detalle: ${resultado.entrada.mostrarDetalle()}\n")
                }
                is EstadoValidacion.NoValida -> {
                    salida.append("\n✖ RESULTADO: Validación Fallida.\n")
                    salida.append("${resultado.mensajeError}\n")
                }
            }


            tvConsola.text = salida.toString()
        }
    }


    private suspend fun validarEntrada(id: Int, listaEntradas: List<Entrada>): EstadoValidacion {
        delay(2000)
        val entradaEncontrada = listaEntradas.find { it.id == id }

        return if (entradaEncontrada != null) {
            EstadoValidacion.Valida(entradaEncontrada)
        } else {
            EstadoValidacion.NoValida("Error: La entrada con ID $id no fue encontrada.")
        }
    }
}