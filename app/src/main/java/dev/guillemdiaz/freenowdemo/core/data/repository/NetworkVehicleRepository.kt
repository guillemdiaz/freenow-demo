package dev.guillemdiaz.freenowdemo.core.data.repository

import dev.guillemdiaz.freenowdemo.core.model.Result
import dev.guillemdiaz.freenowdemo.core.model.Vehicle
import dev.guillemdiaz.freenowdemo.core.model.VehicleType
import dev.guillemdiaz.freenowdemo.core.network.VehicleApiService
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Implementation of [VehicleRepository] that fetches vehicle data from the remote API via [VehicleApiService].
 * @param apiService The Retrofit-generated service used to perform HTTP calls.
 */
class NetworkVehicleRepository @Inject constructor(private val apiService: VehicleApiService) : VehicleRepository {

    /**
     * Fetches the list of available vehicles from the remote API and maps them to domain models.
     */
    override suspend fun getVehicles(): Flow<Result<List<Vehicle>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getVehicles()
            val vehicles = response.poiList.map { networkVehicle ->
                // Generates fake data based on the ID or type
                val isXl = networkVehicle.fleetType == "TAXI_XL" || networkVehicle.id.endsWith("2")

                Vehicle(
                    id = networkVehicle.id,
                    latitude = networkVehicle.coordinate.latitude,
                    longitude = networkVehicle.coordinate.longitude,
                    type = when (networkVehicle.fleetType) {
                        "TAXI" -> VehicleType.TAXI
                        "RENTAL_CAR" -> VehicleType.RENTAL_CAR
                        else -> VehicleType.TAXI
                    },
                    etaMinutes = if (isXl) 3 else 1,
                    maxSeats = if (isXl) 6 else 4,
                    price = if (isXl) 21.20 else 16.60
                )
            }
            emit(Result.Success(vehicles))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
