package com.app.arcabyolimpo.di

import android.content.Context
import com.app.arcabyolimpo.data.local.auth.UserPreferences
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.interceptor.AuthInterceptor
import com.app.arcabyolimpo.data.remote.interceptor.SessionManager
import com.app.arcabyolimpo.data.remote.interceptor.TokenAuthenticator
import com.app.arcabyolimpo.data.repository.auth.UserRepositoryImpl
import com.app.arcabyolimpo.data.repository.beneficiaries.BeneficiaryRepositoryImpl
import com.app.arcabyolimpo.data.repository.disabilities.DisabilityRepositoryImpl
import com.app.arcabyolimpo.data.repository.password.PasswordUserRepositoryImpl
import com.app.arcabyolimpo.data.repository.product.ProductRepositoryImpl
import com.app.arcabyolimpo.data.repository.productbatches.ProductBatchRepositoryImpl
import com.app.arcabyolimpo.data.repository.qr.QrRepositoryImpl
import com.app.arcabyolimpo.data.repository.supplies.SupplyRepositoryImpl
import com.app.arcabyolimpo.data.repository.user.UsersRepositoryImpl
import com.app.arcabyolimpo.data.repository.workshops.WorkshopRepositoryImpl
import com.app.arcabyolimpo.domain.repository.auth.UserRepository
import com.app.arcabyolimpo.domain.repository.beneficiaries.BeneficiaryRepository
import com.app.arcabyolimpo.domain.repository.disability.DisabilityRepository
import com.app.arcabyolimpo.domain.repository.password.PasswordUserRepository
import com.app.arcabyolimpo.domain.repository.product.ProductRepository
import com.app.arcabyolimpo.domain.repository.productbatches.ProductBatchRepository
import com.app.arcabyolimpo.domain.repository.qr.QrRepository
import com.app.arcabyolimpo.domain.repository.supplies.SupplyRepository
import com.app.arcabyolimpo.domain.repository.user.UsersRepository
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository
import com.app.arcabyolimpo.domain.usecase.productbatches.GetProductBatchesUseCase
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
    private const val BASE_URL = "http://10.0.2.2:8081/"

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
    fun providePasswordUserRepository(api: ArcaApi): PasswordUserRepository = PasswordUserRepositoryImpl(api)

    /**
     * Provides the [SupplyRepository] implementation.
     *
     * This repository handles all supply-related data operations,
     * including fetching the supply list and retrieving detailed
     * information for a specific supply. It uses [ArcaApi] as the
     * remote data source and maps API responses to domain models.
     *
     * @param api The [ArcaApi] instance used to perform network requests.
     * @return A singleton instance of [SupplyRepositoryImpl].
     */
    @Provides
    @Singleton
    fun provideSupplyRepository(
        api: ArcaApi,
        @ApplicationContext context: Context,
    ): SupplyRepository = SupplyRepositoryImpl(api, context)

    /**
     * Provides the [ProductRepository] implementation.
     *
     * @param api The [ArcaApi] instance used to perform network requests.
     * @return A singleton instance of [ProductRepositoryImpl].
     */
    @Provides
    @Singleton
    fun provideProductRepository(
        api: ArcaApi,
        @ApplicationContext context: Context,
    ): ProductRepository = ProductRepositoryImpl(api, context)

    /**
     * Provides the [WorkshopRepository] implementation.
     *
     * This repository handles all workshop-related data operations,
     * including fetching the workshop list and retrieving detailed
     * information for a specific workshop. It uses [ArcaApi] as the
     * remote data source and maps API responses to domain models.
     *
     * @param api The [ArcaApi] instance used to perform network requests.
     * @return A singleton instance of [WorkshopRepositoryImpl].
     */
    @Provides
    @Singleton
    fun provideWorkshopRepository(api: ArcaApi): WorkshopRepository = WorkshopRepositoryImpl(api)

    /**
     * Provides the [BeneficiaryRepository] implementation.
     *
     * This repository handles all beneficiary-related data operations,
     * including fetching the beneficiary list and retrieving detailed
     * information for a specific beneficiary. It uses [ArcaApi] as the
     * remote data source and maps API responses to domain models.
     *
     * @param api The [ArcaApi] instance used to perform network requests.
     * @return A singleton instance of [BeneficiaryRepositoryImpl].
     */
    @Provides
    @Singleton
    fun provideBeneficiaryRepository(api: ArcaApi): BeneficiaryRepository = BeneficiaryRepositoryImpl(api)

    @Provides
    @Singleton
    fun provDisabilityRepository(api: ArcaApi): DisabilityRepository = DisabilityRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideUsersRepository(api: ArcaApi): UsersRepository = UsersRepositoryImpl(api)

    /** Provides the [ProductBatchRepository] implementation.*/
    @Provides
    @Singleton
    fun provideProductBatchRepository(api: ArcaApi): ProductBatchRepository = ProductBatchRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideQrRepository(api: ArcaApi): QrRepository = QrRepositoryImpl(api)
}
