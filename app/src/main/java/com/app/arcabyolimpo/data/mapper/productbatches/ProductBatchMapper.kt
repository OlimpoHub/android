package com.app.arcabyolimpo.data.mapper.productbatches

import com.app.arcabyolimpo.data.remote.dto.productbatches.ProductBatchDto
import com.app.arcabyolimpo.domain.model.productbatches.ProductBatch

/**
 * Mapping the Dto to Domain model and viceversa
 *
 * @return ProductBatch
 */
fun ProductBatchDto.toDomain(): ProductBatch =
    ProductBatch(
        idProducto = idProducto,
        nombre = nombre,
        precioUnitario = precioUnitario,
        descripcion = descripcion,
        imagen = imagen,
        disponible = disponible,
        idInventario = idInventario,
        precioVenta = precioVenta,
        cantidadProducida = cantidadProducida,
        fechaCaducidad = fechaCaducidad,
        fechaRealizacion = fechaRealizacion,
    )

fun ProductBatch.toDto(): ProductBatchDto =
    ProductBatchDto(
        idProducto = idProducto,
        nombre = nombre,
        precioUnitario = precioUnitario,
        descripcion = descripcion,
        imagen = imagen,
        disponible = disponible,
        idInventario = idInventario,
        precioVenta = precioVenta,
        cantidadProducida = cantidadProducida,
        fechaCaducidad = fechaCaducidad,
        fechaRealizacion = fechaRealizacion,
    )
