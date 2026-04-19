package dev.guillemdiaz.freenowdemo.feature.booking

import app.cash.turbine.test
import dev.guillemdiaz.freenowdemo.core.analytics.TestAnalyticsTracker
import dev.guillemdiaz.freenowdemo.core.data.repository.FakeVehicleRepository
import dev.guillemdiaz.freenowdemo.feature.booking.state.BookingStep
import dev.guillemdiaz.freenowdemo.feature.booking.state.BookingViewIntent
import dev.guillemdiaz.freenowdemo.rules.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class BookingViewModelTest {

    // Swaps the Main dispatcher for the Test dispatcher
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: BookingViewModel
    private lateinit var repository: FakeVehicleRepository
    private lateinit var analyticsTracker: TestAnalyticsTracker

    @Before
    fun setup() {
        repository = FakeVehicleRepository()
        analyticsTracker = TestAnalyticsTracker()

        viewModel = BookingViewModel(
            repository = repository,
            analyticsTracker = analyticsTracker
        )
    }

    @Test
    fun `when initialized, automatically loads vehicles`() = runTest {
        viewModel.state.test {
            assertTrue(awaitItem().isLoading)

            val successState = awaitItem()
            assertFalse(successState.isLoading)
            assertTrue(successState.vehicles.isNotEmpty())
            assertTrue(successState.vehicleOptions.isNotEmpty())
        }
    }

    @Test
    fun `when manual refresh intent processed, state updates to loading then success`() = runTest {
        viewModel.state.test {
            assertTrue(awaitItem().isLoading)
            assertFalse(awaitItem().isLoading)

            viewModel.processIntent(BookingViewIntent.LoadVehicles)

            assertTrue(awaitItem().isLoading)

            val refreshedState = awaitItem()
            assertFalse(refreshedState.isLoading)
            assertTrue(refreshedState.vehicles.isNotEmpty())
        }
    }

    @Test
    fun `when SelectVehicle intent processed, updates selected vehicle id`() = runTest {
        viewModel.state.test {
            awaitItem()
            val loadedState = awaitItem()

            val vehicleToSelect = loadedState.vehicles.first().id

            viewModel.processIntent(BookingViewIntent.SelectVehicle(vehicleToSelect))

            val selectedState = awaitItem()
            assertEquals(vehicleToSelect, selectedState.selectedVehicle)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when OrderRideClicked, logs analytics, loads, and shows success dialog`() = runTest {
        viewModel.state.test {
            awaitItem()
            val loadedState = awaitItem()

            viewModel.processIntent(BookingViewIntent.SelectVehicle(loadedState.vehicles.first().id))
            awaitItem()

            viewModel.processIntent(BookingViewIntent.OrderRideClicked)

            assertEquals("Ride_Booked", analyticsTracker.lastEventName)

            assertTrue(awaitItem().isLoading)

            advanceTimeBy(2500)

            val finalState = awaitItem()
            assertFalse(finalState.isLoading)
            assertTrue(finalState.isRideBooked)
        }
    }

    @Test
    fun `when DismissSuccessDialog processed, completely resets booking state`() = runTest {
        viewModel.state.test {
            awaitItem()
            awaitItem()

            viewModel.processIntent(BookingViewIntent.DestinationConfirmed("Home", "Work"))
            awaitItem()

            viewModel.processIntent(BookingViewIntent.DismissSuccessDialog)

            val resetState = awaitItem()
            assertFalse(resetState.isRideBooked)
            assertEquals(BookingStep.SEARCH, resetState.currentStep)
            assertEquals(null, resetState.selectedVehicle)
            assertEquals(null, resetState.pickupLocation)
            assertEquals(null, resetState.dropoffLocation)
        }
    }
}
