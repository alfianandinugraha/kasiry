package com.kasiry.app.models.data

data class Product(
    val productId: String,
    val name: String,
    val description: String?,
    val weight: String?,
    val buyPrice: Double,
    val sellPrice: Double,
    val stock: Double,
    val barcode: String?,
    val category: Category?,
    val company: Company,
    val picture: Asset,
) {
    override fun toString(): String {
        return "Product(productId='$productId', name='$name', weight='$weight', buyPrice=$buyPrice, sellPrice=$sellPrice, stock=$stock, barcode='$barcode', category=$category, company=$company, picture=$picture)"
    }
}

