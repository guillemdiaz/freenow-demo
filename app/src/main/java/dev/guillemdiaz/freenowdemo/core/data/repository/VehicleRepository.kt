package dev.guillemdiaz.freenowdemo.core.data.repository

import dev.guillemdiaz.freenowdemo.core.model.Result
import dev.guillemdiaz.freenowdemo.core.model.Vehicle
import kotlinx.coroutines.flow.Flow

/**
 * Contract for accessing vehicle data, abstracting the underlying data source
 * (network, cache, fake, etc.) from the rest of the application.
 */
interface VehicleRepository {
    /**
     * Fetches a list of available vehicles in the user's area.
     * Throws an exception if the network fails.
     */
    suspend fun getVehicles(): Flow<Result<List<Vehicle>>>
}
