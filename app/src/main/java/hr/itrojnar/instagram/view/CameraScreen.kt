package hr.itrojnar.instagram.view

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.util.findActivity
import hr.itrojnar.instagram.view.dialog.ImagePickerDialog
import hr.itrojnar.instagram.view.dialog.LoadingDialog
import hr.itrojnar.instagram.viewmodel.CameraViewModel
import java.io.ByteArrayOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(navController: NavHostController) {

    val context = LocalContext.current

    var showImagePickerDialog by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val viewModel: CameraViewModel = hiltViewModel()

    val isUploadSuccessful by remember { viewModel.isUploadedSuccessful }

    val focusManager = LocalFocusManager.current

    val takePictureLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            bitmap?.let {
                val bytes = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                val path = MediaStore.Images.Media.insertImage(
                    context.contentResolver, it, "Title", null
                )
                imageUri = Uri.parse(path)
                imageUri?.let { uri ->
                    viewModel.setImageUri(uri)
                }
            }
        }

    val pickImageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            viewModel.setImageUri(uri)
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

    if (viewModel.isLoading.value) {
        LoadingDialog()
    }

    if (isUploadSuccessful) {
        Toast.makeText(context, stringResource(R.string.the_post_was_created_successfully), Toast.LENGTH_SHORT).show()
        navController.popBackStack()
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
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(36.dp)
                        )
                    } else {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = "Camera",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Add the LocationInput composable here
            LocationInput(cameraViewModel = viewModel)

            Text(
                text = stringResource(R.string.description) + ":",
                modifier = Modifier.padding(horizontal = 16.dp),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
            )


            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Color.Transparent),
                shape = RoundedCornerShape(10.dp),
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    value = viewModel.description.value,
                    onValueChange = { newValue -> viewModel.setDescription(newValue) },
                    label = { Text(text = stringResource(R.string.description)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.clearFocus() }
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedLabelColor = Color.Gray, // Color for the hint text when not focused
                        unfocusedBorderColor = Color.Gray, // Color for the border when not focused
                        focusedLabelColor = Color.Black, // Color for the hint text when focused
                        focusedBorderColor = Color.Black, // Color for the border when focused
                    )
                )
            }
            
            Spacer(modifier = Modifier.size(10.dp))
            
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 16.dp),
                onClick = { viewModel.createPost() },
                enabled = viewModel.isReadyToPost,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color(0xFF3797EF).copy(alpha = 0.4f),
                    containerColor = Color(0xFF3797EF)
                ),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text(
                    stringResource(R.string.upload_post),
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun LocationInput(cameraViewModel: CameraViewModel) {
    val context = LocalContext.current
    var locationText by remember { mutableStateOf("No location selected") }

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
            text = "Selected location:",
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                //.padding(12.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                .padding(12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = locationText,
                style = TextStyle(fontSize = 16.sp),
                color = Color.Black
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
            Text(stringResource(R.string.select_location))
        }
    }
}
