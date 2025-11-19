package com.app.arcabyolimpo.domain.model.product


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
