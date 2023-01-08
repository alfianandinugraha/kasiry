package com.kasiry.app.models.data

data class Transaction(
    val transactionId: String,
    val products: List<TransactionProduct>,
)
