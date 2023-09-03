package hr.itrojnar.instagram.view.utility

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun UsageLine(percentage: Double) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .padding(start = 10.dp, end = 10.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(Color.Gray)
    ) {
        LinearProgressIndicator(
            progress = percentage.toFloat() / 100,
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp),
            color = Color.Blue
        )
    }
}