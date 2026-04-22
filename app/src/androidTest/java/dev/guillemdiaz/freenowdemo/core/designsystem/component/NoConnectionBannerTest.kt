package dev.guillemdiaz.freenowdemo.core.designsystem.component

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dev.guillemdiaz.freenowdemo.R
import dev.guillemdiaz.freenowdemo.core.designsystem.theme.FreenowTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class NoConnectionBannerTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun whenBannerIsVisible_textsAndButtonAreDisplayed() {
        composeTestRule.setContent {
            FreenowTheme {
                NoConnectionBanner(
                    isVisible = true,
                    onRetryClick = {}
                )
            }
        }

        val expectedTitle = composeTestRule.activity.getString(R.string.offline_msg_title)
        val expectedSubtitle = composeTestRule.activity.getString(R.string.offline_msg_subtitle)
        val expectedButtonText = composeTestRule.activity.getString(R.string.offline_msg_button)

        composeTestRule.onNodeWithText(expectedTitle).assertIsDisplayed()
        composeTestRule.onNodeWithText(expectedSubtitle).assertIsDisplayed()
        composeTestRule.onNodeWithText(expectedButtonText).assertIsDisplayed()
    }

    @Test
    fun whenRetryClicked_callbackIsTriggered() {
        var retryClicked = false

        composeTestRule.setContent {
            FreenowTheme {
                NoConnectionBanner(
                    isVisible = true,
                    onRetryClick = { retryClicked = true }
                )
            }
        }

        val expectedButtonText = composeTestRule.activity.getString(R.string.offline_msg_button)

        composeTestRule.onNodeWithText(expectedButtonText).performClick()

        assertTrue("The retry callback should be triggered when the button is clicked", retryClicked)
    }
}
