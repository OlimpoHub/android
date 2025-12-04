package com.app.arcabyolimpo.data.mapper.product

import com.app.arcabyolimpo.data.remote.dto.product.ProductDetailDto
import com.app.arcabyolimpo.domain.model.product.ProductDetail

/**
 * Maps a [ProductDetailDto] from the data layer to a [ProductDetail] domain model.
 *
 * This mapper transforms detailed product information received from the API into
 * the domain representation used throughout the application's business logic layer.
 * It includes enriched information such as workshop and category names that are
 * typically returned by joined queries or expanded API responses.
 *
 * The mapper assumes that the [ProductDetailDto] contains all necessary fields
 * from the API response, including the product identifier and related entity names.
 * This is typically used when fetching a single product with full details rather
 * than a list of products with minimal information.
 *
 * @receiver The [ProductDetailDto] instance received from the API with complete product information.
 * @return A [ProductDetail] domain model containing the mapped product information ready for business logic processing.
 */
fun ProductDetailDto.toDetailDomain(): ProductDetail {
    return ProductDetail(
        idProduct = idProduct,
        name = this.name,
        unitaryPrice = this.unitaryPrice,
        description = this.description,
        status = this.status,
        workshopName = this.workshopName,
        categoryDescription = this.categoryDescription,
        image = this.image,
    )
}