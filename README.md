# ServiTec 

**ServiTec** es una aplicación Android nativa desarrollada en Kotlin para la gestión de servicios automotrices. Permite a los usuarios agendar citas, registrar mantenimientos y consultar el historial de reparaciones de vehículos.

## Características

* **Autenticación de Usuarios:** Login seguro con validación de credenciales (usuario/contraseña).
* **Gestión de Servicios:** Formulario para ingresar nuevos servicios (Cliente, Patente, Tipo de Servicio, Fecha).
* **Base de Datos Local:** Persistencia de datos offline utilizando **Room Database**.
* **Interfaz Moderna:** UI construida totalmente con **Jetpack Compose** siguiendo patrones de Material Design 3.
* **Arquitectura:** Patrón **MVVM** (Model-View-ViewModel) para una separación clara de la lógica y la interfaz.

## 📱 Tecnologías Utilizadas

* [Kotlin](https://kotlinlang.org/) - Lenguaje principal.
* [Jetpack Compose](https://developer.android.com/jetpack/compose) - Kit de herramientas para UI nativa.
* [Room Database](https://developer.android.com/training/data-storage/room) - Capa de abstracción sobre SQLite.
* [ViewModel & StateFlow](https://developer.android.com/topic/libraries/architecture/viewmodel) - Gestión de estado reactivo.
* [Corrutinas](https://kotlinlang.org/docs/coroutines-overview.html) - Manejo de operaciones asíncronas.
