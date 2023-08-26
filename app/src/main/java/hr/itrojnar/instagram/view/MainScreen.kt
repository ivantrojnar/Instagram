package hr.itrojnar.instagram.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.IconButton
import androidx.compose.material.ModalDrawer
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Send
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.ExperimentalPagingApi
import hr.itrojnar.instagram.model.DummyData
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.model.User
import hr.itrojnar.instagram.nav.BottomNavGraph
import hr.itrojnar.instagram.view.drawer.DrawerFooter
import hr.itrojnar.instagram.view.drawer.DrawerHeader
import hr.itrojnar.instagram.view.drawer.DrawerItem
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagingApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val user = DummyData.dummyUser

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    //val isHomeScreen = currentDestination?.route == "home"

    ModalDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            DrawerContent(navController, drawerState, user)
        }) {

        Scaffold(
            topBar = {
                if (currentDestination == "home") {
                    TopAppBarWithBorder(
                        navController = navController,
                        backgroundColor = colorResource(id = R.color.very_light_gray),
                        contentColor = Color.Black,
                        bottomBorderColor = Color(0xFFCCCCCC)
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
                }
            },
            bottomBar = {
                if (currentDestination != "camera") {
                    BottomBar(navController = navController)
                }
            }
        ) {
            BottomNavGraph(navController = navController)
        }
    }
}


@Composable
fun TopAppBarWithBorder(
    navController: NavHostController,
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
                IconButton(onClick = { navController.navigate("camera") }) {
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
            BottomNavScreen.items.forEach {
                AddItem(
                    screen = it,
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
fun DrawerContent(navController: NavHostController, drawerState: DrawerState, user: User) {

    val coroutineScope = rememberCoroutineScope()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val closeDrawer = {
        if (drawerState.isOpen) {
            coroutineScope.launch {
                drawerState.close()
                Log.e("CUSTOM TAG", currentDestination.toString())
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
            .background(Color.Transparent),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        DrawerHeader(user = user)

        DrawerItem(icon = Icons.Default.Home, label = "Home", isSelected = currentDestination?.route == "home") {
            navController.navigate("home")
            closeDrawer()
        }

        DrawerItem(icon = Icons.Default.Search, label = "Search", isSelected = currentDestination?.route == "search") {
            navController.navigate("search")
            closeDrawer()
        }

        DrawerItem(icon = Icons.Default.Map, label = "Map", isSelected = currentDestination?.route == "map") {
            navController.navigate("map")
            closeDrawer()
        }

        DrawerItem(icon = Icons.Default.Person, label = "Profile", isSelected = currentDestination?.route == "profile") {
            navController.navigate("profile")
            closeDrawer()
        }

        DrawerItem(icon = Icons.Default.Settings, label = "Settings", isSelected = currentDestination?.route == "settings") {
            navController.navigate("settings")
            closeDrawer()
        }

        Spacer(modifier = Modifier.weight(1f))

        DrawerFooter {
            // TODO IMPLEMENT LOG OUT LOGIC
        }
    }
}
@Composable
fun ListOfDrawerItems(
    navController: NavHostController,
    drawerState: DrawerState
) {
    val coroutineScope = rememberCoroutineScope()
    val currentDestination = navController.currentBackStackEntry?.destination


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
        },
        isSelected = currentDestination?.route == "home"
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
    action: () -> Unit,
    isSelected: Boolean = false
) {
    val backgroundColor = if (isSelected) Color(0xFFEFEFEF) else Color.White
    val contentColor = if (isSelected) Color(0xFFE4405F) else Color.Gray

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .clickable(onClick = action)
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp)) // Border radius
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = contentColor)
        Spacer(Modifier.width(16.dp))
        Text(text = text, style = TextStyle(fontSize = 16.sp, color = contentColor))
    }
}