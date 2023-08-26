package hr.itrojnar.instagram.view

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
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
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import coil.transform.Transformation
import com.commit451.coiltransformations.BlurTransformation
import com.commit451.coiltransformations.ColorFilterTransformation
import com.commit451.coiltransformations.GrayscaleTransformation
import com.commit451.coiltransformations.SquareCropTransformation
import com.commit451.coiltransformations.gpu.BrightnessFilterTransformation
import com.commit451.coiltransformations.gpu.InvertFilterTransformation
import com.commit451.coiltransformations.gpu.KuwaharaFilterTransformation
import com.commit451.coiltransformations.gpu.PixelationFilterTransformation
import com.commit451.coiltransformations.gpu.SepiaFilterTransformation
import com.commit451.coiltransformations.gpu.SketchFilterTransformation
import com.commit451.coiltransformations.gpu.SwirlFilterTransformation
import com.commit451.coiltransformations.gpu.ToonFilterTransformation
import com.commit451.coiltransformations.gpu.VignetteFilterTransformation
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.view.dialog.ImagePickerDialog
import hr.itrojnar.instagram.view.dialog.LoadingDialog
import hr.itrojnar.instagram.view.utility.ImageFilterOption
import hr.itrojnar.instagram.view.utility.NoTransformation
import hr.itrojnar.instagram.viewmodel.CameraViewModel
import kotlinx.coroutines.launch
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

    var selectedTransformation by remember { mutableStateOf<Transformation>(NoTransformation()) }
    var selectedFilter by remember { mutableStateOf("No filter") }

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
                    selectedTransformation = NoTransformation()
                }
            }
        }

    val pickImageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            viewModel.setImageUri(uri)
            selectedTransformation = NoTransformation()
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
        Toast.makeText(
            context,
            stringResource(R.string.the_post_was_created_successfully),
            Toast.LENGTH_SHORT
        ).show()
        navController.popBackStack()
    }

    val layoutCoordinatesState = remember { mutableStateOf<LayoutCoordinates?>(null) }

    val filterOptions = listOf(
        Pair(stringResource(R.string.no_filter), NoTransformation()),
        Pair(stringResource(R.string.blur), BlurTransformation(context)),
        Pair(stringResource(R.string.circle_crop), CircleCropTransformation()),
        Pair(stringResource(R.string.sepia), SepiaFilterTransformation(context)),
        Pair(stringResource(R.string.Red), ColorFilterTransformation(Color.Red.copy(0.2f).hashCode())),
        Pair(stringResource(R.string.Green), ColorFilterTransformation(Color.Green.copy(0.2f).hashCode())),
        Pair(stringResource(R.string.Blue), ColorFilterTransformation(Color.Blue.copy(0.2f).hashCode())),
        Pair(stringResource(R.string.grayscale), GrayscaleTransformation()),
        Pair(stringResource(R.string.invert), InvertFilterTransformation(context)),
        Pair(stringResource(R.string.kuwahara), KuwaharaFilterTransformation(context)),
        Pair(stringResource(R.string.pixelation), PixelationFilterTransformation(context)),
        Pair(stringResource(R.string.sketch), SketchFilterTransformation(context)),
        Pair(stringResource(R.string.swirl), SwirlFilterTransformation(context)),
        Pair(stringResource(R.string.toon), ToonFilterTransformation(context)),
        Pair(stringResource(R.string.vignette), VignetteFilterTransformation(context)),
        Pair(stringResource(R.string.brightness), BrightnessFilterTransformation(context, 0.5f))
    )

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
                    .onGloballyPositioned { layoutCoordinates ->
                        layoutCoordinatesState.value = layoutCoordinates
                    },
            ) {
                imageUri?.let { uri ->
                    val painter = rememberImagePainter(
                        data = uri,
                        builder = {
                            selectedTransformation?.let { transformation ->
                                transformations(transformation)
                            }
                        }
                    )
                    Image(
                        painter = painter,
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

            Spacer(modifier = Modifier.padding(top = 16.dp))
            
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(count = filterOptions.size) { index ->
                    val (label, transformation) = filterOptions[index]
                    ImageFilterOption(
                        modifier = when (index) {
                            0 -> Modifier.padding(start = 16.dp)
                            filterOptions.size - 1 -> Modifier.padding(end = 8.dp)
                            else -> Modifier
                        },
                        label = label,
                        transformation = transformation,
                        isSelected = label == selectedFilter,
                        onOptionSelected = { newTransformation ->
                            selectedTransformation = newTransformation
                            selectedFilter = label
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

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
                onClick = {
                    viewModel.viewModelScope.launch {
                        val transformedBitmap = applyTransformationToBitmap(selectedTransformation, context, imageUri!!)
                        if (transformedBitmap != null) {
                            val savedUri = saveBitmapToMediaStore(transformedBitmap, context)
                            if (savedUri != null) {
                                viewModel.setImageUri(savedUri)
                            }
                        }
                        viewModel.createPost()
                    }
                },
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

fun saveBitmapToMediaStore(bitmap: Bitmap, context: Context): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "Edited_Image_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }

    val contentResolver = context.contentResolver
    val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    if (uri != null) {
        val outputStream = contentResolver.openOutputStream(uri)
        outputStream?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
    }

    return uri
}

private suspend fun applyTransformationToBitmap(transformation: Transformation?, context: Context, imageUri: Uri): Bitmap? {
    val imageLoader = ImageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(imageUri)
        .transformations(transformation ?: CircleCropTransformation())
        .build()

    val result = imageLoader.execute(request).drawable
    if (result is BitmapDrawable) {
        return result.bitmap
    }
    return null
}
