package com.example.serviciostec.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.serviciostec.model.data.entities.UserEntity
import com.example.serviciostec.viewmodel.UserViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {
    val userState by userViewModel.userState.collectAsState()
    val context = LocalContext.current

    var showEditDialog by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null && userState != null) {
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            try {
                context.contentResolver.takePersistableUriPermission(uri, flag)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            userViewModel.actualizarFotoPerfil(userState!!.id, uri.toString())
        }
    }

    Scaffold(
        bottomBar = {}
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState()), // Hacemos scrollable la pantalla
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(contentAlignment = Alignment.BottomEnd) {
                        if (userState?.photoUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(userState!!.photoUri),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .border(4.dp, Color.White, CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .padding(16.dp),
                                tint = Color.Gray
                            )
                        }
                        SmallFloatingActionButton(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            containerColor = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = "Cambiar Foto", tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = userState?.nombre ?: "Usuario",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = userState?.usuario ?: "Sin Email",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Centro de Ayuda",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp).align(Alignment.Start)
            )

            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column {
                    SupportItem(Icons.Default.Call, "Llamar a Soporte") {
                        val intent = Intent(Intent.ACTION_DIAL).apply { data = Uri.parse("tel:+56912345678") }
                        context.startActivity(intent)
                    }
                    Divider(color = Color.LightGray.copy(alpha = 0.2f))
                    SupportItem(Icons.Default.Email, "Enviar Correo") {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:soporte@servitec.cl")
                            putExtra(Intent.EXTRA_SUBJECT, "Ayuda App ServiTec")
                        }
                        try { context.startActivity(intent) } catch (e: Exception) {}
                    }

                    Divider(color = Color.LightGray.copy(alpha = 0.2f))
                    SupportItem(Icons.Default.Edit, "Modificar Perfil") {
                        showEditDialog = true
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    userViewModel.logout()
                    navController.navigate("login") { popUpTo(0) }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                modifier = Modifier.padding(16.dp).fillMaxWidth().height(50.dp)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión")
            }
        }

        if (showEditDialog && userState != null) {
            EditProfileDialog(
                user = userState!!,
                onDismiss = { showEditDialog = false },
                onSave = { nombre, apellido, telefono, usuario, contra ->
                    userViewModel.updateUser(nombre, apellido, telefono, usuario, contra)
                    showEditDialog = false
                }
            )
        }
    }
}

@Composable
fun EditProfileDialog(
    user: UserEntity,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String) -> Unit
) {
    // Estados inicializados con los datos actuales
    var nombre by remember { mutableStateOf(user.nombre) }
    var apellido by remember { mutableStateOf(user.apellido) }
    var telefono by remember { mutableStateOf(user.telefono) }
    var usuario by remember { mutableStateOf(user.usuario) }
    var pass by remember { mutableStateOf(user.contrasena) }
    var confirmPass by remember { mutableStateOf(user.contrasena) }

    var errorMsg by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Datos Personales", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nombre, onValueChange = { nombre = it },
                    label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = apellido, onValueChange = { apellido = it },
                    label = { Text("Apellido") }, modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = telefono, onValueChange = { telefono = it },
                    label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = usuario, onValueChange = { usuario = it },
                    label = { Text("Usuario / Email") }, modifier = Modifier.fillMaxWidth()
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Seguridad", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)

                OutlinedTextField(
                    value = pass, onValueChange = { pass = it },
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = confirmPass, onValueChange = { confirmPass = it },
                    label = { Text("Confirmar Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = errorMsg != null,
                    modifier = Modifier.fillMaxWidth()
                )

                if (errorMsg != null) {
                    Text(text = errorMsg!!, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (pass.isEmpty()) {
                    errorMsg = "La contraseña no puede estar vacía"
                } else if (pass != confirmPass) {
                    errorMsg = "Las contraseñas no coinciden"
                } else {
                    onSave(nombre, apellido, telefono, usuario, pass)
                }
            }) {
                Text("Guardar Cambios")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun SupportItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}