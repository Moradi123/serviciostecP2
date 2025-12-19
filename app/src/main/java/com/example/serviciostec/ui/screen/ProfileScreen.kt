package com.example.serviciostec.ui.screen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.material.icons.filled.*
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
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.serviciostec.model.data.entities.UserEntity
import com.example.serviciostec.viewmodel.UserViewModel
import java.io.ByteArrayOutputStream

@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {
    val userState by userViewModel.userState.collectAsState()
    val context = LocalContext.current
    var showEditDialog by remember { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null && userState != null) {
            val uri = getImageUriFromBitmap(context, bitmap)
            userViewModel.actualizarFotoPerfil(userState!!.id, uri.toString())
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context, "Se requiere permiso de cámara", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(bottomBar = {}) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState()),
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

                        val openCamera = {
                            val permission = Manifest.permission.CAMERA
                            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                                cameraLauncher.launch(null)
                            } else {
                                permissionLauncher.launch(permission)
                            }
                        }

                        if (userState?.photoUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(userState!!.photoUri),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .border(4.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
                                    .clickable { openCamera() },
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(120.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surface)
                                    .padding(16.dp)
                                    .clickable { openCamera() },
                                tint = Color.Gray
                            )
                        }

                        SmallFloatingActionButton(
                            onClick = { openCamera() },
                            containerColor = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = "Tomar Foto", tint = MaterialTheme.colorScheme.onPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "${userState?.nombre} ${userState?.apellido}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = userState?.usuario ?: "Sin Email",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                    Text(
                        text = userState?.rol?.uppercase() ?: "CLIENTE",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFFFC107),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.padding(16.dp).fillMaxWidth().height(50.dp)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null, tint = MaterialTheme.colorScheme.onError)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar Sesión", color = MaterialTheme.colorScheme.onError)
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

fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
    val bytes = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Perfil_${System.currentTimeMillis()}", null)
    return Uri.parse(path)
}

@Composable
fun EditProfileDialog(
    user: UserEntity,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String) -> Unit
) {
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
                OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") })
                OutlinedTextField(value = apellido, onValueChange = { apellido = it }, label = { Text("Apellido") })
                OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") })
                OutlinedTextField(value = usuario, onValueChange = { usuario = it }, label = { Text("Usuario / Email") })
                Divider()
                OutlinedTextField(value = pass, onValueChange = { pass = it }, label = { Text("Nueva Contraseña") }, visualTransformation = PasswordVisualTransformation())
                OutlinedTextField(value = confirmPass, onValueChange = { confirmPass = it }, label = { Text("Confirmar Contraseña") }, visualTransformation = PasswordVisualTransformation(), isError = errorMsg != null)
                if (errorMsg != null) Text(errorMsg!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
        },
        confirmButton = {
            Button(onClick = {
                if (pass.isEmpty()) errorMsg = "La contraseña no puede estar vacía"
                else if (pass != confirmPass) errorMsg = "Las contraseñas no coinciden"
                else onSave(nombre, apellido, telefono, usuario, pass)
            }) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
fun SupportItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}