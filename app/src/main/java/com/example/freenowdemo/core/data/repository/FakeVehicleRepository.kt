package com.example.freenowdemo.core.data.repository

import com.example.freenowdemo.core.model.Vehicle
import com.example.freenowdemo.core.model.VehicleType
import javax.inject.Inject
import kotlinx.coroutines.delay

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
    override suspend fun getVehicles(): List<Vehicle> {
        delay(7500)

        // throw Exception("Simulated network failure")

        return listOf(
            Vehicle("taxi_1", VehicleType.TAXI, 41.39888, 2.17040),
            Vehicle("taxi_2", VehicleType.TAXI, 41.38935, 2.15378),
            Vehicle("taxi_3", VehicleType.TAXI, 41.38050, 2.16689),
            Vehicle("taxi_4", VehicleType.TAXI, 41.38915, 2.14933),
            Vehicle("taxi_5", VehicleType.TAXI, 41.39470, 2.20089)

        )
    }
}
