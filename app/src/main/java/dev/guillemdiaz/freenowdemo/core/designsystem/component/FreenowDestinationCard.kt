package dev.guillemdiaz.freenowdemo.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.guillemdiaz.freenowdemo.R
import dev.guillemdiaz.freenowdemo.core.designsystem.icon.FreenowIcons
import dev.guillemdiaz.freenowdemo.core.designsystem.theme.FreenowTheme

/**
 * A rectangular card containing two [FreenowAddressTextField]s to select pickup and dropoff locations.
 * @param pickupText The current text in the top pickup field.
 * @param dropoffText The current text in the bottom dropoff field.
 * @param isConfirmReady True if both fields are populated, allowing the keyboard "Done" action to trigger confirmation.
 * @param onPickupChange Callback when the pickup text changes.
 * @param onDropoffChange Callback when the dropoff text changes.
 * @param onBackClick Callback when the leading back arrow is clicked.
 * @param onAddStopClick Callback when the trailing add icon is clicked.
 * @param onKeyboardConfirm Callback triggered when the user presses "Done" on the software keyboard while [isConfirmReady] is true.
 */
@Composable
fun FreenowDestinationCard(
    modifier: Modifier = Modifier,
    pickupText: String,
    dropoffText: String,
    isConfirmReady: Boolean,
    onPickupChange: (String) -> Unit,
    onDropoffChange: (String) -> Unit,
    onKeyboardConfirm: () -> Unit,
    onBackClick: () -> Unit,
    onAddStopClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
        color = Color.Transparent
    ) {
        Row(
            modifier = modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(FreenowIcons.BackArrow),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onBackClick() }
                    .align(Alignment.Top)
                    .offset(y = 6.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                FreenowAddressTextField(
                    value = pickupText,
                    onValueChange = onPickupChange,
                    placeholderText = stringResource(R.string.pickup),
                    leadingIcon = FreenowIcons.Pickup,
                    leadingIconTint = MaterialTheme.colorScheme.onSurface,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                HorizontalDivider()
                FreenowAddressTextField(
                    value = dropoffText,
                    onValueChange = onDropoffChange,
                    placeholderText = stringResource(R.string.dropoff),
                    leadingIcon = FreenowIcons.Dropoff,
                    leadingIconTint = MaterialTheme.colorScheme.primary,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            if (isConfirmReady) {
                                onKeyboardConfirm()
                            }
                        }
                    )
                )
            }
            Icon(
                painter = painterResource(FreenowIcons.Add),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp).clickable { onAddStopClick() }
            )
        }
    }
}

@Preview(showBackground = true, name = "Light")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
fun FreenowDestinationCardPreview() {
    FreenowTheme {
        FreenowDestinationCard(
            pickupText = "",
            dropoffText = "",
            isConfirmReady = false,
            onPickupChange = {},
            onDropoffChange = {},
            onBackClick = {},
            onAddStopClick = {},
            onKeyboardConfirm = {}
        )
    }
}
