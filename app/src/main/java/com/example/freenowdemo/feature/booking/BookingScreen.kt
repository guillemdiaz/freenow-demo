package com.example.freenowdemo.feature.booking

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.freenowdemo.R
import com.example.freenowdemo.core.designsystem.component.FreenowLocationListItem
import com.example.freenowdemo.core.designsystem.component.FreenowSearchBar
import com.example.freenowdemo.core.designsystem.component.FreenowServiceCard
import com.example.freenowdemo.core.designsystem.component.NoConnectionBanner
import com.example.freenowdemo.core.designsystem.icon.FreenowIcons
import com.example.freenowdemo.core.designsystem.theme.FreenowTheme
import com.example.freenowdemo.core.model.VehicleType
import com.example.freenowdemo.feature.booking.state.BookingViewEffect
import com.example.freenowdemo.feature.booking.state.BookingViewIntent
import com.example.freenowdemo.feature.booking.state.BookingViewState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

/**
 * Main booking screen that combines a Google Map with a draggable bottom sheet.
 * The sheet peeks at a fixed height, allowing the user to drag it up to cover the map.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(modifier: Modifier = Modifier, viewModel: BookingViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Listen for one-off MVI Effects
    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is BookingViewEffect.ShowNoConnectionBanner -> {
                    println("EFFECT: Show offline banner!")
                }

                is BookingViewEffect.NavigateToDestinationSearch -> {
                    println("EFFECT: Navigate to destination search. Pre-selected: ${effect.preselectedService}")
                }

                is BookingViewEffect.NavigateToSetSavedLocation -> {
                    println("EFFECT: Navigate to set saved location: ${effect.locationType}")
                }
            }
        }
    }

    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        modifier = modifier.alpha(if (state.isOffline) 0.4f else 1f),
        sheetContainerColor = MaterialTheme.colorScheme.background,
        containerColor = if (state.isLoading) {
            MaterialTheme.colorScheme.surfaceVariant
        } else {
            MaterialTheme.colorScheme.background
        },
        scaffoldState = scaffoldState,
        sheetPeekHeight = 475.dp,
        sheetShadowElevation = 8.dp,
        sheetDragHandle = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                // Custom drag handle instead of the default Material one,
                // to have better control over its size and padding
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(36.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(50))
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f))
                )
            }
        },
        sheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        sheetContent = {
            BookingSheetContent(onIntent = { intent -> viewModel.processIntent(intent) })
        }
    ) { _ ->
        Box(modifier = Modifier.fillMaxSize()) {
            BookingMapContent(modifier = Modifier.fillMaxSize(), state = state)

            AnimatedVisibility(
                visible = state.isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 475.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    // Load the JSON file from res/raw
                    val composition by rememberLottieComposition(
                        LottieCompositionSpec.RawRes(R.raw.lottie_animation)
                    )

                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
        }
    }
    // Full-screen overlay dialog
    if (state.isOffline) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                NoConnectionBanner(
                    isVisible = true,
                    onRetryClick = {
                        viewModel.processIntent(BookingViewIntent.LoadVehicles)
                    }
                )
            }
        }
    }
}

/**
 * Content of the bottom sheet that shows the search bar, saved locations,
 * and available service options.
 */
@Composable
private fun BookingSheetContent(onIntent: (BookingViewIntent) -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxSize()
    ) {
        FreenowSearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            onItemClick = { onIntent(BookingViewIntent.SearchBarClicked) }
        )
        FreenowLocationListItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp),
            title = stringResource(R.string.address_home_title),
            subtitle = stringResource(R.string.address_home_subtitle),
            icon = FreenowIcons.Home,
            onItemClick = { onIntent(BookingViewIntent.SavedLocationClicked("Home")) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        FreenowLocationListItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp),
            title = stringResource(R.string.address_work_title),
            subtitle = stringResource(R.string.address_work_subtitle),
            icon = FreenowIcons.Work,
            onItemClick = { onIntent(BookingViewIntent.SavedLocationClicked("Work")) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FreenowServiceCard(
                modifier = Modifier.weight(1f),
                title = "Taxi",
                image = R.drawable.img_taxi,
                onItemClick = { onIntent(BookingViewIntent.ServiceCardClicked("Taxi")) }
            )
            FreenowServiceCard(
                modifier = Modifier.weight(1f),
                title = "Rent a car",
                image = R.drawable.img_car,
                onItemClick = { onIntent(BookingViewIntent.ServiceCardClicked("Rent a car")) }
            )
        }
    }
}

/**
 * Full-screen Google Map that adapts its style based on the current system theme.
 * Zoom controls are hidden to keep the UI clean even though the user can still pinch to zoom.
 */
@Composable
private fun BookingMapContent(modifier: Modifier = Modifier, state: BookingViewState) {
    val context = LocalContext.current
    val isDarkTheme = isSystemInDarkTheme()

    val mapStyleOptions = remember(isDarkTheme) {
        if (isDarkTheme) {
            // Custom JSON map style for dark mode to prevent the map
            // from flashing/re-rendering when the system theme changes
            MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_dark)
        } else {
            // Default Google Maps style
            null
        }
    }

    // Keyed on mapStyleOptions so properties recompose when the theme changes
    val properties by remember(mapStyleOptions) {
        mutableStateOf(MapProperties(mapType = MapType.NORMAL, mapStyleOptions = mapStyleOptions))
    }

    val uiSettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = false)) }

    // Default camera position centered on Barcelona
    val location = LatLng(41.3569, 2.1700)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 13f)
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings
    ) {
        state.vehicles.forEach { vehicle ->
            val iconRes = when (vehicle.type) {
                VehicleType.TAXI -> R.drawable.img_taxi
                VehicleType.RENTAL_CAR -> R.drawable.img_car
            }

            // Draw the Compose UI exactly at the vehicle's Lat/Lng coordinates
            MarkerComposable(
                state = MarkerState(position = LatLng(vehicle.latitude, vehicle.longitude)),
                title = vehicle.id
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = "Vehicle on map",
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingSheetContentPreview() {
    FreenowTheme {
        // BookingSheetContent()
    }
}
