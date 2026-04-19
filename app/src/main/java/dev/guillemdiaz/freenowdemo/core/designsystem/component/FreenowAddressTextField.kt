package dev.guillemdiaz.freenowdemo.core.designsystem.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.guillemdiaz.freenowdemo.R
import dev.guillemdiaz.freenowdemo.core.designsystem.icon.FreenowIcons
import dev.guillemdiaz.freenowdemo.core.designsystem.theme.FreenowTheme

/**
 * A single-line text field with a leading icon, placeholder and themed cursor for address input.
 */
@Composable
fun FreenowAddressTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    leadingIcon: Int,
    leadingIconTint: Color,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        modifier = modifier.fillMaxWidth().padding(vertical = 4.dp),
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurface
        ),
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        decorationBox = { innerTextField ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = leadingIcon),
                    contentDescription = null,
                    tint = leadingIconTint,
                    modifier = Modifier.size(12.dp)
                )
                Box {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholderText,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

@Preview(showBackground = true, name = "Light")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark")
@Composable
fun FreenowAddressTextFieldPreview() {
    FreenowTheme {
        FreenowAddressTextField(
            value = "",
            onValueChange = {},
            placeholderText = stringResource(R.string.pickup),
            leadingIcon = FreenowIcons.Dropoff,
            leadingIconTint = MaterialTheme.colorScheme.primary
        )
    }
}
