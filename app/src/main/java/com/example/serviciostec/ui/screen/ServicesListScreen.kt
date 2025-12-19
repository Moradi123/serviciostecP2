package com.example.serviciostec.ui.screen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
        ServiceItem("Cambio de Aceite", "$35.000", Icons.Default.DirectionsCar),
        ServiceItem("Alineación y Balanceo", "$25.000", Icons.Default.Build),
        ServiceItem("Revisión de Frenos", "$15.000", Icons.Default.Build),
        ServiceItem("Cambio de Batería", "$60.000", Icons.Default.DirectionsCar),
        ServiceItem("Mantención Kilometraje", "$120.000", Icons.Default.Build),
        ServiceItem("Scanner Motor", "$20.000", Icons.Default.DirectionsCar)
    )

    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = { Text("Taller ServiTec", fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color.White
                    )
                )

                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title, fontWeight = FontWeight.SemiBold) },
                            icon = {
                                Icon(
                                    if (index == 0) Icons.Default.Build else Icons.Default.ShoppingBag,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            }
        },
        bottomBar = {
            BottomNavBar(navController)
        }
    ) { paddingValues ->
        Crossfade(
            targetState = selectedTab,
            animationSpec = tween(500),
            label = "TabAnimation",
            modifier = Modifier.padding(paddingValues)
        ) { tabIndex ->

            Column(modifier = Modifier.padding(16.dp)) {
                if (tabIndex == 0) {
                    Text(
                        "Agenda tu Hora",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text("Selecciona un servicio para reservar", color = Color.Gray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
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
                        Column {
                            Text("Catálogo", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                            Text("Repuestos y accesorios", color = Color.Gray, fontSize = 14.sp)
                        }

                        FilledTonalIconButton(onClick = { navController.navigate("cart") }) {
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

                    if (productos.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize().padding(top = 50.dp),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(16.dp))
                                Text("Cargando productos...", color = Color.Gray)
                            }
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
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
    }
}

@Composable
fun ProductoItemCard(producto: ProductoEntity, onAddClick: () -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = producto.nombre.take(1).uppercase(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = producto.categoria,
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$ ${producto.precio}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { onAddClick() },
                modifier = Modifier.fillMaxWidth().height(40.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.AddShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Agregar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}