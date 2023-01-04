package com.kasiry.app.repositories

import android.app.Application
import android.content.Context
import com.kasiry.app.models.data.Category
import com.kasiry.app.utils.http.*
import okhttp3.OkHttpClient

class CategoryRepository(
    context: Context
) {
    val client = OkHttpClient
        .Builder()
        .addInterceptor(
            AccessTokenInterceptor(context)
        )
        .build()

    suspend fun getAll(): HttpState<List<Category>> {
        val request = ApiRequest
            .json(
                HttpMethod.GET,
                "/categories"
            )
            .build()

        return HttpRequest
            .create<List<Category>>(
                call = client.newCall(request),
            )
            .execute()
    }

    suspend fun create(category: Category): HttpState<Category> {
        val request = ApiRequest
            .json(
                HttpMethod.POST,
                "/categories",
                category
            )
            .build()

        return HttpRequest
            .create<Category>(
                call = client.newCall(request),
            )
            .execute()
    }

    suspend fun update(category: Category): HttpState<Category> {
        val request = ApiRequest
            .json(
                HttpMethod.PUT,
                "/categories/${category.categoryId}",
                category
            )
            .build()

        return HttpRequest
            .create<Category>(
                call = client.newCall(request),
            )
            .execute()
    }

    suspend fun delete(categoryId: String): HttpState<Any> {
        val request = ApiRequest
            .json(
                HttpMethod.DELETE,
                "/categories/$categoryId"
            )
            .build()

        return HttpRequest
            .create<Any>(
                call = client.newCall(request),
            )
            .execute()
    }

    suspend fun get(categoryId: String): HttpState<Category> {
        val request = ApiRequest
            .json(
                HttpMethod.GET,
                "/categories/$categoryId"
            )
            .build()

        return HttpRequest
            .create<Category>(
                call = client.newCall(request),
            )
            .execute()
    }
}