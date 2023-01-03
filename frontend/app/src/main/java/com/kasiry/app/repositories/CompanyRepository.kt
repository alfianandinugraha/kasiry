package com.kasiry.app.repositories

import android.content.Context
import com.kasiry.app.models.data.Company
import com.kasiry.app.utils.http.*
import okhttp3.OkHttpClient

class CompanyRepository(context: Context) {
    private val client = OkHttpClient
        .Builder()
        .addInterceptor(
            AccessTokenInterceptor(context)
        )
        .build()

    suspend fun update(company: Company): HttpState<Company> {
        val request = ApiRequest
            .json(
                HttpMethod.PUT,
                "/company",
                company
            )
            .build()

        return HttpRequest
            .create<Company>(
                call = client.newCall(request),
            )
            .execute()
    }
}