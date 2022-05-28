package com.example.anythingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.anythingapp.ui.theme.AnythingAppTheme
import java.security.AccessController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnythingAppTheme {
                var navController= rememberNavController()
                // A surface container using the 'background' color from the theme
              Scaffold (
                  topBar = { TopBar()},
                  bottomBar = { BottomNavigation(navController = navController)}
                      ){
                  Navigation(navController = navController)

              }
            }
        }
    }
}
@Composable
fun Navigation(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = NavigationItem.Home.route){
        composable(NavigationItem.Home.route){
            HomeScreen()
        }
        composable(NavigationItem.Music.route){
            MusicScreen()
        }
        composable(NavigationItem.Movies.route){
            MovieScreen()
        }
        composable(NavigationItem.Books.route){
            BooksScreen()
        }
        composable(NavigationItem.Profile.route){
            ProfileScreen()
        }
    }

}
@Composable
fun TopBar(){
    TopAppBar(
       contentColor = Color.White,
        backgroundColor = colorResource(id = R.color.colorPrimary),
    ) {

    }
}
@Composable
fun BottomNavigation(navController: NavHostController){
    var items= listOf(
        NavigationItem.Home,
        NavigationItem.Music,
        NavigationItem.Movies,
        NavigationItem.Books,
        NavigationItem.Profile
    )
    androidx.compose.material.BottomNavigation(
        backgroundColor = colorResource(id = R.color.colorPrimary),
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
      var currentRoute=  navBackStackEntry?.destination?.route
        items.forEach(){
            item->
          BottomNavigationItem(
              selected =currentRoute==item.route ,
              onClick = {
                        navController.navigate(item.route){
                            navController.graph.startDestinationRoute?.let {
                                route->popUpTo(route){
                                    saveState=true
                            }
                            }
                            launchSingleTop=true
                            restoreState=true

                        }

              },
              icon={Icon(painter = painterResource(id = item.icon),contentDescription = item.title)},
              label = { Text(text = item.title)},
              selectedContentColor = Color.White,
              unselectedContentColor = Color.White.copy(0.4f),
              alwaysShowLabel = true
          )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AnythingAppTheme {

    }
}