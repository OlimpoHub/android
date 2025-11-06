package com.app.arcabyolimpo.di

import android.content.Context
import com.app.arcabyolimpo.data.local.auth.UserPreferences
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.interceptor.AuthInterceptor
import com.app.arcabyolimpo.data.remote.interceptor.SessionManager
import com.app.arcabyolimpo.data.remote.interceptor.TokenAuthenticator
import com.app.arcabyolimpo.data.repository.auth.UserRepositoryImpl
import com.app.arcabyolimpo.data.repository.password.PasswordPasswordUserRepositoryImpl
import com.app.arcabyolimpo.data.repository.supplies.SupplyRepositoryImpl
import com.app.arcabyolimpo.domain.repository.auth.UserRepository
import com.app.arcabyolimpo.domain.repository.password.PasswordUserRepository
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/** Dependency Injection (DI) module for providing app-wide singletons. */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "http://10.0.2.2:8080/" // LOCALHOST

    /**
     * Provides a configured [OkHttpClient] instance.
     *
     * Includes:
     * - [AuthInterceptor] to attach the access token to outgoing requests.
     * - [TokenAuthenticator] to automatically refresh tokens when expired.
     *
     * @param authPreferences Used by the interceptor and authenticator to retrieve stored tokens.
     * @param sessionManager Handles logout and session expiration events.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        authPreferences: UserPreferences,
        sessionManager: SessionManager,
    ): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(AuthInterceptor(authPreferences))
            .authenticator(TokenAuthenticator(authPreferences, sessionManager, BASE_URL))
            .build()

    /**
     * Provides a singleton [Retrofit] instance configured with:
     * - The base API URL.
     * - A JSON converter for serialization/deserialization.
     * - The custom OkHttpClient for request handling.
     */
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    /**
     * Provides the main API service for making network requests.
     *
     * @param retrofit The configured Retrofit instance.
     */
    @Provides
    @Singleton
    fun provideArcaApi(retrofit: Retrofit): ArcaApi = retrofit.create(ArcaApi::class.java)

    /** Provides a singleton instance of [UserPreferences]. */
    @Provides
    @Singleton
    fun provideUserPreferences(
        @ApplicationContext context: Context,
    ): UserPreferences = UserPreferences(context)

    /** Provides the [UserRepository] implementation. */
    @Provides
    @Singleton
    fun provideUserRepository(
        api: ArcaApi,
        authPreferences: UserPreferences,
    ): UserRepository = UserRepositoryImpl(api, authPreferences)

    @Provides
    @Singleton
    fun providePasswordUserRepository(
        api: ArcaApi
    ): PasswordUserRepository = PasswordPasswordUserRepositoryImpl(api)
}
