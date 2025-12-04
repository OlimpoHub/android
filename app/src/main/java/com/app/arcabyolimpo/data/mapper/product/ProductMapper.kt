package com.app.arcabyolimpo.data.mapper.product

import com.app.arcabyolimpo.data.remote.dto.product.ProductDto
import com.app.arcabyolimpo.domain.model.product.Product
import java.util.UUID

/**
 * Extension function to convert a [ProductDto] from the data (remote) layer
 * into a [Product] domain model. This mapping centralizes the transformation
 * logic and keeps the domain layer decoupled from DTO details.
 *
 * If [idProducto] is null, a new UUID is generated and a warning is printed.
 * This guarantees that the domain model always has a stable identifier.
 */
fun ProductDto.toDomain(): Product =
    Product(
        id = idProducto ?: UUID.randomUUID().toString().also {
            println("WARNING: idProducto was null, generated ID: $it")
        },
        name = nombre,
        unitaryPrice = precioUnitario,
        workshopName = nombreTaller,
        description = descripcion,
        available = (disponible ?: 1) == 1,
        imageUrl = imagen,
        category = idCategoria,
    )
