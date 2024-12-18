package com.dmarts05.speedshield.presentation.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dmarts05.speedshield.R

@Composable
fun DonateScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        val logoResourceId = R.drawable.la_radio_bbs_logo
        Image(
            painter = painterResource(id = logoResourceId),
            contentDescription = "laradiobbs.net Logo",
            modifier = Modifier.width(256.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Main title
        Text(
            text = "Support LaRadioBBS!",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Subtitle
        Text(
            text = "Help us maintain and improve this service by making a donation. Every contribution counts!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(24.dp))
        // Donate button
        Button(
            onClick = {
                val donationUrl = "https://www.laradiobbs.net/donacion_radaresBBS.html"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(donationUrl))
                context.startActivity(intent)
            },
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .height(48.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Donate", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}
