package com.app.arcabyolimpo.domain.model.product

/**
 * Domain model para mostrar / listar productos en la app.
 * NO reemplaza ProductAdd, lo complementa.
 */
data class Product(
    val id: String,              // idProducto
    val name: String,            // Nombre
    val unitaryPrice: String,    // PrecioUnitario
    val workshopName: String?,   // nombreTaller
    val description: String?,    // Descripcion
    val available: Boolean,      // Disponible == 1 -> true
    val imageUrl: String?,       // imagen
    val category: String?,       // idCategoria
)
