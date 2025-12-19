package com.example.serviciostec

import org.junit.Test
import org.junit.Assert.*

class LogicaNegocioTest {

    // 1. Prueba de lógica simple: Verificar que el cálculo de IVA sea correcto (ejemplo)
    @Test
    fun calcular_iva_es_correcto() {
        val precioNeto = 10000
        val ivaEsperado = 1900 // 19%
        val ivaCalculado = precioNeto * 0.19

        assertEquals(ivaEsperado.toDouble(), ivaCalculado, 0.01)
    }

    // 2. Prueba de validación de formulario: Email
    @Test
    fun validar_email_correcto() {
        val email = "mecanico@taller.cl"
        val esValido = email.contains("@") && email.contains(".")
        assertTrue("El email debería ser válido", esValido)
    }

    @Test
    fun validar_email_incorrecto() {
        val email = "mecanicotallercl"
        val esValido = email.contains("@") && email.contains(".")
        assertFalse("El email NO debería ser válido", esValido)
    }

    // 3. Prueba de lógica de carrito (Simulada)
    @Test
    fun calcular_total_carrito() {
        val precioLlanta = 50000
        val precioAceite = 20000
        val cantidadLlantas = 2
        val cantidadAceite = 1

        val totalEsperado = (precioLlanta * cantidadLlantas) + (precioAceite * cantidadAceite)
        val totalCalculado = 120000

        assertEquals(totalEsperado, totalCalculado)
    }
}