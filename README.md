ServiTec
ServiTec es una aplicación Android nativa desarrollada en Kotlin para la gestión integral de servicios automotrices. Permite a los usuarios agendar citas, registrar mantenimientos, gestionar sus vehículos y consultar el historial de reparaciones, integrando una arquitectura robusta con persistencia local y consumo de microservicios externos.

Características Principales
Autenticación Segura: Login de usuarios con validación de credenciales y roles (Cliente/Mecánico/Admin).

Gestión de Citas y Servicios: Formulario dinámico para agendar servicios (Cliente, Patente, Tipo de Servicio, Fecha).

Mis Vehículos: CRUD completo (Crear, Leer, Actualizar, Eliminar) para gestionar la flota de vehículos del usuario.

Conectividad Híbrida:

API REST: Consumo de microservicios para catálogo de productos y servicios.

Pagos: Integración con API externa (Stripe) para gestión de pagos.

Base de Datos Local: Persistencia de datos offline-first utilizando Room Database, asegurando funcionalidad sin internet.

Interfaz Moderna: UI construida 100% con Jetpack Compose siguiendo los lineamientos de Material Design 3.

Calidad de Código: Cobertura de pruebas unitarias implementadas con JUnit y MockK.

Tecnologías Utilizadas
Este proyecto sigue una arquitectura MVVM (Model-View-ViewModel) para asegurar la escalabilidad y testabilidad.

Lenguaje: Kotlin

UI: Jetpack Compose (Material 3)

Inyección de Dependencias: Manual / ViewModelFactory

Persistencia Local: Room Database (SQLite)

Red (Networking): Retrofit + GSON

Concurrencia: Corrutinas y StateFlow

Carga de Imágenes: Coil

Testing: JUnit 4, MockK, Coroutines-Test

Guía de Instalación y Ejecución
Para ejecutar el proyecto completo, debes levantar primero el entorno de Backend (Microservicios) y luego la aplicación móvil.

Prerrequisitos
Android Studio Koala o superior.

JDK 17 o superior.

Dispositivo Android físico o Emulador.

Conexión a la misma red WiFi (si usas dispositivo físico).

Paso 1: Ejecutar los Microservicios (Backend)
Navega a la carpeta de tu proyecto de Backend (Spring Boot / Java).

Asegúrate de que la base de datos del backend (MySQL/PostgreSQL) esté activa.

Ejecuta el microservicio principal. Por defecto, debería correr en el puerto 8081.

Bash

./mvnw spring-boot:run
# O desde tu IDE ejecutando la clase Main
Verifica que el backend responde entrando en tu navegador a: http://localhost:8081/api/health (o el endpoint que tengas configurado).

Paso 2: Configurar la IP en la App Android
IMPORTANTE: Para que la app se comunique con el servidor, debes configurar la dirección IP correcta.

Abre una terminal (CMD o Terminal) y obtén tu IP local:

Windows: ipconfig (Busca Dirección IPv4, ej: 192.168.1.15).

Mac/Linux: ifconfig.

En Android Studio, abre el archivo: app/src/main/java/com/example/serviciostec/model/api/RetrofitClient.kt

Modifica la variable BASE_URL con tu IP:

Kotlin

// Si usas EMULADOR de Android Studio:
private const val BASE_URL = "http://10.0.2.2:8081/"

// Si usas CELULAR FÍSICO (conectado al mismo WiFi):
private const val BASE_URL = "http://192.168.1.XX:8081/" // Reemplaza XX con tu IP
Asegúrate de mantener la barra / al final.

Paso 3: Ejecutar la Aplicación
Abre el proyecto en Android Studio.

Deja que Gradle sincronice las dependencias.

Selecciona tu dispositivo (Emulador o Físico) en la barra superior.

Presiona el botón Run

Ejecución de Pruebas (Testing)
El proyecto cuenta con pruebas unitarias para validar la lógica de negocio en los ViewModels.

Para correr los tests:

En Android Studio, ve a la carpeta src/test/java.

Haz clic derecho sobre la carpeta com.example.serviciostec.

Selecciona "Run Tests in 'serviciostec'...".

Deberías ver los resultados en verde para:

UserViewModelTest (Login, Logout, Actualización).

VehiculoViewModelTest (CRUD de vehículos).

✒️ Autores
Jorge Mora

Proyecto desarrollado para la asignatura de Desarrollo de Aplicaciones Móviles.
