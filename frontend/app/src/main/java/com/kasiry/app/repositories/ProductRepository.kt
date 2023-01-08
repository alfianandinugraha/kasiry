package com.kasiry.app.repositories

import android.content.Context
import android.net.Uri
import com.kasiry.app.models.data.Product
import com.kasiry.app.utils.http.*
import okhttp3.OkHttpClient

class ProductRepository(context: Context) {
    private val client = OkHttpClient
        .Builder()
        .addInterceptor(
            AccessTokenInterceptor(context)
        )
        .build()

    data class StoreBody(
        val name: String,
        val description: String?,
        val buyPrice: Double,
        val sellPrice: Double,
        val stock: Int,
        val barcode: String?,
        val categoryId: String?,
        val pictureId: String? = null,
    )

    data class UpdateBody (
        val name: String,
        val description: String?,
        val buyPrice: Double,
        val sellPrice: Double,
        val stock: Int,
        val barcode: String?,
        val categoryId: String?,
        val pictureId: String? = null,
    )

    suspend fun store(body: StoreBody): HttpState<Product> {
        val request = ApiRequest
            .json(
                HttpMethod.POST,
                "/products",
                body
            )
            .build()

        return HttpRequest
            .create<Product>(
                call = client.newCall(request),
            )
            .execute()
    }

    suspend fun update(productId: String, body: UpdateBody): HttpState<Product> {
        val request = ApiRequest
            .json(
                HttpMethod.PUT,
                "/products/$productId",
                body
            )
            .build()

        return HttpRequest
            .create<Product>(
                call = client.newCall(request),
            )
            .execute()
    }

    suspend fun getAll(query: String? = null): HttpState<List<Product>> {
        val request = ApiRequest
            .json(
                HttpMethod.GET,
                "/products",
                params = mapOf(
                    "query" to query
                )
            )
            .build()

        return HttpRequest
            .create<List<Product>>(
                call = client.newCall(request),
            )
            .execute()
    }

    suspend fun get(productId: String): HttpState<Product> {
        val request = ApiRequest
            .json(
                HttpMethod.GET,
                "/products/$productId"
            )
            .build()

        return HttpRequest
            .create<Product>(
                call = client.newCall(request),
            )
            .execute()
    }

    suspend fun delete(productId: String): HttpState<Product> {
        val request = ApiRequest
            .json(
                HttpMethod.DELETE,
                "/products/$productId"
            )
            .build()

        return HttpRequest
            .create<Product>(
                call = client.newCall(request),
            )
            .execute()
    }
}