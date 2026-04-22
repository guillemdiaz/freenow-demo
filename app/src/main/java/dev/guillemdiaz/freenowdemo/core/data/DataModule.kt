package dev.guillemdiaz.freenowdemo.core.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.guillemdiaz.freenowdemo.core.data.repository.NetworkVehicleRepository
import dev.guillemdiaz.freenowdemo.core.data.repository.VehicleRepository

/**
 * Hilt dependency-injection module that wires together the data layer bindings for the entire application.
 */
@Module
@InstallIn(SingletonComponent::class)
@Suppress("unused")
abstract class DataModule {

    /**
     * Tells Hilt to satisfy every [VehicleRepository] injection point with a
     * [NetworkVehicleRepository] instance.
     * @param networkVehicleRepository The concrete instance Hilt constructs automatically.
     * @return The same instance, typed as the [VehicleRepository] interface.
     */
    @Binds
    abstract fun bindVehicleRepository(networkVehicleRepository: NetworkVehicleRepository): VehicleRepository
}
