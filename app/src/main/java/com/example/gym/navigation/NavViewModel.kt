package com.example.gym.navigation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gym.Routine
import com.example.gym.ui.theme.Green700

class NavViewModel(
//    var navController: NavController
): ViewModel() {
    //    var currentScreen by mutableStateOf(SCREENCONTANTS.DASHBOARD)
//        private set
//    var navController by navC
    var currentScreen: Screen by mutableStateOf(Screen.Dashboard)
    var appBarText by mutableStateOf("Dashboard")
        private set
    var backButtonEnabled by mutableStateOf(false)
        private set
    var backButtonColor by mutableStateOf(Color.Transparent)
        private set
    var lastScreen: Screen by mutableStateOf(Screen.Dashboard)

    fun switchScreen(screen: Screen) {
        lastScreen = currentScreen
        currentScreen = screen
        appBarText = getScreenBarText(screen)
        if (screen == Screen.AddRoutine || screen == Screen.RoutineDetails) {
            backButtonEnabled = true
            backButtonColor = Green700
        }
    }

    fun updateTopBarText(text: String) {
        appBarText = text
    }

    fun switchBackToScreen(): Screen {
        currentScreen = lastScreen
        backButtonEnabled = false
        backButtonColor = Color.Transparent
        appBarText = getScreenBarText(lastScreen)
        return lastScreen
    }

    fun getScreenBarText(screen: Screen): String {
        when (screen) {
            Screen.Dashboard -> {
                return "Dashboard"
            }
            Screen.Routines -> {
                return "Routines"
            }
            Screen.Stats -> {
                return "Statistics"
            }
            Screen.Profile -> {
                return "Profile"
            }
            Screen.AddRoutine -> {
                return "Add Routines"
            }
            else -> {}
        }
        return "Error"
    }
//    fun switchToDetails(routine: Routine) {
//        appBarText = routine.name
//        switchToNestedScreen(SCREENCONTANTS.ROUTINE_DETAILS)
//        Log.d("Screen Details", routine.name)
//    }

//    fun switchToNestedScreen(screenNum: Int) {
//        lastScreen = currentScreen
//        backButtonEnabled = true
//        backButtonColor = Green700
//        switchScreen(screenNum)
//    }

//    fun switchBackToScreen() {
//        switchScreen(lastScreen)
//        backButtonEnabled = false
//        backButtonColor = Color.Transparent
//    }


//    object SCREENCONTANTS {
//        const val DASHBOARD = 0
//        const val ROUTINES = 1
//        const val STATS = 2
//        const val PROFILE = 3
//        const val ROUTINE_DETAILS = 4
//        const val ADD_ROUTINES = 5
//    }
}
