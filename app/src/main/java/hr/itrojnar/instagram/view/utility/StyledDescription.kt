package hr.itrojnar.instagram.view.utility

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun StyledDescription(userName: String, postDescription: String) {
    val defaultColor = MaterialTheme.colorScheme.onSurface
    val hashtagColor = Color(0xFF45B8F0)  // Blue color

    // Create a builder for the AnnotatedString
    val annotatedString = buildAnnotatedString {
        // Append the user name with bold
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(userName)
            append(" ")  // Space after the user name
        }

        // Split the description into words and process each word
        postDescription.split(" ").forEach { word ->
            // Check if the word is a hashtag
            if (word.startsWith("#")) {
                withStyle(style = SpanStyle(color = hashtagColor)) {
                    append(word)
                }
            } else {
                withStyle(style = SpanStyle(color = defaultColor)) {
                    append(word)
                }
            }
            append(" ")  // Space after each word
        }
    }

    Text(text = annotatedString, modifier = Modifier.padding(horizontal = 8.dp))
}