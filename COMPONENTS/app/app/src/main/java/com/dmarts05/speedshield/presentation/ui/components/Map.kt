package com.dmarts05.speedshield.presentation.ui.components

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.annotation.ViewAnnotation
import com.mapbox.maps.extension.compose.style.MapStyle
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions

@Composable
fun Map(modifier: Modifier = Modifier) {
    val mapViewportState = rememberMapViewportState()

    val context = LocalContext.current
    var hasLocationPermission by remember { mutableStateOf(false) }

    // Permission launcher
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
        if (!isGranted) {
            Toast.makeText(
                context,
                "Location permission is required to display the map.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Check permission and request if needed
    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Render Map or Permission Message
    if (hasLocationPermission) {
        MapboxMap(
            modifier,
            style = {
                MapStyle(
                    style = if (isSystemInDarkTheme()) Style.TRAFFIC_NIGHT else Style.TRAFFIC_DAY
                )
            },
            mapViewportState = mapViewportState,
            scaleBar = {}
        ) {
            SpeedcamViewAnnotationExample()
            MapEffect(Unit) { mapView ->
                mapView.location.updateSettings {
                    locationPuck = createDefault2DPuck()
                    enabled = true
                    puckBearing = PuckBearing.COURSE
                    puckBearingEnabled = true
                }
                mapViewportState.transitionToFollowPuckState()
            }
        }
    } else {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text("Please grant location permission to use the map.")
        }
    }
}


@Composable
fun SpeedcamViewAnnotationExample() {
    val speedCamData = listOf(
        SpeedCam(
            Point.fromLngLat(-5.5749, 42.5985),
            speedLimit = 50,
            streetName = "Paseo de Salamanca"
        ),
        SpeedCam(
            Point.fromLngLat(-5.5780, 42.6003),
            speedLimit = 80,
            streetName = "Avenida de los Cubos"
        ),
        SpeedCam(
            Point.fromLngLat(-5.5715, 42.5998),
            speedLimit = 40,
            streetName = "Avenida del Padre Isla"
        ),
        SpeedCam(
            Point.fromLngLat(-5.5680, 42.6020),
            speedLimit = 30,
            streetName = "Calle de las Cercas"
        ),
        SpeedCam(Point.fromLngLat(-5.5762, 42.6015), speedLimit = 60, streetName = "Calle Ancha"),
        SpeedCam(
            Point.fromLngLat(-5.5731, 42.5968),
            speedLimit = 70,
            streetName = "Calle Mariano Domínguez Berrueta"
        ),
        SpeedCam(
            Point.fromLngLat(-5.5705, 42.5970),
            speedLimit = 90,
            streetName = "Plaza de Santo Domingo"
        ),
        SpeedCam(
            Point.fromLngLat(-5.5754, 42.6005),
            speedLimit = 50,
            streetName = "Avenida de la Facultad"
        ),
        SpeedCam(
            Point.fromLngLat(-5.5718, 42.5987),
            speedLimit = 30,
            streetName = "Calle Ramón y Cajal"
        ),
        SpeedCam(
            Point.fromLngLat(-5.5675, 42.6018),
            speedLimit = 80,
            streetName = "Plaza del Espolón"
        )
    )

    for (speedCam in speedCamData) {
        ViewAnnotation(
            options = viewAnnotationOptions {
                geometry(speedCam.location)
                allowOverlap(true)
            }
        ) {
            SpeedcamViewAnnotationContent(speedCam)
        }
    }
}

@Composable
fun SpeedcamViewAnnotationContent(speedCam: SpeedCam) {
    var isClicked by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .wrapContentSize()
            .clickable { isClicked = !isClicked }
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isClicked) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = speedCam.streetName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Text(
                text = "${speedCam.speedLimit}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
        }
    }
}

data class SpeedCam(val location: Point, val speedLimit: Int, val streetName: String)
