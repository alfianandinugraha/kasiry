package com.kasiry.app.repositories

import android.content.Context
import com.kasiry.app.models.data.Transaction
import com.kasiry.app.utils.http.*
import okhttp3.OkHttpClient

class TransactionRepository(
    context: Context
) {
    val client = OkHttpClient
        .Builder()
        .addInterceptor(
            AccessTokenInterceptor(context)
        )
        .build()

    data class ProductRequest(
        val quantity: Int,
        val productId: String
    )

    data class StoreBody(
        val products: List<ProductRequest>,
        val datetime: Int
    )

    suspend fun store(body: StoreBody): HttpState<Transaction> {
        val request = ApiRequest
            .json(
                HttpMethod.POST,
                "/transactions",
                body
            )
            .build()

        return HttpRequest
            .create<Transaction>(
                call = client.newCall(request),
            )
            .execute()
    }

    suspend fun getAll(
        limit: Int? = null
    ): HttpState<List<Transaction>> {
        val request = ApiRequest
            .json(
                HttpMethod.GET,
                "/transactions",
                params = mapOf(
                    "limit" to limit
                )
            )
            .build()

        return HttpRequest
            .create<List<Transaction>>(
                call = client.newCall(request),
            )
            .execute()
    }

    suspend fun detail(transactionId: String): HttpState<Transaction> {
        val request = ApiRequest
            .json(
                HttpMethod.GET,
                "/transactions/$transactionId",
            )
            .build()

        return HttpRequest
            .create<Transaction>(
                call = client.newCall(request),
            )
            .execute()
    }

    suspend fun delete(transactionId: String): HttpState<Transaction> {
        val request = ApiRequest
            .json(
                HttpMethod.DELETE,
                "/transactions/$transactionId",
            )
            .build()

        return HttpRequest
            .create<Transaction>(
                call = client.newCall(request),
            )
            .execute()
    }
}