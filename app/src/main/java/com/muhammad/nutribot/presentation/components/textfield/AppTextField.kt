package com.muhammad.nutribot.presentation.components.textfield

import androidx.annotation.StringRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.muhammad.nutribot.utils.rippleClickable

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    onClick: () -> Unit = {},
    onTrailingClick: () -> Unit = {},
    leadingIcon: Int? = null, contentPadding: PaddingValues = PaddingValues(16.dp),
    trailingIcon: Int? = null,
    textStyle : TextStyle = MaterialTheme.typography.headlineLarge.copy(
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center
    ),
    @StringRes hint: Int? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    var isFocused by remember { mutableStateOf(false) }
    val borderColor by animateColorAsState(
        targetValue = if (isFocused) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent,
        animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
        label = "borderColor"
    )
    BasicTextField(
        state = state,
        enabled = enabled,
        textStyle = textStyle,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 2),
        readOnly = readOnly,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.surface),
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(contentPadding)
            .onFocusChanged {
                isFocused = it.isFocused
            }, decorator = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (leadingIcon != null) {
                    Icon(
                        imageVector = ImageVector.vectorResource(leadingIcon),
                        contentDescription = null, modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    if (state.text.isEmpty() && hint != null) {
                        Text(
                            text = stringResource(hint),
                            style = textStyle.copy(color = MaterialTheme.colorScheme.surface),
                        )
                    }
                    innerTextField()
                }
                if (trailingIcon != null) {
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = ImageVector.vectorResource(trailingIcon),
                        contentDescription = null,
                        modifier = Modifier.rippleClickable(onClick = onTrailingClick),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    )
}