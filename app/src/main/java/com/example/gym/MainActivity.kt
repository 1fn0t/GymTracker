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
import com.example.gym.database.TrackerRepository
import com.example.gym.navigation.AppBar
import com.example.gym.navigation.GymTrackNavigation
import com.example.gym.navigation.NavViewModel
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
                // A surface container using the 'background' color from the theme
                val navModel: NavViewModel = viewModel()
                Scaffold(
                    bottomBar = {
                        GymTrackNavigation(selectNavItem = { item ->
                            navModel.switchScreen(item)
                        },
                        currentScreen = navModel.currentScreen
                            )
                    },
                    topBar = {
                        AppBar(
                            barText = navModel.appBarText,
                            backBtnEnabled = navModel.backButtonEnabled,
                            backBtnColor = navModel.backButtonColor,
                            goBackToScreen = { navModel.switchBackToScreen() }
                        )
                    }
                ) { paddingValues ->
                    val mod = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                    when (navModel.currentScreen) {
                        NavViewModel.SCREENCONTANTS.DASHBOARD -> {
                            Text(text ="Dashboard", modifier = mod)
                        }
                        NavViewModel.SCREENCONTANTS.ROUTINES -> {
                            RoutinesScreen(switchToDetails = { item -> navModel.switchToDetails(item) },
                                switchToNestedScreen = { num -> navModel.switchToNestedScreen(num) },
//                                trackerRepo = trackerRepo,
                                modifier = mod)
//                            Text(text ="Routines", modifier = mod)
                        }
                        NavViewModel.SCREENCONTANTS.STATS -> {
                            Text(text ="Stats", modifier = mod)
                        }
                        NavViewModel.SCREENCONTANTS.PROFILE -> {
                            Text(text ="Profile", modifier = mod)
                        }
                        NavViewModel.SCREENCONTANTS.ROUTINE_DETAILS -> {
                            RoutineDetailsScreen(routine = getSampleRoutine(), modifier = mod)
                        }
                        NavViewModel.SCREENCONTANTS.ADD_ROUTINES -> {
                            AddRoutinesScreen(this, modifier = mod)
                        }
                    }
                }

            }
        }
    }
}

