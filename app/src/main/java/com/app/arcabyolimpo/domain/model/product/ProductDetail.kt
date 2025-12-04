package com.app.arcabyolimpo.domain.model.product

/**
 * Domain model representing comprehensive product details in the business logic layer.
 *
 * This model encapsulates complete product information typically used for detailed
 * product views, comprehensive listings, or pre-filling product edit forms. Unlike
 * the basic [Product] model, this includes enriched data such as workshop names and
 * category descriptions obtained through database joins or expanded API responses.
 *
 * The model provides a structured representation of product data that has been
 * transformed from the data layer DTOs into a clean domain format suitable for
 * business logic operations and UI presentation without data layer concerns.
 *
 * @property idProduct The unique identifier of the product used for database queries
 *                     and API requests. Required for all product operations.
 * @property name The display name of the product shown to users throughout the application.
 * @property unitaryPrice The price per unit of the product as a string, allowing flexible
 *                        currency formatting and decimal precision in the presentation layer.
 * @property description A comprehensive textual description providing details about the
 *                       product's features, materials, dimensions, usage instructions,
 *                       or other relevant information for customers.
 * @property status The current status of the product represented as an integer code
 *                  (e.g., 1 for active, 0 for inactive, 2 for discontinued). The specific
 *                  meaning of status codes is defined by the business logic layer.
 * @property workshopName The full name of the workshop that produces this product. This
 *                        enriched field provides context about the product's origin and
 *                        craftsmanship without requiring additional queries.
 * @property categoryDescription The full human-readable description of the category this
 *                               product belongs to, providing more context than just a
 *                               category ID and enhancing the user experience.
 * @property image The URL or path to the product's primary display image. Nullable to
 *                 support products without associated images, allowing the UI to display
 *                 appropriate placeholder graphics.
 */
data class ProductDetail(
    val idProduct: String,
    val name: String,
    val unitaryPrice: String,
    val description: String,
    val status: Int,
    val workshopName: String,
    val categoryDescription: String,
    val image: String?,
)