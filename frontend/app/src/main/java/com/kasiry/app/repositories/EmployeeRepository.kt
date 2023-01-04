package com.kasiry.app.repositories

import android.content.Context
import com.kasiry.app.models.data.Ability
import com.kasiry.app.models.data.Employee
import com.kasiry.app.utils.http.*
import okhttp3.OkHttpClient

class EmployeeRepository(
    context: Context
) {
    private val client = OkHttpClient
        .Builder()
        .addInterceptor(
            AccessTokenInterceptor(context)
        )
        .build()

    data class CreateBody (
        val name: String,
        val email: String,
        val phone: String,
        val password: String,
        val abilities: Ability,
    )

    data class UpdateBody (
        val userId: String,
        val name: String,
        val email: String,
        val phone: String,
        val abilities: Ability,
    )

    suspend fun create(employee: CreateBody): HttpState<Employee> {
        val request = ApiRequest
            .json(
                HttpMethod.POST,
                "/employees",
                employee
            )
            .build()

        return HttpRequest
            .create<Employee>(
                call = client.newCall(request),
            )
            .execute()
    }

    suspend fun update(employee: UpdateBody): HttpState<Employee> {
        val request = ApiRequest
            .json(
                HttpMethod.PUT,
                "/employees/${employee.userId}",
                employee
            )
            .build()

        return HttpRequest
            .create<Employee>(
                call = client.newCall(request),
            )
            .execute()
    }

    suspend fun getAll(): HttpState<List<Employee>> {
        val request = ApiRequest
            .json(
                HttpMethod.GET,
                "/employees",
            )
            .build()

        return HttpRequest
            .create<List<Employee>>(
                call = client.newCall(request),
            )
            .execute()
    }

    suspend fun get(userId: String) : HttpState<Employee> {
        val request = ApiRequest
            .json(
                HttpMethod.GET,
                "/employees/$userId",
            )
            .build()

        return HttpRequest
            .create<Employee>(
                call = client.newCall(request),
            )
            .execute()
    }

    suspend fun delete(userId: String) : HttpState<Any> {
        val request = ApiRequest
            .json(
                HttpMethod.DELETE,
                "/employees/$userId",
            )
            .build()

        return HttpRequest
            .create<Any>(
                call = client.newCall(request),
            )
            .execute()
    }
}