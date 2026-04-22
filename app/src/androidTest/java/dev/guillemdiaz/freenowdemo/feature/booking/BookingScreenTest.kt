package dev.guillemdiaz.freenowdemo.feature.booking

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.guillemdiaz.freenowdemo.R
import dev.guillemdiaz.freenowdemo.core.designsystem.theme.FreenowTheme
import dev.guillemdiaz.freenowdemo.feature.booking.state.BookingStep
import dev.guillemdiaz.freenowdemo.feature.booking.state.BookingViewIntent
import dev.guillemdiaz.freenowdemo.feature.booking.state.BookingViewState
import dev.guillemdiaz.freenowdemo.feature.booking.state.VehicleUiModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BookingScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun whenConfirmRideStep_orderNowButtonIsDisplayed() {
        val fakeState = BookingViewState(
            currentStep = BookingStep.CONFIRM_RIDE,
            selectedVehicle = "1",
            vehicleOptions = listOf(
                VehicleUiModel(
                    id = "1",
                    titleRes = R.string.taxi_fixed_price,
                    subtitle = "in 1 min · 4 seats",
                    price = 16.60,
                    iconRes = R.drawable.img_taxi
                )
            )
        )

        composeTestRule.setContent {
            FreenowTheme {
                BookingScreen(
                    isOffline = false,
                    state = fakeState,
                    onIntent = {}
                )
            }
        }

        val orderNowText = composeTestRule.activity.getString(R.string.order_now)
        composeTestRule.onNodeWithText(orderNowText).assertIsDisplayed()
    }

    @Test
    fun whenRideIsBooked_successDialogIsDisplayed() {
        val fakeState = BookingViewState(
            isRideBooked = true,
            selectedVehicle = "1",
            vehicleOptions = listOf(
                VehicleUiModel(
                    "1",
                    R.string.taxi_fixed_price,
                    "1 min",
                    16.60,
                    R.drawable.img_taxi
                )
            )
        )

        composeTestRule.setContent {
            FreenowTheme {
                BookingScreen(isOffline = false, state = fakeState, onIntent = {})
            }
        }

        composeTestRule.onNodeWithText("Ride Confirmed!").assertIsDisplayed()
        composeTestRule.onNodeWithText("Back to Search").assertIsDisplayed()
    }

    @Test
    fun whenSelectVehicleStepAndNoVehicleSelected_buttonIsDisabled() {
        val fakeState = BookingViewState(
            currentStep = BookingStep.SELECT_VEHICLE,
            selectedVehicle = null
        )

        composeTestRule.setContent {
            FreenowTheme {
                BookingScreen(isOffline = false, state = fakeState, onIntent = {})
            }
        }

        val selectRideText = composeTestRule.activity.getString(R.string.select_a_ride)

        composeTestRule.onNodeWithText(selectRideText)
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    @Test
    fun whenOrderNowClicked_firesOrderRideIntent() {
        var capturedIntent: BookingViewIntent? = null

        val fakeState = BookingViewState(
            currentStep = BookingStep.CONFIRM_RIDE,
            selectedVehicle = "1",
            vehicleOptions = listOf(
                VehicleUiModel(
                    "1",
                    R.string.taxi_fixed_price,
                    "1 min",
                    16.60,
                    R.drawable.img_taxi
                )
            )
        )

        composeTestRule.setContent {
            FreenowTheme {
                BookingScreen(
                    isOffline = false,
                    state = fakeState,
                    onIntent = { intent -> capturedIntent = intent }
                )
            }
        }

        val orderNowText = composeTestRule.activity.getString(R.string.order_now)

        composeTestRule.onNodeWithText(orderNowText).performClick()

        assertEquals(BookingViewIntent.OrderRideClicked, capturedIntent)
    }
}
