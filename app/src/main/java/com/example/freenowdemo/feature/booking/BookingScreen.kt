package com.example.freenowdemo.feature.booking

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.freenowdemo.R
import com.example.freenowdemo.core.designsystem.component.FreenowLocationListItem
import com.example.freenowdemo.core.designsystem.component.FreenowSearchBar
import com.example.freenowdemo.core.designsystem.component.FreenowServiceCard
import com.example.freenowdemo.core.designsystem.component.FreenowVehicleOptionItem
import com.example.freenowdemo.core.designsystem.component.NoConnectionBanner
import com.example.freenowdemo.core.designsystem.icon.FreenowIcons
import com.example.freenowdemo.core.designsystem.theme.FreenowTheme
import com.example.freenowdemo.core.model.VehicleType
import com.example.freenowdemo.feature.booking.state.BookingStep
import com.example.freenowdemo.feature.booking.state.BookingViewIntent
import com.example.freenowdemo.feature.booking.state.BookingViewState
import com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom
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

private val DEFAULT_LOCATION = LatLng(41.3820, 2.1680)
private const val DEFAULT_ZOOM = 13f
private const val VEHICLE_ZOOM = 17f

/**
 * Main booking screen that combines a Google Map with a draggable bottom sheet.
 * The sheet peeks at a fixed height, allowing the user to drag it up to cover the map.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    modifier: Modifier = Modifier,
    isOffline: Boolean,
    state: BookingViewState,
    onIntent: (BookingViewIntent) -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val peekHeight = when (state.currentStep) {
        BookingStep.SEARCH -> 475.dp

        BookingStep.SELECT_VEHICLE,
        BookingStep.CONFIRM_RIDE -> 380.dp
    }

    Box(modifier = Modifier.fillMaxSize()) {
        BottomSheetScaffold(
            modifier = modifier.alpha(if (isOffline) 0.4f else 1f),
            sheetContainerColor = MaterialTheme.colorScheme.background,
            containerColor = if (state.isLoading) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.background
            },
            scaffoldState = scaffoldState,
            sheetPeekHeight = peekHeight,
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
                BookingSheetContent(onIntent = onIntent, state = state, scaffoldState = scaffoldState)
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
        AnimatedVisibility(
            visible =
            (state.currentStep == BookingStep.SELECT_VEHICLE || state.currentStep == BookingStep.CONFIRM_RIDE) &&
                !isOffline,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 }),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            val selectedOption = state.vehicleOptions.find { it.id == state.selectedVehicle }
            val isConfirmStep = state.currentStep == BookingStep.CONFIRM_RIDE

            Button(
                onClick = {
                    if (isConfirmStep) {
                        /* TODO: onIntent() */
                    } else {
                        onIntent(BookingViewIntent.ConfirmRideClicked)
                    }
                },
                enabled = selectedOption != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if (isConfirmStep) {
                        stringResource(R.string.order_now)
                    } else if (selectedOption !=
                        null
                    ) {
                        stringResource(R.string.confirm, selectedOption.title)
                    } else {
                        stringResource(R.string.select_a_ride)
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        // Full-screen overlay dialog
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
                    onRetryClick = { onIntent(BookingViewIntent.LoadVehicles) }
                )
            }
        }
    }
}

