package com.example.gym.navigation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.example.gym.Routine
import com.example.gym.ui.theme.Green700

class NavViewModel: ViewModel() {
    var currentScreen by mutableStateOf(SCREENCONTANTS.DASHBOARD)
        private set
    var appBarText by mutableStateOf("Dashboard")
        private set
    var backButtonEnabled by mutableStateOf(false)
        private set
    var backButtonColor by mutableStateOf(Color.Transparent)
        private set
    var lastScreen by mutableStateOf(SCREENCONTANTS.DASHBOARD)

//    var currentDetails: State<Routine?> by mutableStateOf(null)
//        private set

    fun switchScreen(screenNum: Int) {
        currentScreen = screenNum
        when (screenNum) {
            SCREENCONTANTS.DASHBOARD -> {
                appBarText = "Dashboard"
            }
            SCREENCONTANTS.ROUTINES -> {
                appBarText = "Routines"
            }
            SCREENCONTANTS.STATS -> {
                appBarText = "Statistics"
            }
            SCREENCONTANTS.PROFILE -> {
                appBarText = "Profile"
            }
            else -> {}
        }
    }

    fun switchToDetails(routine: Routine) {
        appBarText = routine.name
        switchToNestedScreen(SCREENCONTANTS.ROUTINE_DETAILS)
        Log.d("Screen Details", routine.name)
    }

    fun switchToNestedScreen(screenNum: Int) {
        lastScreen = currentScreen
        backButtonEnabled = true
        backButtonColor = Green700
        switchScreen(screenNum)
    }

    fun switchBackToScreen() {
        switchScreen(lastScreen)
        backButtonEnabled = false
        backButtonColor = Color.Transparent
    }


    object SCREENCONTANTS {
        const val DASHBOARD = 0
        const val ROUTINES = 1
        const val STATS = 2
        const val PROFILE = 3
        const val ROUTINE_DETAILS = 4
        const val ADD_ROUTINES = 5
    }
}
