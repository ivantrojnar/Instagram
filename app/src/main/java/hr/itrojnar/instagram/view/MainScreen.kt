package hr.itrojnar.instagram.view

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.nav.BottomNavGraph

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) {
        BottomNavGraph(navController = navController)
    }
}

@Composable
fun BottomBar(navController: NavHostController) {


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation(
        backgroundColor = colorResource(id = R.color.very_light_gray)
    ) {
        BottomNavScreen::class.sealedSubclasses.forEach {
            AddItem(
                screen = it.objectInstance!!,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomNavScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

    val iconSize = if (isSelected) 34.dp else 27.dp

    // Animate size changes
    val animatedSize by animateDpAsState(
        targetValue = iconSize,
        animationSpec = tween(durationMillis = 300)
    )

    BottomNavigationItem(
        icon = {
            val icon = if (isSelected) screen.selectedIcon else screen.icon
            Icon(
                imageVector = icon,
                contentDescription = stringResource(id = screen.title),
                tint = if (isSelected) Color.Black else Color.Gray,
                modifier = Modifier.size(animatedSize)
            )
        },
        //label = { Text(text = stringResource(id = screen.title))},
        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        })
}