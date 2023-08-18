package hr.itrojnar.instagram.view

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.util.findActivity
import hr.itrojnar.instagram.view.dialog.ImagePickerDialog
import hr.itrojnar.instagram.viewmodel.CameraViewModel
import java.io.ByteArrayOutputStream

@Composable
fun CameraScreen(navController: NavHostController) {

    val context = LocalContext.current
    val activity = context.findActivity()

    var showImagePickerDialog by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val viewModel: CameraViewModel = viewModel()

    val takePictureLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            bitmap?.let {
                val bytes = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                val path = MediaStore.Images.Media.insertImage(
                    context.contentResolver, it, "Title", null
                )
                imageUri = Uri.parse(path)
            }
        }

    val pickImageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    if (showImagePickerDialog) {
        ImagePickerDialog(
            onTakePhoto = {
                takePictureLauncher.launch(null)
                showImagePickerDialog = false
            },
            onSelectFromGallery = {
                pickImageLauncher.launch("image/*")
                showImagePickerDialog = false
            },
            onDismissRequest = { showImagePickerDialog = false }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "Go back",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navController.popBackStack() }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Text(
                text = stringResource(R.string.create_new_post),
                style = TextStyle(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                fontSize = 22.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color.LightGray)
            ) {
                //val image = viewModel.selectedImage
                imageUri?.let {
                    val image: Painter = rememberAsyncImagePainter(model = it)
                    Image(
                        painter = image,
                        contentDescription = "Selected preview",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                IconButton(
                    onClick = { showImagePickerDialog = true },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                ) {
                    if (imageUri != null) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(36.dp))
                    } else {
                        Icon(Icons.Default.CameraAlt, contentDescription = "Camera", modifier = Modifier.size(36.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add the LocationInput composable here
            LocationInput(navController = navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationInput(navController: NavHostController) {
    val context = LocalContext.current
    var locationText by remember { mutableStateOf("No location selected") }

    val activityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val place = Autocomplete.getPlaceFromIntent(result.data!!)
            locationText = place.address ?: "Unknown location"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextField(
            value = locationText,
            onValueChange = { },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Location") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val intent = Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN,
                    listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
                ).build(context)
                activityResultLauncher.launch(intent)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Select Location")
        }
    }
}