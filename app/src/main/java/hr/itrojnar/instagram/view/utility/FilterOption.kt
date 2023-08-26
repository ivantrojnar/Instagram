package hr.itrojnar.instagram.view.utility

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.itrojnar.instagram.util.instagramGradientColors

@Composable
fun FilterOption(
    modifier: Modifier = Modifier,
    option: String,
    isSelected: Boolean = false,
    onOptionSelected: (String) -> Unit,
) {
    // Check if instagramGradient has at least 2 colors, or else set a default list
    val safeInstagramGradient = if (instagramGradientColors.size >= 2) {
        instagramGradientColors
    } else {
        listOf(Color.Gray, Color.Gray) // Default to a single-color gradient for safety
    }

    val gradientBrush = Brush.horizontalGradient(safeInstagramGradient)

    // These animate*AsState functions allow properties to smoothly animate between their values
    val backgroundColor by animateColorAsState(
        if (isSelected) Color.Gray else Color(0xFFF0F0F0)
    )

    val textColor by animateColorAsState(
        if (isSelected) Color.White else Color.DarkGray
    )

    Text(
        text = option,
        modifier = modifier
            .run {
                if (isSelected) {
                    this.background(brush = gradientBrush, shape = RoundedCornerShape(8.dp))
                } else {
                    this.background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
                }
            }
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .clickable { onOptionSelected(option) }
            .padding(8.dp),
        style = TextStyle(
            color = textColor,
            fontSize = 14.sp, // Single font size
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    )
}