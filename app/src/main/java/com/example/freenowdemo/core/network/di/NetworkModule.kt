package com.example.freenowdemo.core.network.di

import com.example.freenowdemo.core.network.VehicleApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * Hilt module that provides all network-layer dependencies for the application.
 * Dependency graph:
 * [Json] + [OkHttpClient] => [Retrofit] => [VehicleApiService]
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // Base URL pointing to the raw GitHub Gist that serves as the mock API.
    private const val BASE_URL = "https://gist.githubusercontent.com/guillemdiaz/f7c2d4a373d6f30222fbdbbfdc5502d2/raw/"

    /**
     * Provides the [Json] instance used by the Kotlin Serialization converter.
     */
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    /**
     * Provides the [OkHttpClient] used by Retrofit for all HTTP calls.
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            // Logs the full JSON body to Logcat
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    /**
     * Provides the [Retrofit] instance used to make API calls.
     * Wires together the [OkHttpClient] for transport and the Kotlinx Serialization
     * converter factory for automatic JSON => data class parsing
     * @param okHttpClient The HTTP client with logging interceptor attached.
     * @param json The configured [Json] instance for deserializing responses.
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    /**
     * Provides the [VehicleApiService] Retrofit implementation.
     * @param retrofit The configured [Retrofit] instance to create the service from.
     */
    @Provides
    @Singleton
    fun provideVehicleApiService(retrofit: Retrofit): VehicleApiService = retrofit.create(VehicleApiService::class.java)
}
