package hr.itrojnar.instagram.view.drawer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.DrawerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import hr.itrojnar.instagram.model.User

@Composable
fun DrawerContent(navController: NavHostController, drawerState: DrawerState, user: User) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        DrawerHeader(user = user)

        DrawerItem(icon = Icons.Default.Home, label = "Home", isSelected = true) {
            navController.navigate("home")
            //closeDrawer(drawerState)
        }

        DrawerItem(icon = Icons.Default.Person, label = "Profile", isSelected = false) {
            navController.navigate("profile")
            //closeDrawer(drawerState)
        }

        Spacer(modifier = Modifier.weight(1f))

        DrawerFooter {
            // Implement your logout action here
        }
    }
}

//fun closeDrawer(drawerState: DrawerState) {
//    val coroutineScope = rememberCoroutineScope()
//    if (drawerState.isOpen) {
//        coroutineScope.launch {
//            drawerState.close()
//        }
//    }
//}
