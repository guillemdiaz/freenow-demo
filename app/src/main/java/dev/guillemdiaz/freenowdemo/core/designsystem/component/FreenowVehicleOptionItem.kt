package dev.guillemdiaz.freenowdemo.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.guillemdiaz.freenowdemo.R
import dev.guillemdiaz.freenowdemo.core.designsystem.theme.FreenowTheme

/**
 * A selectable list item representing a single vehicle option in the booking bottom sheet.
 * @param title The localized name of the vehicle category (e.g., "Taxi Fixed Price").
 * @param subtitle Secondary information such as ETA and seat capacity.
 * @param price The formatted price string including currency symbols.
 * @param iconRes The drawable resource ID for the vehicle image.
 * @param isSelected Whether this item is currently selected by the user, triggering a border highlight.
 * @param onClick Callback triggered when the entire row is tapped.
 */
@Composable
fun FreenowVehicleOptionItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    price: String,
    iconRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) {
        MaterialTheme.colorScheme.primaryContainer.copy(
            alpha = 0.2f
        )
    } else {
        Color.Transparent
    }
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .drawBehind {
                if (isSelected) {
                    drawRect(
                        color = borderColor,
                        size = Size(6.dp.toPx(), size.height)
                    )
                }
            },
        color = backgroundColor,
        shape = RoundedCornerShape(
            topStart = 0.dp,
            bottomStart = 0.dp,
            topEnd = 12.dp,
            bottomEnd = 12.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = price,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true, name = "Light")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
fun FreenowVehicleOptionItemPreview() {
    FreenowTheme {
        FreenowVehicleOptionItem(
            title = "Taxi",
            subtitle = "in 1 min • 4 seats",
            price = "€10.00",
            iconRes = R.drawable.img_taxi,
            isSelected = true,
            onClick = {}
        )
    }
}
