package com.example.freenowdemo.core.network

import com.example.freenowdemo.core.model.VehicleResponse
import retrofit2.http.GET

/**
 * Retrofit service interface defining the HTTP endpoints for the vehicle API.
 */
interface VehicleApiService {

    /**
     * Fetches the full list of available vehicles from the remote source.
     */
    @GET("vehicles.json")
    suspend fun getVehicles(): VehicleResponse
}
