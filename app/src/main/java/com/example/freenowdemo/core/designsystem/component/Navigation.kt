package com.example.freenowdemo.core.designsystem.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * App-level bottom navigation bar with a subtle top shadow to visually separate
 * it from the screen content.
 * @param content The navigation bar items to display.
 */
@Composable
fun FreenowNavigationBar(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    val shadowColor = Color.Black.copy(alpha = 0.10f)

    Box(modifier = modifier) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
            // Disabled to prevent Material3 from tinting the background with primary color
            tonalElevation = 0.dp,
            content = content
        )
        // Gradient drawn on top of the bar to fake a top shadow,
        // since NavigationBar doesn't support elevation shadows on its top edge
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .align(Alignment.TopStart)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, shadowColor)
                    )
                )
        )
    }
}

/**
 * A single item in [FreenowNavigationBar] with a spring scale animation on press
 * and support for separate selected/unselected icons.
 */
@Composable
fun RowScope.FreenowNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    selectedIcon: @Composable () -> Unit = icon,
    label: @Composable (() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.65f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "navItemScale"
    )

    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        interactionSource = interactionSource,
        icon = {
            Column(
                // Scale applied here rather than on the whole item to avoid
                // shrinking the touch target along with the visual content
                modifier = Modifier.scale(scale),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                if (selected) selectedIcon() else icon()
                ProvideTextStyle(MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp)) {
                    label?.invoke()
                }
            }
        },
        // label is null because it's rendered manually inside the icon slot above
        label = null,
        alwaysShowLabel = false,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            // Removed the default pill indicator to keep the bar visually minimal
            indicatorColor = Color.Transparent
        )
    )
}
