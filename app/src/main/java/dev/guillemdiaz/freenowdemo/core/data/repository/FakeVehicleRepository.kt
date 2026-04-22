package dev.guillemdiaz.freenowdemo.core.data.repository

import dev.guillemdiaz.freenowdemo.core.model.Result
import dev.guillemdiaz.freenowdemo.core.model.Vehicle
import dev.guillemdiaz.freenowdemo.core.model.VehicleType
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * In-memory fake implementation of [VehicleRepository] used during development and testing.
 * Replaces real network calls with hardcoded data so the UI can be built and validated
 * without a live backend.
 * Annotated with [@Inject constructor][Inject] so Hilt can instantiate it automatically
 * when it is bound as the [VehicleRepository] implementation in [DataModule].
 */
class FakeVehicleRepository @Inject constructor() : VehicleRepository {

    /**
     * Returns a hardcoded list of vehicles after a simulated network delay.
     */
    override suspend fun getVehicles(): Flow<Result<List<Vehicle>>> = flow {
        emit(Result.Loading)
        delay(7500)

        emit(
            Result.Success(
                listOf(
                    Vehicle("taxi_1", VehicleType.TAXI, 41.39888, 2.17040, 1, 4, 16.60),
                    Vehicle("taxi_2", VehicleType.TAXI, 41.38935, 2.15378, 3, 6, 21.20), // XL Taxi
                    Vehicle("taxi_3", VehicleType.TAXI, 41.38050, 2.17330, 1, 4, 16.60)
                )
            )
        )
    }
}
