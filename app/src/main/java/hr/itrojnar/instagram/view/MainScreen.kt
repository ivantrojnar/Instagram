package hr.itrojnar.instagram.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.ExperimentalPagingApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.api.FirebaseUserRepository
import hr.itrojnar.instagram.api.UserRepository
import hr.itrojnar.instagram.model.User
import hr.itrojnar.instagram.nav.BottomNavGraph
import hr.itrojnar.instagram.util.LottieAnimationLoop
import hr.itrojnar.instagram.util.putUser
import hr.itrojnar.instagram.view.drawer.DrawerFooter
import hr.itrojnar.instagram.view.drawer.DrawerHeader
import hr.itrojnar.instagram.view.drawer.DrawerItem
import hr.itrojnar.instagram.viewmodel.MainScreenViewModel
import hr.itrojnar.instagram.viewmodel.UserState
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagingApi::class)
@Composable
fun MainScreen(navHostController: NavHostController) {

    val darkTheme = isSystemInDarkTheme()
    val backgroundColor = if (darkTheme) Color.Black else Color.Transparent
    val topAppBarBackgroundColor =
        if (darkTheme) Color(0xFF121212) else colorResource(id = R.color.very_light_gray)
    val bottomBorderColor = if (darkTheme) Color(0xFF232323) else Color(0xFFCCCCCC)

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val viewModel: MainScreenViewModel = hiltViewModel()

    val userState by viewModel.userState.observeAsState(initial = UserState.Loading)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    val isMapScreen = currentDestination == "map"

    val context = LocalContext.current

    when (userState) {
        is UserState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimationLoop(
                    resId = R.raw.loading_animation,
                    Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            }
        }

        is UserState.Loaded -> {
            val user = (userState as UserState.Loaded).user
            val sharedPreferences =
                context.getSharedPreferences("user_details", Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putUser(user)
            }
            ModalDrawer(
                drawerState = drawerState,
                gesturesEnabled = !isMapScreen,
                drawerContent = {
                    DrawerContent(
                        navController,
                        navHostController,
                        drawerState,
                        user,
                        context,
                        viewModel,
                        backgroundColor,
                        topAppBarBackgroundColor,
                        bottomBorderColor,
                    )
                }) {

                Scaffold(
                    topBar = {
                        if (currentDestination == "home") {
                            TopAppBarWithBorder(
                                navController = navController,
                                backgroundColor = topAppBarBackgroundColor,
                                contentColor = Color.Black,
                                bottomBorderColor = bottomBorderColor
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
                                    Icon(
                                        Icons.Filled.Menu,
                                        contentDescription = stringResource(R.string.open_drawer)
                                    )
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

        is UserState.Error -> {
            Text(stringResource(R.string.an_error_occurred))
        }

        is UserState.Default -> {
            Text(stringResource(R.string.an_error_occurred))
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
    val logo =
        if (isSystemInDarkTheme()) R.drawable.instagram_logo_dark else R.drawable.instagram_logo
    Box {
        TopAppBar(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        ) {
            content()

            Spacer(modifier = Modifier.weight(1f))

            val painter = painterResource(id = logo)
            Icon(
                painter = painter,
                contentDescription = stringResource(R.string.instagram_logo_cd),
                modifier = Modifier.size(125.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Row {
                IconButton(onClick = { navController.navigate("camera") }) {
                    Icon(
                        Icons.Outlined.CameraAlt,
                        contentDescription = stringResource(R.string.open_camera)
                    )
                }
                IconButton(onClick = { /* Handle send/paper plane click */ }) {
                    Icon(
                        Icons.Outlined.Send,
                        contentDescription = stringResource(R.string.direct_messages)
                    )
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

    val bottomBarBackgroundColor =
        if (isSystemInDarkTheme()) Color(0xFF121212) else colorResource(id = R.color.very_light_gray)
    val topBorderColor = if (isSystemInDarkTheme()) Color(0xFF232323) else Color(0xFFCCCCCC)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Box {
        BottomNavigation(
            backgroundColor = bottomBarBackgroundColor
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
            color = topBorderColor,
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
    val tintSelected =
        if (isSystemInDarkTheme()) Color.White else Color.Black
    val tintNotSelected =
        if (isSystemInDarkTheme()) Color.LightGray else Color.Gray

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
                tint = if (isSelected) tintSelected else Color.Gray,
                modifier = Modifier.size(animatedSize)
            )
        },
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
    navHostController: NavHostController,
    drawerState: DrawerState,
    user: User,
    context: Context,
    viewModel: MainScreenViewModel,
    backgroundColor: Color,
    topAppBarBackgroundColor: Color,
    bottomBorderColor: Color
) {

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            DrawerHeader(user = user)

            DrawerItem(
                icon = Icons.Default.Home,
                label = stringResource(id = R.string.home),
                isSelected = currentDestination?.route == "home"
            ) {
                navController.navigate("home")
                closeDrawer()
            }

            DrawerItem(
                icon = Icons.Default.Search,
                label = stringResource(id = R.string.search),
                isSelected = currentDestination?.route == "search"
            ) {
                navController.navigate("search")
                closeDrawer()
            }

            DrawerItem(
                icon = Icons.Default.Map,
                label = stringResource(id = R.string.map),
                isSelected = currentDestination?.route == "map"
            ) {
                navController.navigate("map")
                closeDrawer()
            }

            DrawerItem(
                icon = Icons.Default.Person,
                label = stringResource(id = R.string.profile),
                isSelected = currentDestination?.route == "profile"
            ) {
                navController.navigate("profile")
                closeDrawer()
            }

            DrawerItem(
                icon = Icons.Default.Settings,
                label = stringResource(id = R.string.settings),
                isSelected = currentDestination?.route == "settings"
            ) {
                navController.navigate("settings")
                closeDrawer()
            }

            Spacer(modifier = Modifier.weight(1f))

            DrawerFooter {
                FirebaseAuth.getInstance().signOut()
                if (FirebaseAuth.getInstance().currentUser == null) {
                    Log.d("LOGOUT", "User successfully logged out")
                } else {
                    Log.d("LOGOUT", "Something went wrong")
                }
                FirebaseAuth.getInstance().currentUser?.reload()

                val sharedPreferences =
                    context.getSharedPreferences("user_details", Context.MODE_PRIVATE)
                sharedPreferences.edit().clear().apply()

                while (navController.popBackStack()) {

                }

                viewModel.clearResources()

                navHostController.navigate(AuthScreen.Login.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
            }
        }
    }
}
