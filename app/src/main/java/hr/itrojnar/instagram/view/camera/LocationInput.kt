package hr.itrojnar.instagram.view.camera

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.viewmodel.CameraViewModel

@Composable
fun LocationInput(cameraViewModel: CameraViewModel) {

    val darkTheme = isSystemInDarkTheme()
    val textColor = if (darkTheme) Color.White else Color.Black

    val context = LocalContext.current
    val noLocationSelectedString = stringResource(R.string.no_location_selected)
    var locationText by remember { mutableStateOf(noLocationSelectedString) }

    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(result.data!!)
            locationText = place.address ?: "Unknown location"
            cameraViewModel.setLocation(place)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = stringResource(R.string.selected_location),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = locationText,
                style = TextStyle(fontSize = 16.sp),
                color = textColor
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val intent = Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN,
                    listOf(
                        Place.Field.ID,
                        Place.Field.NAME,
                        Place.Field.ADDRESS,
                        Place.Field.LAT_LNG
                    )
                ).build(context)
                activityResultLauncher.launch(intent)
            },
            modifier = Modifier.align(Alignment.Start),
            colors = ButtonDefaults.buttonColors(Color(0xFF3797EF)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(stringResource(R.string.select_location), color = Color.White)
        }
    }
}