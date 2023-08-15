package hr.itrojnar.instagram.view

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.IconButton
import androidx.compose.material.ModalDrawer
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.CameraFront
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.rounded.Camera
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.rememberDrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.nav.BottomNavGraph
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

//    ModalDrawer(
//        drawerState = drawerState,
//        gesturesEnabled = true,
//        drawerContent = {
//            DrawerContent(navController, drawerState)
//        }) {
//
//    }
//    Scaffold(
//        bottomBar = { BottomBar(navController = navController) }
//    ) {
//        BottomNavGraph(navController = navController)
//    }
    Scaffold(
        topBar = {
            TopAppBarWithBorder(
                backgroundColor = colorResource(id = R.color.very_light_gray),
                contentColor = Color.Black,
                bottomBorderColor = Color(0xFFCCCCCC )
            ) {
                IconButton(onClick = {
                    scope.launch {
                        if (drawerState.isClosed) {
                            drawerState.open()
                        } else {
                            drawerState.close()
                        }
                    }
                }) {
                    Icon(Icons.Filled.Menu, contentDescription = "Open Drawer")
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        },
        bottomBar = { BottomBar(navController = navController) }
    ) {
        ModalDrawer(
            drawerState = drawerState,
            gesturesEnabled = true,
            drawerContent = {
                DrawerContent(navController, drawerState)
            }) {
            BottomNavGraph(navController = navController)
        }
    }
}

@Composable
fun TopAppBarWithBorder(
    backgroundColor: Color,
    contentColor: Color,
    bottomBorderColor: Color,
    content: @Composable RowScope.() -> Unit
) {
    Box {
        TopAppBar(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        ) {
            content()

            Spacer(modifier = Modifier.weight(1f))

            val painter = painterResource(id = R.drawable.instagram_logo)
            Icon(painter = painter, contentDescription = "Instagram Logo", modifier = Modifier.size(125.dp))

            Spacer(modifier = Modifier.weight(1f))

            Row {
                IconButton(onClick = { /* Handle camera click */ }) {
                    Icon(Icons.Outlined.CameraAlt, contentDescription = "Open Camera")
                }
                IconButton(onClick = { /* Handle send/paper plane click */ }) {
                    Icon(Icons.Outlined.Send, contentDescription = "Direct Messages")
                }
            }
        }
        Divider(
            color = bottomBorderColor,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}


@Composable
fun BottomBar(navController: NavHostController) {


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Box {
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
        Divider(
            color = Color(0xFFCCCCCC),
            modifier = Modifier.align(Alignment.TopCenter)
        )
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

@Composable
fun DrawerContent(
    navController: NavHostController,
    drawerState: DrawerState
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Drawer Header",
            style = TextStyle(fontSize = 24.sp),
            modifier = Modifier.padding(16.dp)
        )
        Divider()

        ListOfDrawerItems(navController, drawerState)
    }
}

@Composable
fun ListOfDrawerItems(
    navController: NavHostController,
    drawerState: DrawerState
) {
    val coroutineScope = rememberCoroutineScope()

    val closeDrawerAction = {
        if (drawerState.isOpen) {
            coroutineScope.launch {
                drawerState.close()
            }
        }
    }

    ListItem(
        text = "Home",
        icon = Icons.Default.Home,
        action = {
            navController.navigate("home")
            closeDrawerAction()
        }
    )

    ListItem(
        text = "Profile",
        icon = Icons.Default.Person,
        action = {
            navController.navigate("profile")
            closeDrawerAction()
        }
    )
}

@Composable
fun ListItem(
    text: String,
    icon: ImageVector,
    action: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = action)
            .padding(16.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(Modifier.width(16.dp))
        Text(text = text, style = TextStyle(fontSize = 16.sp))
    }
}
