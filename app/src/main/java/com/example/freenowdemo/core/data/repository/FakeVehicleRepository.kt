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
        delay(1500)

        return listOf(
            Vehicle("taxi_1", VehicleType.TAXI, 42.2665, 2.9615),
            Vehicle("taxi_2", VehicleType.TAXI, 42.2670, 2.9600),
            Vehicle("car_1", VehicleType.RENTAL_CAR, 42.2650, 2.9620)
        )
    }
}