/**
 * Content of the bottom sheet that shows the search bar, saved locations,
 * and available service options.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookingSheetContent(
    onIntent: (BookingViewIntent) -> Unit,
    state: BookingViewState,
    scaffoldState: BottomSheetScaffoldState
) {
    Crossfade(
        targetState = state.currentStep,
        label = "Step Swap",
        modifier = Modifier.fillMaxSize()
    ) { step ->
        when (step) {
            BookingStep.SEARCH -> {
                SearchStepContent(onIntent = onIntent)
            }

            BookingStep.SELECT_VEHICLE -> {
                SelectVehicleStepContent(
                    state = state,
                    onIntent = onIntent,
                    scaffoldState = scaffoldState
                )
            }

            BookingStep.CONFIRM_RIDE -> {
                ConfirmRideStepContent(state = state, onIntent = onIntent)
            }
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
    val location = DEFAULT_LOCATION
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, DEFAULT_ZOOM)
    }

    // Listens for changes to the selected vehicle
    LaunchedEffect(state.selectedVehicle, state.currentStep) {
        if (state.currentStep == BookingStep.SEARCH) {
            val defaultCameraUpdate = newLatLngZoom(
                DEFAULT_LOCATION,
                DEFAULT_ZOOM
            )
            cameraPositionState.animate(defaultCameraUpdate)
        } else if (state.selectedVehicle != null) {
            val selected = state.vehicles.find { it.id == state.selectedVehicle }
            if (selected != null) {
                val targetLatLng = LatLng(selected.latitude, selected.longitude)
                val targetCameraUpdate = newLatLngZoom(targetLatLng, VEHICLE_ZOOM)
                cameraPositionState.animate(targetCameraUpdate)
            }
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = properties,
        uiSettings = uiSettings,
        contentPadding = PaddingValues(bottom = 350.dp)
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
                    contentDescription = stringResource(R.string.vehicle_on_map),
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}

@Composable
private fun SearchStepContent(modifier: Modifier = Modifier, onIntent: (BookingViewIntent) -> Unit) {
    Column(Modifier.fillMaxSize()) {
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
                title = stringResource(R.string.taxi),
                image = R.drawable.img_taxi,
                onItemClick = { onIntent(BookingViewIntent.ServiceCardClicked("Taxi")) }
            )
            FreenowServiceCard(
                modifier = Modifier.weight(1f),
                title = stringResource(R.string.rent_a_car),
                image = R.drawable.img_car,
                onItemClick = { onIntent(BookingViewIntent.ServiceCardClicked("Rent a car")) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectVehicleStepContent(
    state: BookingViewState,
    onIntent: (BookingViewIntent) -> Unit,
    scaffoldState: BottomSheetScaffoldState,
    modifier: Modifier = Modifier
) {
    val titleText = if (scaffoldState.bottomSheetState.targetValue == SheetValue.Expanded) {
        stringResource(R.string.select_a_way_to_travel)
    } else {
        stringResource(R.string.swipe_up_for_more)
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(FreenowIcons.BackArrow),
                contentDescription = stringResource(R.string.back_to_search),
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onIntent(BookingViewIntent.BackToSearchClicked) }
            )
            Text(
                text = titleText,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                // The weight pushes the text, the padding offsets the back arrow so it stays perfectly centered
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 24.dp)
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

        /*
        TODO: The LazyColumn currently fights with the BottomSheetScaffold for nested scroll events.
        A custom draggable sheet or nested scroll interceptor is needed in the future to allow
        independent list scrolling without expanding/collapsing the sheet.
         */
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Uses the UI model from the ViewModel
            items(state.vehicleOptions) { option ->
                FreenowVehicleOptionItem(
                    title = option.title,
                    subtitle = option.subtitle,
                    price = option.price,
                    iconRes = option.iconRes,
                    isSelected = state.selectedVehicle == option.id,
                    onClick = { onIntent(BookingViewIntent.SelectVehicle(option.id)) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun ConfirmRideStepContent(
    state: BookingViewState,
    onIntent: (BookingViewIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    // Find the vehicle the user selected
    val selectedOption = state.vehicleOptions.find { it.id == state.selectedVehicle }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(FreenowIcons.BackArrow),
                contentDescription = "Back to selection",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onIntent(BookingViewIntent.BackToVehicleSelectionClicked) }
            )
            Text(
                text = "Confirm your ride",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 24.dp)
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(top = 6.dp)) {
                Icon(
                    painter = painterResource(FreenowIcons.Pickup),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(12.dp)
                )
                Box(
                    modifier = Modifier
                        .width(
                            2.dp
                        )
                        .height(30.dp)
                        .background(MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f))
                )
                Icon(
                    painter = painterResource(FreenowIcons.Dropoff),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(12.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = state.pickupLocation?.takeIf { it.isNotBlank() } ?: "Current Location",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = state.dropoffLocation?.takeIf { it.isNotBlank() } ?: "Destination",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))

        // Selected Vehicle Summary
        if (selectedOption != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = selectedOption.iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Column {
                        Text(
                            text = selectedOption.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = selectedOption.subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = selectedOption.price,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingSearchPreview() {
    FreenowTheme {
        BookingScreen(
            isOffline = false,
            state = BookingViewState(),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BookingSelectVehiclePreview() {
    FreenowTheme {
        BookingScreen(
            isOffline = false,
            state = BookingViewState(
                currentStep = BookingStep.SELECT_VEHICLE,
                vehicleOptions = listOf(
                    // Fake UI models for the preview
                    com.example.freenowdemo.feature.booking.state.VehicleUiModel(
                        "1",
                        "Taxi Fixed Price",
                        "in 1 min • 4 seats",
                        "€16.60",
                        R.drawable.img_taxi
                    ),
                    com.example.freenowdemo.feature.booking.state.VehicleUiModel(
                        "2",
                        "Taxi XL",
                        "in 3 min • 6 seats",
                        "€22.50",
                        R.drawable.img_taxi
                    )
                ),
                selectedVehicle = "1"
            ),
            onIntent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BookingConfirmRidePreview() {
    FreenowTheme {
        BookingScreen(
            isOffline = false,
            state = BookingViewState(
                currentStep = BookingStep.CONFIRM_RIDE,
                pickupLocation = "Sagrada Familia",
                dropoffLocation = "Barcelona Airport (BCN)",
                vehicleOptions = listOf(
                    com.example.freenowdemo.feature.booking.state.VehicleUiModel(
                        "1",
                        "Taxi Fixed Price",
                        "in 1 min • 4 seats",
                        "€16.60",
                        R.drawable.img_taxi
                    )
                ),
                selectedVehicle = "1"
            ),
            onIntent = {}
        )
    }
}
