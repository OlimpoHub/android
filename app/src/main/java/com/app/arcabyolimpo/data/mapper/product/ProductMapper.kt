package com.app.arcabyolimpo.data.mapper.product

import com.app.arcabyolimpo.data.remote.dto.product.ProductDto
import com.app.arcabyolimpo.domain.model.product.Product
import java.util.UUID


/**
 * Maps a [ProductDto] from the data layer to a [Product] domain model.
 *
 * This mapper transforms product data received from the API into the domain
 * representation used throughout the application. It handles the conversion
 * of Spanish field names to English domain properties and performs necessary
 * data transformations including availability status conversion and null handling.
 *
 * The mapper includes defensive programming by generating a UUID if the product
 * identifier is unexpectedly null, logging a warning to aid in debugging potential
 * API issues. The availability field is converted from an integer representation
 * (where 1 means available) to a more intuitive boolean value.
 *
 * @receiver The [ProductDto] instance received from the API or data source.
 * @return A [Product] domain model with transformed and normalized field values.
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
