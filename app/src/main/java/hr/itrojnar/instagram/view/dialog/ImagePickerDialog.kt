package hr.itrojnar.instagram.view.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ImagePickerDialog(
    onTakePhoto: () -> Unit,
    onSelectFromGallery: () -> Unit,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Choose Profile Picture") },
        text = {
            Column {
                TextButton(onClick = onTakePhoto) {
                    Text(text = "Take Photo")
                }
                TextButton(onClick = onSelectFromGallery) {
                    Text(text = "Select from Gallery")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Cancel")
            }
        }
    )
}