package com.example.freenowdemo.feature.destination

import app.cash.turbine.test
import com.example.freenowdemo.feature.destination.state.DestinationViewEffect
import com.example.freenowdemo.feature.destination.state.DestinationViewIntent
import com.example.freenowdemo.feature.destination.state.DestinationViewState
import com.example.freenowdemo.rules.MainDispatcherRule
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DestinationViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: DestinationViewModel

    @Before
    fun setup() {
        viewModel = DestinationViewModel()
    }

    @Test
    fun `when initialized, state is default`() = runTest {
        assertEquals(DestinationViewState(), viewModel.state.value)
    }

    @Test
    fun `when UpdatePickup intent processed, updates pickup text in state`() = runTest {
        viewModel.state.test {
            awaitItem()

            val expectedText = "Sagrada Familia"
            viewModel.processIntent(DestinationViewIntent.UpdatePickup(expectedText))

            val updatedState = awaitItem()
            assertEquals(expectedText, updatedState.pickupText)
        }
    }

    @Test
    fun `when UpdateDropoff intent processed, updates dropoff text in state`() = runTest {
        viewModel.state.test {
            awaitItem()

            val expectedText = "Airport"
            viewModel.processIntent(DestinationViewIntent.UpdateDropoff(expectedText))

            val updatedState = awaitItem()
            assertEquals(expectedText, updatedState.dropoffText)
        }
    }

    @Test
    fun `when ConfirmClicked intent processed, emits NavigateBackWithResult effect`() = runTest {
        viewModel.effect.test {
            val pickup = "Home"
            val dropoff = "Work"
            viewModel.processIntent(DestinationViewIntent.UpdatePickup(pickup))
            viewModel.processIntent(DestinationViewIntent.UpdateDropoff(dropoff))

            viewModel.processIntent(DestinationViewIntent.ConfirmClicked)

            val emittedEffect = awaitItem()
            assertEquals(
                DestinationViewEffect.NavigateBackWithResult(pickup, dropoff),
                emittedEffect
            )
        }
    }
}
