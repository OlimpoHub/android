package com.app.arcabyolimpo.domain.model.productbatches

/**
 * Data model representing a Product Batch in the domain layer.
 * 
 * @param idProducto: String -> product batch ID
 * @param nombre: String -> name of the product
 * @param precioUnitario: String -> unit price of the product
 * @param descripcion: String -> description of the product
 * @param imagen: String? -> image URL of the product (nullable)
 * @param disponible: Int -> availability status of the product
 * @param idInventario: String -> inventory ID associated with the product
 * @param precioVenta: String -> sale price of the product
 * @param cantidadProducida: Int -> quantity produced in the batch
 * @param fechaCaducidad: String? -> expiration date of the product (nullable)
 * @param fechaRealizacion: String -> production date of the product
 */
data class ProductBatch(
    val idProducto: String,
    val nombre: String,
    val precioUnitario: String,
    val descripcion: String,
    val imagen: String?,
    val disponible: Int,
    val idInventario: String,
    val precioVenta: String,
    val cantidadProducida: Int,
    val fechaCaducidad: String?,
    val fechaRealizacion: String,
)
