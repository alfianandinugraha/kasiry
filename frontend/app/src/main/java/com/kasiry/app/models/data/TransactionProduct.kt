package com.kasiry.app.models.data

data class TransactionProduct(
    val transactionProductId: String,
    val transactionId: String,
    val name: String,
    val quantity: Int,
    val buyPrice: Double,
    val sellPrice: Double,
    val barcode: String,
    val picture: Asset
)