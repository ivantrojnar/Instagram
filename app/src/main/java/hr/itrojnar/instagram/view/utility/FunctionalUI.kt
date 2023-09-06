package hr.itrojnar.instagram.view.utility

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import hr.itrojnar.instagram.util.capitalize
import hr.itrojnar.instagram.util.concatenateStrings
import hr.itrojnar.instagram.util.doubleMultiplier
import hr.itrojnar.instagram.util.isEvenOrOdd
import hr.itrojnar.instagram.util.maxOfTwo

@Composable
fun FunctionalUI() {

    val darkTheme = isSystemInDarkTheme()
    val backgroundColor = if (darkTheme) Color.Black else Color.Transparent

    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(backgroundColor)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Example for Double Multiplier with custom logic
        val doubleResult = doubleMultiplier(5) { it * 3 } // triples the number
        Text(text = "Triple of 5 is: $doubleResult")

        // Example for Is Even Or Odd
        val evenOrOddCheck = isEvenOrOdd(10)
        Text(text = "10 is $evenOrOddCheck")

        // Example for Max Of Two
        val maxResult = maxOfTwo(10, 20)
        Text(text = "Max of 10 and 20 is: $maxResult")

        // Example for Concatenate Strings with custom formatting
        val concatResult = concatenateStrings("Hello", " World!") { a, b -> "[$a$b]" } // Adds [] around the concatenated strings
        Text(text = concatResult)

        // Example for Capitalize
        val capitalizedResultFirst = capitalize("hello world")
        val capitalizedResultLast = capitalize("hello world", "last")
        Text(text = "First Word Capitalized: $capitalizedResultFirst")
        Text(text = "Last Word Capitalized: $capitalizedResultLast")
    }
}