package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

/**
 * DTO used to send a new supply batch to the backend API.
 *
 * Mirrors the JSON structure expected by the backend. Property names are mapped
 * with @SerializedName to match the Spanish keys used in the API.
 *
 * Example payload:
 * {
 *   "cantidad": 10,
 *   "FechaCaducidad": "2026-11-03",
 *   "FechaCompra": "2025-11-01",
 *   "idInsumo": "i1",
 *   "adquisicion": "Compra"
 * }
 */
data class RegisterSupplyBatchDto(
    /** Supply identifier (foreign key). Maps to JSON key "idInsumo". */
    @SerializedName("idInsumo") val supplyId: String,
    /** Quantity in the batch. Maps to JSON key "cantidad". */
    @SerializedName("cantidad") val quantity: Int,
    /** Expiration date. Maps to JSON key "FechaCaducidad". Format: see backend spec. */
    @SerializedName("FechaCaducidad") val expirationDate: String,
    /** Acquisition type (e.g. "Compra", "Donación"). Maps to JSON key "adquisicion". */
    @SerializedName("adquisicion") val acquisition: String,
    /** Purchase/bought date. Maps to JSON key "FechaCompra". */
    @SerializedName("FechaCompra") val boughtDate: String,
)
