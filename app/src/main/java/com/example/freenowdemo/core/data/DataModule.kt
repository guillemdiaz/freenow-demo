package com.example.freenowdemo.core.data.di

import com.example.freenowdemo.core.data.repository.FakeVehicleRepository
import com.example.freenowdemo.core.data.repository.VehicleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Hilt dependency-injection module that wires together the data layer bindings
 * for the entire application.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    /**
     * Tells Hilt to satisfy every [VehicleRepository] injection point with a
     * [FakeVehicleRepository] instance.
     * @param fakeVehicleRepository The concrete instance Hilt constructs automatically.
     * @return The same instance, typed as the [VehicleRepository] interface.
     */
    @Binds
    abstract fun bindVehicleRepository(fakeVehicleRepository: FakeVehicleRepository): VehicleRepository
}
