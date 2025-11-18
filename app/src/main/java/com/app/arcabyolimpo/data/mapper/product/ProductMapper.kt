package com.app.arcabyolimpo.data.mapper.product

import com.app.arcabyolimpo.data.remote.dto.product.ProductDto
import com.app.arcabyolimpo.domain.model.product.Product
import java.util.UUID

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
