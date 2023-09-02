package hr.itrojnar.instagram.view.utility

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.unit.dp

@Composable
fun SubscriptionCard(
    title: String,
    description: String,
    gradient: List<Color>,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    val border = if (isSelected) BorderStroke(4.dp, Color.Black) else null

    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        border = border,
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(200.dp)
                .background(Brush.horizontalGradient(gradient))
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        shadow = Shadow(
                            Color.Black,
                            offset = Offset(1f, 1f),
                            blurRadius = 2f
                        )
                    ),
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        shadow = Shadow(
                            Color.Black,
                            offset = Offset(1f, 1f),
                            blurRadius = 2f
                        )
                    ),
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}