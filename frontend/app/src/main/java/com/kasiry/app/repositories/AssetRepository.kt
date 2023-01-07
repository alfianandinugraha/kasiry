package com.kasiry.app.repositories

import android.content.Context
import com.kasiry.app.models.data.Asset
import com.kasiry.app.utils.http.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.InputStream

class AssetRepository(
    context: Context
) {
    private val client = OkHttpClient
        .Builder()
        .addInterceptor(
            AccessTokenInterceptor(context)
        )
        .build()

    data class UploadBody(
        val fileName: String,
        val file: InputStream,
    )

    suspend fun upload(body: UploadBody): HttpState<Asset> {
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("fileName", body.fileName)
            .addFormDataPart(
                "file",
                body.fileName,
                body.file.readBytes().toRequestBody()
            )
            .build()

        val request = ApiRequest
            .formData(
                HttpMethod.POST,
                "/assets",
                multipartBody
            )
            .build()

        return HttpRequest
            .create<Asset>(
                call = client.newCall(request)
            )
            .execute()
    }
}