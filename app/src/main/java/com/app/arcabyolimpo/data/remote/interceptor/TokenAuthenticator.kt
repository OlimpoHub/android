package com.app.arcabyolimpo.data.remote.interceptor

import android.util.Log
import com.app.arcabyolimpo.data.local.auth.UserPreferences
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.dto.auth.RefreshRequestDto
import com.app.arcabyolimpo.data.remote.dto.auth.RefreshResponseDto
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Singleton

/**
 * [TokenAuthenticator] automatically handles token renewal when a request fails
 * due to an expired or invalid access token (when the server returns a 401 error).
 *
 * It intercepts authentication failures, refreshes the token using the refresh token,
 * updates the stored credentials, and retries the failed request with the new access token.
 *
 * If the refresh process fails, the authenticator logs the user out by clearing
 * session data and redirecting the user to the login screen through [SessionManager].
 *
 * This class is designed to work seamlessly with [AuthInterceptor] and Retrofit.
 */
@Singleton
class TokenAuthenticator(
    private val userPreferences: UserPreferences,
    private val sessionManager: SessionManager,
    private val baseUrl: String,
) : Authenticator {
    private val refreshMutex = Mutex()

    /**
     * Called automatically by OkHttp when a request receives an HTTP 401 (Unauthorized) response.
     * This function tries to refresh the access token and retry the original request.
     *
     * @param route The network route.
     * @param response The failed [Response] that triggered authentication.
     * @return A new [Request] with an updated token if successful, or null if re-authentication fails.
     */
    override fun authenticate(
        route: Route?,
        response: Response,
    ): Request? {
        if (responseCount(response) >= 2) {
            runBlocking { sessionManager.logout() }
            return null
        }

        return runBlocking {
            refreshMutex.withLock {
                try {
                    val currentRefreshToken = userPreferences.getRefreshToken().first()
                    if (currentRefreshToken.isNullOrBlank()) {
                        sessionManager.logout()
                        return@runBlocking null
                    }

                    val client = OkHttpClient.Builder().build()
                    val retrofit =
                        Retrofit
                            .Builder()
                            .baseUrl(baseUrl)
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()

                    val refreshApi = retrofit.create(ArcaApi::class.java)

                    val refreshResponse: RefreshResponseDto =
                        try {
                            refreshApi.refresh(RefreshRequestDto(currentRefreshToken))
                        } catch (e: Exception) {
                            sessionManager.logout()
                            return@runBlocking null
                        }

                    userPreferences.updateAccessToken(
                        newToken = refreshResponse.accessToken,
                    )

                    val requestWithNewToken =
                        response.request
                            .newBuilder()
                            .header("Authorization", "Bearer ${refreshResponse.accessToken}")
                            .build()

                    return@runBlocking requestWithNewToken
                } catch (e: IOException) {
                    sessionManager.logout()
                    return@runBlocking null
                } catch (e: Exception) {
                    sessionManager.logout()
                    return@runBlocking null
                }
            }
        }
    }

    /**
     * Counts the number of times a request has been attempted by following
     * the chain of prior responses.
     *
     * @param response The current [Response].
     * @return The number of times the same request has been retried.
     */
    private fun responseCount(response: Response): Int {
        var res: Response? = response
        var result = 1
        while (res?.priorResponse != null) {
            result++
            res = res.priorResponse
        }
        return result
    }
}
