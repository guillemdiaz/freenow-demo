package com.example.freenowdemo.core.model

import kotlinx.serialization.Serializable

/**
 * Root response object returned by the vehicle API.
 * Maps directly to the top-level JSON structure, which wraps all vehicles in a "poiList" array.
 * JSON structure:
 * {
 *   "poiList": [ { ... }, { ... } ]
 * }
 * @param poiList The list of vehicles returned by the API. "POI" stands for Point of Interest,
 *                   which is how the API refers to each available vehicle on the map.
 */
@Serializable
data class VehicleResponse(val poiList: List<NetworkVehicle>)

/**
 * Represents a single vehicle as returned by the network layer.
 */
@Serializable
data class NetworkVehicle(
    val id: String,
    val coordinate: NetworkCoordinate,
    val fleetType: String
    // val heading: Double
)

/**
 * Represents a GPS coordinate pair nested inside a [NetworkVehicle].
 */
@Serializable
data class NetworkCoordinate(val latitude: Double, val longitude: Double)
