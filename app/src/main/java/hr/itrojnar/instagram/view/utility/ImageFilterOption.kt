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
import coil.transform.Transformation
import hr.itrojnar.instagram.util.instagramGradientColors

@Composable
fun ImageFilterOption(
    modifier: Modifier = Modifier,
    label: String,
    transformation: Transformation,
    isSelected: Boolean = false,
    onOptionSelected: (Transformation) -> Unit,
) {
    val safeInstagramGradient = if (instagramGradientColors.size >= 2) {
        instagramGradientColors
    } else {
        listOf(Color.Gray, Color.Gray)
    }

    val gradientBrush = Brush.horizontalGradient(safeInstagramGradient)

    val backgroundColor by animateColorAsState(
        if (isSelected) Color.Gray else Color(0xFFF0F0F0)
    )

    val textColor by animateColorAsState(
        if (isSelected) Color.White else Color.DarkGray
    )

    Text(
        text = label,
        modifier = modifier
            .run {
                if (isSelected) {
                    this.background(brush = gradientBrush, shape = RoundedCornerShape(8.dp))
                } else {
                    this.background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
                }
            }
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .clickable { onOptionSelected(transformation) }
            .padding(8.dp),
        style = TextStyle(
            color = textColor,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    )
}