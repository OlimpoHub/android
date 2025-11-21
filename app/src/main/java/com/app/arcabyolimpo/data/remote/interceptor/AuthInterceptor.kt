package com.app.arcabyolimpo.data.remote.interceptor

import com.app.arcabyolimpo.data.local.auth.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

/**
 * [AuthInterceptor] is responsible for attaching the access token to every
 * outgoing HTTP request that requires authentication.
 *
 * This class is integrated with OkHttp and automatically adds the
 * `Authorization: Bearer <token>` header if an access token is available
 * in the [UserPreferences] DataStore.
 *
 * The interceptor runs in a blocking context to synchronously fetch
 * the latest token before proceeding with the request.
 */
class AuthInterceptor(
    private val userPreferences: UserPreferences,
) : Interceptor {
    /**
     * Intercepts every outgoing HTTP request to inject the access token
     * into its headers if it exists.
     *
     * @param chain The OkHttp [Interceptor.Chain] that holds the original request.
     * @return The [Response] received after executing the request.
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val accessToken: String? =
            runBlocking {
                userPreferences.getAccessToken().first()
            }

        val requestWithAuth =
            if (!accessToken.isNullOrBlank()) {
                originalRequest
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $accessToken")
                    .build()
            } else {
                originalRequest
            }

        val response = chain.proceed(requestWithAuth)

        return response
    }
}
