package com.dmarts05.speedshield.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dmarts05.speedshield.R

@Composable
fun Logo(modifier: Modifier = Modifier, logoSize: Dp, textStyle: TextStyle) {
    val logoResourceId =
        if (isSystemInDarkTheme()) R.drawable.speedshield_white_logo else R.drawable.speedshield_black_logo
    Image(
        painter = painterResource(id = logoResourceId),
        contentDescription = "Speedshield Logo",
        modifier = modifier.size(logoSize)
    )
    Text(
        text = "Speedshield",
        style = textStyle,
        modifier = modifier.padding(top = 8.dp)
    )
}
