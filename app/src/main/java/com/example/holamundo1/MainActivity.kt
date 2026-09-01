import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

// ==========================================
// Parte 1: Modelado del Sistema (POO)
// ==========================================

// Clase Base
open class Entrada(
    val id: Int,
    val precio: Double
) {
    open fun mostrarDetalle() {
        println("Entrada ID: $id | Precio: $$precio")
    }
}

// Clases Derivadas e Implementación de Polimorfismo
class EntradaGeneral(
    id: Int,
    precio: Double
) : Entrada(id, precio) {
    override fun mostrarDetalle() {
        println("[GENERAL] ID: $id | Precio: $$precio")
    }
}

class EntradaVIP(
    id: Int,
    precio: Double,
    val beneficiosExtra: String
) : Entrada(id, precio) {
    override fun mostrarDetalle() {
        println("[VIP] ID: $id | Precio: $$precio | Beneficios: $beneficiosExtra")
    }
}

//parte2

sealed class EstadoValidacion {
    object Validando : EstadoValidacion()
    data class Valida(val entrada: Entrada) : EstadoValidacion()
    data class NoValida(val mensajeError: String) : EstadoValidacion()
}


suspend fun validarEntrada(id: Int, listaEntradas: List<Entrada>): EstadoValidacion {
    println("Iniciando proceso de validación...")
    delay(2000)

    val entradaEncontrada = listaEntradas.find { it.id == id }

    return if (entradaEncontrada != null) {
        EstadoValidacion.Valida(entradaEncontrada)
    } else {
        EstadoValidacion.NoValida("Error: La entrada con ID $id no fue encontrada en el sistema.")
    }
}

//main

fun main() {
    // --- Parte 2: Gestión y Análisis de Datos (Colecciones) ---
    val listaEntradas: List<Entrada> = listOf(
        EntradaGeneral(id = 101, precio = 15000.0),
        EntradaVIP(id = 102, precio = 45000.0, beneficiosExtra = "Acceso a Lounge + Bebida Gratis"),
        EntradaGeneral(id = 103, precio = 15000.0),
        EntradaVIP(id = 104, precio = 50000.0, beneficiosExtra = "Meet & Greet + Estacionamiento Preferente"),
        EntradaGeneral(id = 105, precio = 15000.0)
    )

    println("=== DETALLE DE ENTRADAS VENDIDAS ===")
    listaEntradas.forEach { it.mostrarDetalle() }
    println()

    // Cálculo del ingreso total
    val ingresoTotal = listaEntradas.sumOf { it.precio }
    println("Ingreso Total Generado: $$ingresoTotal")

    // Filtrar y contar entradas VIP
    val cantidadVIP = listaEntradas.count { it is EntradaVIP }
    println("Cantidad de Entradas VIP vendidas: $cantidadVIP")
    println("\n-----------------------------------\n")

    // --- Parte 3: Ejecución Asíncrona mediante Corrutinas ---
    runBlocking {
        val idABuscar = 102 // Cambia este ID para probar un caso de éxito o fallo (ej. 999)

        // Simulación de respuesta inicial de interfaz
        val estadoInicial: EstadoValidacion = EstadoValidacion.Validando
        if (estadoInicial is EstadoValidacion.Validando) {
            println("Estado actual: Validando entrada en el servidor...")
        }

        // Llamada a la función asíncrona
        val resultado = validarEntrada(idABuscar, listaEntradas)

        // Manejo del resultado con expresión 'when'
        when (resultado) {
            is EstadoValidacion.Validando -> {
                println("El sistema aún está procesando la validación.")
            }
            is EstadoValidacion.Valida -> {
                println("✔ RESULTADO: Entrada Válida.")
                println("Detalles de la entrada confirmada:")
                resultado.entrada.mostrarDetalle()
            }
            is EstadoValidacion.NoValida -> {
                println("✖ RESULTADO: Validación Fallida.")
                println(resultado.mensajeError)
            }
        }
    }
}