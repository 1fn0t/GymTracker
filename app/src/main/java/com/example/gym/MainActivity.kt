package com.example.gym

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gym.database.StatisticsScreen
import com.example.gym.database.TrackerRepository
import com.example.gym.navigation.*
import com.example.gym.routines.AddRoutinesScreen
import com.example.gym.routines.RoutineDetailsScreen
import com.example.gym.routines.RoutinesScreen
import com.example.gym.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

private const val TAG = "Main Activity"
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var trackerRepo: TrackerRepository
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            GymTheme {
                val navModel: NavViewModel = viewModel()
                val navController = rememberNavController()
                val databaseModel: DatabaseViewModel = viewModel()
                Scaffold(
                    bottomBar = {
                        GymTrackNavigation(
                            switchScreen = { screen -> navModel.switchScreen(screen) },
                            navController = navController
                        )
                    },
                    topBar = {
                        AppBar(
                            barText = navModel.appBarText,
                            backBtnEnabled = navModel.backButtonEnabled,
                            backBtnColor = navModel.backButtonColor,
                            goBackToScreen = { navModel.switchBackToScreen() },
                            navController = navController
                        )
                    }
                ) { paddingValues ->
                    val mod = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                    NavHost(navController = navController, startDestination = Screen.Dashboard.route) {
                        composable(route = Screen.Dashboard.route) {
                            DashboardScreen(repoModel = databaseModel, mod)
                        }
                        composable(route = Screen.Profile.route) {
                            Text(text ="Profile", mod)
                        }
                        composable(route = Screen.Stats.route) {
                            StatisticsScreen(repoModel = databaseModel, modifier = mod)
                        }
                        composable(route = Screen.Routines.route) {
                            RoutinesScreen(navModel = navModel, navController = navController, repoModel = databaseModel,
                                modifier = mod)
                        }
                        composable(
                            route = Screen.RoutineDetails.route + "/{detailName}",
                            arguments = listOf(
                                navArgument("detailName") {
                                    type = NavType.StringType
                                    defaultValue = "Error"
                                    nullable = true
                                }
                            )
                        ) { entry ->
                            RoutineDetailsScreen(detailName = entry.arguments?.getString("detailName"),
                                repoModel = databaseModel, navController = navController, navModel = navModel, modifier =mod)
                        }
                        composable(route = Screen.AddRoutine.route) {
                            AddRoutinesScreen(context = this@MainActivity, repoModel = databaseModel, modifier = mod)
                        }
                    }
                }
            }
        }
    }
}

