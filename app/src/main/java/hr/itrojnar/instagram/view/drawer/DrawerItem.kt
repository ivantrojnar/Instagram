package hr.itrojnar.instagram.view.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import hr.itrojnar.instagram.util.instagramGradientColors

@Composable
fun DrawerItem(icon: ImageVector, label: String, isSelected: Boolean, action: () -> Unit) {

    val darkTheme = isSystemInDarkTheme()
    val backgroundThemeColor = if (darkTheme) Color.Black else Color.Transparent

    val gradientBrush = Brush.linearGradient(colors = instagramGradientColors)

    val backgroundColor = if (isSelected) gradientBrush else Brush.linearGradient(listOf(backgroundThemeColor, backgroundThemeColor))
    val contentColor = if (isSelected) Color.White else Color.Gray

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = action)
            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = label,
            color = contentColor,
            modifier = Modifier.padding(16.dp)
        )
    }
}