package com.example.freenowdemo.feature.destination

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.freenowdemo.R
import com.example.freenowdemo.core.designsystem.component.FreenowDestinationCard
import com.example.freenowdemo.core.designsystem.component.FreenowLocationListItem
import com.example.freenowdemo.core.designsystem.component.NoConnectionBanner
import com.example.freenowdemo.core.designsystem.icon.FreenowIcons
import com.example.freenowdemo.core.designsystem.theme.FreenowTheme
import com.example.freenowdemo.feature.destination.state.DestinationViewIntent
import com.example.freenowdemo.feature.destination.state.DestinationViewState

/**
 * Screen allowing the user to set pickup and dropoff destinations, choose a location on
 * the map or select from saved locations.
 */
@Composable
fun DestinationScreen(
    modifier: Modifier = Modifier,
    isOffline: Boolean,
    onBackClick: () -> Unit,
    onAddStopClick: () -> Unit,
    state: DestinationViewState,
    onIntent: (DestinationViewIntent) -> Unit
) {
    // Local focus manager to handle keyboard dismissal
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                .alpha(if (isOffline) 0.4f else 1f)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FreenowDestinationCard(
                        pickupText = state.pickupText,
                        dropoffText = state.dropoffText,
                        isConfirmReady = state.isConfirmEnabled,
                        onPickupChange = { newText ->
                            onIntent(DestinationViewIntent.UpdatePickup(newText))
                        },
                        onDropoffChange = { newText ->
                            onIntent(DestinationViewIntent.UpdateDropoff(newText))
                        },
                        onKeyboardConfirm = {
                            focusManager.clearFocus()
                            onIntent(DestinationViewIntent.ConfirmClicked)
                        },
                        onBackClick = onBackClick,
                        onAddStopClick = onAddStopClick
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.clickable { /* TODO */ }
                    ) {
                        Icon(
                            painter = painterResource(FreenowIcons.Map),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = stringResource(R.string.choose_on_map),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        onClick = { /* TODO */ },
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.show_all),
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                    FreenowLocationListItem(
                        title = stringResource(R.string.current_location),
                        icon = FreenowIcons.NavigationArrow,
                        iconRotation = 45f,
                        onItemClick = { /* TODO */ }
                    )
                    FreenowLocationListItem(
                        title = stringResource(R.string.address_home_subtitle),
                        icon = FreenowIcons.Home,
                        onItemClick = { /* TODO */ }
                    )
                    FreenowLocationListItem(
                        title = stringResource(R.string.address_work_subtitle),
                        icon = FreenowIcons.Work,
                        onItemClick = { /* TODO */ }
                    )
                }
            }
        }
        // Full-screen overlay
        if (isOffline) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(100f)
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                awaitPointerEvent().changes.forEach { it.consume() }
                            }
                        }
                    },
                contentAlignment = Alignment.BottomCenter
            ) {
                NoConnectionBanner(
                    isVisible = true,
                    onRetryClick = { }
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "Light")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
fun DestinationScreenPreview() {
    FreenowTheme {
        DestinationScreen(
            isOffline = false,
            onBackClick = {},
            onAddStopClick = {},
            state = DestinationViewState(),
            onIntent = {}
        )
    }
}
