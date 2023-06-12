package com.hevanafa.navigationdrawerdemo

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.hevanafa.navigationdrawerdemo.ui.theme.NavigationDrawerDemoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationDrawerDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BaseScaffold()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseScaffold() {
    // for navigation & in-app routing
    val navController: NavHostController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screens.valueOf(
        backStackEntry?.destination?.route ?: Screens.Start.name
    )
    val canNavigateBack = navController.previousBackStackEntry != null

    // for navigation drawer
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { MainDrawerSheet(navController, scope, drawerState) }) {
        Scaffold (topBar = {
            MainAppBar {
                scope.launch {
                    drawerState.open()
                }
            }
        }) { innerPadding ->
            NavHost(navController, Screens.Start.name) {
                composable(Screens.Start.name) {
                    Column (modifier = Modifier.padding(innerPadding)) {
                        Text("Start Screen")
                    }
                }

                composable(Screens.Page1.name) {
                    Column (modifier = Modifier.padding(innerPadding)) {
                        Text("First page")
                    }
                }

                composable(Screens.Page2.name) {
                    Column (modifier = Modifier.padding(innerPadding)) {
                        Text("Second page")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppBar(
    modifier: Modifier = Modifier,
    navigationClick: () -> Unit = {}
) {
    TopAppBar(
        title = { Text("Example Drawer Navigation") },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = navigationClick) {
                Icon(
                    imageVector = Icons.Filled.List,
                    contentDescription = "Navigation menu"
                )
            }
        }
    )
}

@Composable
fun DrawerHeader() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
        .height(200.dp),
        horizontalArrangement = Arrangement.Center) {

        Image(
            painter = painterResource(R.drawable.juno),
            contentDescription = "Juno from Omega Strikers")

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainDrawerSheet(
    navController: NavHostController,
    scope: CoroutineScope,
    drawerState: DrawerState) {

    ModalDrawerSheet {
        DrawerHeader()

        DrawerItem(Icons.Filled.Home, "Start") {
            navController.navigate(Screens.Start.name)
            scope.launch {
                drawerState.close()
            }
        }

        DrawerItem(Icons.Filled.DateRange, "Page 1") {
            navController.navigate(Screens.Page1.name)
            scope.launch {
                drawerState.close()
            }
        }

        DrawerItem(Icons.Filled.Email, "Page 2") {
            navController.navigate(Screens.Page2.name)
            scope.launch {
                drawerState.close()
            }
        }
    }
}

@Composable
fun DrawerItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ){
        Icon(
            imageVector = icon,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text)
    }
}
