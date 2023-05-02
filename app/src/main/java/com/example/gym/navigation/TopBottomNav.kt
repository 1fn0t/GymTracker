package com.example.gym.navigation

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.gym.NavIcon
import com.example.gym.R
import com.example.gym.ui.theme.Green700
import com.example.gym.ui.theme.Green900

object icons {
    private val screenConst = NavViewModel.SCREENCONTANTS
    val values = listOf(
        NavIcon(R.drawable.ic_home, R.string.dashboard_description, screenConst.DASHBOARD),
        NavIcon(R.drawable.ic_schedule, R.string.scheduler_description, screenConst.ROUTINES),
        NavIcon(R.drawable.ic_analytics, R.string.statistics_description, screenConst.STATS),
        NavIcon(R.drawable.icon_profile, R.string.profile_description, screenConst.PROFILE)
    )
}

@Composable
fun GymTrackNavigation(
    selectNavItem: (Int) -> Unit,
    currentScreen: Int,
    modifier: Modifier = Modifier
) {
    val screenConst = NavViewModel.SCREENCONTANTS
    BottomNavigation(
        backgroundColor = Color.White,
        contentColor = Green900,  // sets button clicked animation color
        modifier = modifier
    ) {
        icons.values.map {
            BottomNavigationItem(
                icon = {
                    Icon(painter = painterResource(it.icon),
                        contentDescription = stringResource(it.info),
                        tint = if (currentScreen == it.screenVal) Green900
                        else Color.Black
                    )
                },
                selected = true,
                onClick = { selectNavItem(it.screenVal) },
            )
        }
    }
}

@Composable
fun AppBar(
    barText: String,
    backBtnEnabled: Boolean,
    backBtnColor: Color,
    goBackToScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = barText,
                color = Green700
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                goBackToScreen()
            },
                enabled = backBtnEnabled
            ) {
                Icon(painter = painterResource(R.drawable.baseline_arrow_back_24),
                    contentDescription = "Toggle Drawer",
                    tint = backBtnColor
                )
            }
        },
        backgroundColor = Color.White,
        modifier = modifier
    )
}