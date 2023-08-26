package hr.itrojnar.instagram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.AndroidEntryPoint
import hr.itrojnar.instagram.nav.RootNavGraph
import hr.itrojnar.instagram.ui.theme.InstagramTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, resources.getString(R.string.google_api_key))
        }
        setContent {
            InstagramTheme {
                Surface {
                    RootNavGraph(navController = rememberNavController())
                }
            }
        }
    }
}