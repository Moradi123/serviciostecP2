package com.example.serviciostec.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.serviciostec.model.data.entities.ProductoEntity
import com.example.serviciostec.ui.components.BottomNavBar
import com.example.serviciostec.ui.components.ServiceCard
import com.example.serviciostec.ui.model.ServiceItem
import com.example.serviciostec.viewmodel.ProductoViewModel
import com.example.serviciostec.model.domain.CartItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesListScreen(navController: NavController, productoViewModel: ProductoViewModel) {

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Servicios", "Productos")

    val productos by productoViewModel.productos.collectAsState()

    val carrito: List<CartItem> by productoViewModel.carrito.collectAsState()

    val cantidadTotalItems = carrito.sumOf { it.cantidad }

    val servicesList = listOf(
        ServiceItem("Cambio de Aceite", "$35.000", Icons.Default.Build),
        ServiceItem("Alineación y Balanceo", "$25.000", Icons.Default.Build),
        ServiceItem("Revisión de Frenos", "$15.000", Icons.Default.Build),
        ServiceItem("Cambio de Batería", "$60.000", Icons.Default.Build),
        ServiceItem("Mantención Kilometraje", "$120.000", Icons.Default.Build),
        ServiceItem("Scanner Motor", "$20.000", Icons.Default.Build)
    )

    Scaffold(
        topBar = {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) },
                        icon = {
                            Icon(
                                if (index == 0) Icons.Default.Build else Icons.Default.ShoppingBag,
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        },
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            if (selectedTab == 0) {
                Text("Agenda tu Hora", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(servicesList) { service ->
                        ServiceCard(
                            service = service,
                            onAgendarClick = { navController.navigate("agendar_fecha/${service.name}") }
                        )
                    }
                }
            } else {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Catálogo", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

                    IconButton(onClick = { navController.navigate("cart") }) {
                        BadgedBox(
                            badge = {
                                if (cantidadTotalItems > 0) {
                                    Badge { Text("$cantidadTotalItems") }
                                }
                            }
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(productos) { producto ->
                        ProductoItemCard(
                            producto = producto,
                            onAddClick = {
                                productoViewModel.agregarAlCarrito(CartItem(producto = producto))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProductoItemCard(producto: ProductoEntity, onAddClick: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(40.dp), tint = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            Text(producto.nombre, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, maxLines = 1)
            Text(producto.categoria, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text("$${producto.precio}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onAddClick() },
                modifier = Modifier.fillMaxWidth().height(36.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Agregar", fontSize = 12.sp)
            }
        }
    }
}