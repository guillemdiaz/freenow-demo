package com.example.freenowdemo.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.freenowdemo.R
import com.example.freenowdemo.core.designsystem.theme.FreenowTheme

/**
 * A rectangular service selection card used primarily in the Booking Bottom Sheet to select a specific service.
 * @param image The drawable resource ID for the service icon. Should be cropped tightly as the image uses fixed sizing.
*/
@Composable
fun FreenowServiceCard(modifier: Modifier = Modifier, title: String, @DrawableRes image: Int, onItemClick: () -> Unit) {
    Surface(
        onClick = onItemClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = title,
                modifier = Modifier.size(54.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FreenowServiceCardPreview() {
    FreenowTheme {
        FreenowServiceCard(
            title = "Taxi",
            image = R.drawable.img_taxi,
            onItemClick = {}
        )
    }
}
