package com.example.freenowdemo.core.model

/**
 * Core domain model representing a vehicle available for booking.
 */
data class Vehicle(val id: String, val type: VehicleType, val latitude: Double, val longitude: Double)

/**
 * Enumerates the supported vehicle categories.
 */
enum class VehicleType {
    TAXI,
    RENTAL_CAR
}
