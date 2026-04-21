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
            emit(Result.Success(vehicles))
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }
}
