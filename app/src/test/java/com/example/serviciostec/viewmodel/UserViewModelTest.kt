package com.example.serviciostec.viewmodel

import com.example.serviciostec.model.data.entities.UserEntity
import com.example.serviciostec.model.data.repository.UserRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    private lateinit var viewModel: UserViewModel
    private lateinit var repository: UserRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        viewModel = UserViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `login exitoso actualiza el userState y limpia errores`() = runTest {
        val fakeUser = UserEntity(
            id = 1,
            nombre = "Juan",
            apellido = "Perez",
            telefono = "123456789",
            usuario = "juan@test.com",
            contrasena = "pass",
            photoUri = null,
            rol = "cliente"
        )

        coEvery { repository.login("juan@test.com", "pass") } returns fakeUser

        viewModel.login("juan@test.com", "pass") {}
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(fakeUser, viewModel.userState.value)
        assertNull(viewModel.loginError.value)
    }

    @Test
    fun `login fallido actualiza el mensaje de error`() = runTest {
        coEvery { repository.login("malo@test.com", "123") } returns null

        viewModel.login("malo@test.com", "123") {}
        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.userState.value)
        assertEquals("Usuario o contraseña incorrectos", viewModel.loginError.value)
    }

    @Test
    fun `logout limpia el estado del usuario`() = runTest {
        val fakeUser = UserEntity(
            id = 1,
            nombre = "Juan",
            apellido = "Perez",
            telefono = "123",
            usuario = "a",
            contrasena = "b",
            photoUri = null,
            rol = "cliente"
        )
        coEvery { repository.login(any(), any()) } returns fakeUser
        viewModel.login("a", "b") {}
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.userState.value)
        viewModel.logout()
        assertNull(viewModel.userState.value)
    }
}