package com.app.arcabyolimpo.data.mapper.product

import com.app.arcabyolimpo.data.remote.dto.product.ProductDetailDto
import com.app.arcabyolimpo.domain.model.product.ProductDetail

/**
 * Extension function to map a ProductDto (Data Layer) into a Product (Domain Layer).
 * * Note: Assumes ProductDto includes all necessary fields (e.g., idProduct) from the API response.
 */
fun ProductDetailDto.toDetailDomain(): ProductDetail =
    ProductDetail(
        idProduct = idProduct,
        name = this.name,
        unitaryPrice = this.unitaryPrice,
        description = this.description,
        status = this.status,
        workshopName = this.workshopName,
        categoryDescription = this.categoryDescription,
        image = this.image,
    )
