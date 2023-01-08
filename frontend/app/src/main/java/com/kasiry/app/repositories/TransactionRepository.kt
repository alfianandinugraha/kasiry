package com.kasiry.app.repositories

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
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

    data class StoreBody (
        val products: List<ProductRequest>
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

    suspend fun getAll(): HttpState<List<Transaction>> {
        val request = ApiRequest
            .json(
                HttpMethod.GET,
                "/transactions",
            )
            .build()

        return HttpRequest
            .create<List<Transaction>>(
                call = client.newCall(request),
            )
            .execute()
    }
}