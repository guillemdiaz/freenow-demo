package com.example.freenowdemo.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.freenowdemo.R
import com.example.freenowdemo.core.designsystem.icon.FreenowIcons
import com.example.freenowdemo.core.designsystem.theme.FreenowTheme

/**
 * A simple card used primarily in the Booking Bottom Sheet to specify location.
 * @param icon The drawable resource ID for the location icon.
 */
@Composable
fun FreenowLocationListItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    @DrawableRes icon: Int,
    onItemClick: () -> Unit
) {
    Surface(
        onClick = onItemClick,
        modifier = modifier,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FreenowLocationListItemPreview() {
    FreenowTheme {
        FreenowLocationListItem(
            title = stringResource(R.string.address_home_title),
            subtitle = stringResource(R.string.address_home_subtitle),
            icon = FreenowIcons.Home,
            onItemClick = {}
        )
    }
}
