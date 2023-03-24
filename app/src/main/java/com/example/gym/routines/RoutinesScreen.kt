package com.example.gym.routines

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.gym.NavViewModel
import com.example.gym.Routine
import com.example.gym.getSampleRoutines
import com.example.gym.R

@Composable
fun RoutinesScreen(
    switchToDetails: (Routine) -> Unit,
    switchToNestedScreen: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val mockRoutines = getSampleRoutines()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Button(onClick = { switchToNestedScreen(NavViewModel.SCREENCONTANTS.ADD_ROUTINES) }) {
                Text(
                    text = stringResource(R.string.add_routine)
                )
            }
            Button(onClick = { /*TODO*/ }) {
                Text(
                    text = stringResource(R.string.add_exercises)
                )
            }
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            itemsIndexed(mockRoutines) { _, item ->
                RoutinesItem(
                    name = item.name,
                    muscleGroups = item.muscleGroups,
                    showDetails = { switchToDetails(item) }
                )
            }
        }
    }
}

@Composable
fun RoutinesItem(
    name: String,
    muscleGroups: List<String>,
    showDetails: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable {
                showDetails()
            }
    ) {
        Text(
            text = name
        )
        Text(
            text = "Muscles Targeted: " + formatElementsInOneLine(muscleGroups)
        )
    }
}

fun formatElementsInOneLine(muscleGroups: List<String>): String {
    var resultStr = ""
    for (i in 0..muscleGroups.size-2) {
        resultStr += muscleGroups[i] + ", "
    }
    return resultStr + muscleGroups[muscleGroups.size-1]
}
