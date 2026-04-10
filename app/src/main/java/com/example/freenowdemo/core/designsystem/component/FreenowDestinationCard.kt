package com.example.freenowdemo.core.designsystem.component

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.freenowdemo.R
import com.example.freenowdemo.core.designsystem.icon.FreenowIcons
import com.example.freenowdemo.core.designsystem.theme.FreenowTheme

/**
 * A card with pickup and dropoff address fields, a back navigation button and an add stop button.
 */
@Composable
fun FreenowDestinationCard(
    modifier: Modifier = Modifier,
    pickupText: String,
    dropoffText: String,
    onPickupChange: (String) -> Unit,
    onDropoffChange: (String) -> Unit,
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
                    leadingIconTint = MaterialTheme.colorScheme.onSurface
                )
                HorizontalDivider()
                FreenowAddressTextField(
                    value = dropoffText,
                    onValueChange = onDropoffChange,
                    placeholderText = stringResource(R.string.dropoff),
                    leadingIcon = FreenowIcons.Dropoff,
                    leadingIconTint = MaterialTheme.colorScheme.primary
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
            onPickupChange = {},
            onDropoffChange = {},
            onBackClick = {},
            onAddStopClick = {}
        )
    }
}
