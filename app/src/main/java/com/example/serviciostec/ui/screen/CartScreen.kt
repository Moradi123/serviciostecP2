package com.example.serviciostec.ui.screen

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import com.example.serviciostec.PagoActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.serviciostec.viewmodel.ProductoViewModel
import com.example.serviciostec.model.domain.CartItem
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    viewModel: ProductoViewModel
) {
    val carrito: List<CartItem> by viewModel.carrito.collectAsState()
    val total by viewModel.totalPagar.collectAsState()
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.limpiarCarrito()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Carrito") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    if (carrito.isNotEmpty()) {
                        IconButton(onClick = { viewModel.limpiarCarrito() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Vaciar", tint = Color.Red)
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (carrito.isNotEmpty()) {
                Surface(
                    shadowElevation = 16.dp,
                    color = Color.White
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total:", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text("$${total}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val intent = Intent(context, PagoActivity::class.java)
                                launcher.launch(intent)
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                        ) {
                            Text("Ir a Pagar", fontSize = 18.sp)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        if (carrito.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Tu carrito está vacío 🛒", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(paddingValues).fillMaxSize().background(Color(0xFFF5F5F5)),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(carrito) { item ->
                    CartItemCard(item, viewModel)
                }
            }
        }
    }
}

@Composable
fun CartItemCard(item: CartItem, viewModel: ProductoViewModel) {
    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.producto.nombre, fontWeight = FontWeight.Bold)
                Text("Unitario: $${item.producto.precio}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                Text("Subtotal: $${item.totalItem}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {

                IconButton(onClick = {
                    viewModel.restarCantidad(item)
                }) {
                    Icon(Icons.Default.Remove, contentDescription = "Restar", tint = Color.Red)
                }

                Text(
                    text = item.cantidad.toString(),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                IconButton(
                    onClick = {
                        viewModel.agregarAlCarrito(CartItem(producto = item.producto, cantidad = 1))
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Sumar", tint = Color(0xFF4CAF50))
                }
            }
        }
    }
}