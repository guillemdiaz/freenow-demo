package dev.guillemdiaz.freenowdemo.core.data.repository

import dev.guillemdiaz.freenowdemo.core.model.Vehicle
import dev.guillemdiaz.freenowdemo.core.model.VehicleType
import dev.guillemdiaz.freenowdemo.core.network.VehicleApiService
import javax.inject.Inject

/**
 * Implementation of [VehicleRepository] that fetches vehicle data from the remote API via [VehicleApiService].
 * @param apiService The Retrofit-generated service used to perform HTTP calls.
 */
class NetworkVehicleRepository @Inject constructor(private val apiService: VehicleApiService) : VehicleRepository {

    /**
     * Fetches the list of available vehicles from the remote API and maps them to domain models.
     */
    override suspend fun getVehicles(): List<Vehicle> {
        val response = apiService.getVehicles()

        return response.poiList.map { networkVehicle ->
            Vehicle(
                id = networkVehicle.id,
                latitude = networkVehicle.coordinate.latitude,
                longitude = networkVehicle.coordinate.longitude,
                type = when (networkVehicle.fleetType) {
                    "TAXI" -> VehicleType.TAXI
                    "RENTAL_CAR" -> VehicleType.RENTAL_CAR
                    else -> VehicleType.TAXI // Fallback
                }
            )
        }
    }
}
