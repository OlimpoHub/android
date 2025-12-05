package com.app.arcabyolimpo.domain.model.product

/**
 * Domain model representing a product in the business logic layer.
 *
 * This model provides a clean, normalized representation of product data used
 * throughout the application's domain and presentation layers. Unlike the data
 * layer DTOs, this model uses English property names, boolean types for flags,
 * and provides a consistent interface regardless of the underlying data source.
 *
 * The model focuses on the essential product information needed for business
 * logic operations and UI presentation, abstracting away data layer concerns
 * such as serialization formats or database field mappings.
 *
 * @property id The unique identifier of the product. This is guaranteed to be
 *              non-null through mapper logic that generates a UUID if the source
 *              data lacks an identifier.
 * @property name The display name of the product shown to users throughout the application.
 * @property unitaryPrice The price per unit of the product as a string, allowing flexible
 *                        display formatting and currency handling in the presentation layer.
 * @property workshopName The name of the workshop that produces this product. Nullable for
 *                        products not yet associated with a workshop or when workshop
 *                        information is unavailable.
 * @property description A textual description providing details about the product's features,
 *                       materials, dimensions, or usage. Nullable for products without
 *                       detailed descriptions.
 * @property available A boolean flag indicating whether the product is currently available
 *                     for purchase. Converted from integer representation in the data layer
 *                     for more intuitive business logic and UI binding.
 * @property imageUrl The URL or path to the product's image for display purposes. Nullable
 *                    to support products without images, allowing the UI to show placeholders.
 * @property category The identifier of the category this product belongs to. Nullable for
 *                    uncategorized products or when category information is not required.
 */

data class Product(
    val id: String,
    val name: String,          
    val unitaryPrice: String,    
    val workshopName: String?,  
    val description: String?,    
    val available: Boolean,      
    val imageUrl: String?,      
    val category: String?,       
)
