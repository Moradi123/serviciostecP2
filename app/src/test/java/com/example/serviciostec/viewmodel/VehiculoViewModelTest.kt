package com.example.serviciostec.viewmodel

import com.example.serviciostec.model.data.dao.VehiculoDao
import com.example.serviciostec.model.data.entities.VehiculoEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class VehiculoViewModelTest {

    private lateinit var viewModel: VehiculoViewModel
    private lateinit var dao: VehiculoDao
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        dao = mockk(relaxed = true)

        coEvery { dao.getAllVehiculos() } returns flowOf(emptyList())

        viewModel = VehiculoViewModel(dao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `agregarVehiculo llama al insert del DAO`() = runTest {
        val nuevoAuto = VehiculoEntity(
            marca = "Toyota",
            modelo = "Yaris",
            patente = "ABCD-12",
            anio = "2020",
            usuarioId = "1"
        )

        // WHEN
        viewModel.agregarVehiculo(nuevoAuto)
        testDispatcher.scheduler.advanceUntilIdle()

        // THEN
        coVerify(exactly = 1) { dao.insertVehiculo(nuevoAuto) }
    }

    @Test
    fun `eliminarVehiculo llama al delete del DAO`() = runTest {
        val autoAborrar = VehiculoEntity(
            id = 1,
            marca = "Mazda",
            modelo = "3",
            patente = "ZZZZ-99",
            anio = "2019",
            usuarioId = "1"
        )

        viewModel.eliminarVehiculo(autoAborrar)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify(exactly = 1) { dao.deleteVehiculo(autoAborrar) }
    }
}