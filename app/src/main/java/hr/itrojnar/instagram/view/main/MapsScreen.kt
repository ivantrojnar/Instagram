package hr.itrojnar.instagram.view.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import hr.itrojnar.instagram.model.Post
import hr.itrojnar.instagram.view.dialog.PostDetailDialog
import hr.itrojnar.instagram.viewmodel.MapsViewModel

@Composable
fun MapsScreen(mapsViewModel: MapsViewModel) {

    val posts = mapsViewModel.posts
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var selectedPost by remember { mutableStateOf<Post?>(null) }

    Column {
        MapViewContainer(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(bottom = 50.dp),
            context,
        ) { googleMap ->

            googleMap.uiSettings.isZoomControlsEnabled = true

            val currentLocation = LatLng(45.8150, 15.9819)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12f))

            posts.forEach { post ->
                val postLocation = LatLng(post.postLatitude, post.postLongitude)
                googleMap.addMarker(
                    MarkerOptions()
                        .position(postLocation)
                        .title(post.postDescription)
                )?.tag = post
            }

            googleMap.setOnMarkerClickListener { marker ->
                val post = marker.tag as? Post
                post?.let {
                    selectedPost = it
                    showDialog = true
                }
                true
            }

            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                googleMap.isMyLocationEnabled = true

                val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val userLocation = LatLng(it.latitude, it.longitude)
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 12f))
                    }
                }
            }
        }
        if (showDialog && selectedPost != null) {
            PostDetailDialog(post = selectedPost!!) {
                showDialog = false
            }
        }
    }
}

@Composable
fun MapViewContainer(
    modifier: Modifier = Modifier,
    context: Context,
    onMapReady: (GoogleMap) -> Unit
) {
    val mapOptions = GoogleMapOptions().mapType(GoogleMap.MAP_TYPE_NORMAL).mapId("bae209deaaa42fb4")

    AndroidView(
        modifier = modifier,
        factory = {
            MapView(context, mapOptions).apply {
                onCreate(Bundle())
                getMapAsync {
                    onMapReady(it)
                }
            }
        },
        update = { mapView ->
            mapView.onResume()
        }
    )
}