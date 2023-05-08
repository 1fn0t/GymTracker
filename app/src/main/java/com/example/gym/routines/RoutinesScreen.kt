package com.example.gym.routines

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gym.*
import com.example.gym.R
import com.example.gym.database.TrackerRepository
import com.example.gym.navigation.NavViewModel
import com.example.gym.navigation.Screen
import com.example.gym.ui.theme.Green700
import com.example.gym.ui.theme.Grey300
import com.example.gym.ui.theme.Grey500
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

private const val TAG = "Routines Screen"

@Composable
fun RoutinesScreen(
    navModel: NavViewModel,
    navController: NavController,
//    repoModel: DatabaseViewModel = viewModel(),
    repoModel: DatabaseViewModel,
    muscleModel: MuscleViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val routines = repoModel.retrieveRoutinesFromDB().collectAsState(initial = listOf())
    var enteredText = remember { mutableStateOf(TextFieldValue("")) }
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(horizontal = 24.dp)
    ) {
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                OutlinedButton(onClick = {
                    navModel.switchScreen(Screen.AddRoutine)
                    navController.navigate(Screen.AddRoutine.route)
                }) {
                    Text(
                        text = stringResource(R.string.add_routine),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                ElevatedButton(onClick = { repoModel.storeExerciseInDB(enteredText.value.text, muscleModel.muscles.toList()) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Green700,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = stringResource(R.string.create_exercises),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
        item {
            NameTextField(enteredName = enteredText, labelText = "Enter exercise name")
        }
        item {
            Divider(color = Grey300)
            MuscleCheckboxes(addMuscles = { muscle -> muscleModel.addMuscle(muscle) },
                removeMuscles = { muscle -> muscleModel.removeMuscle(muscle) },
            )
            Divider(color = Grey500)
        }
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                routines.value.forEachIndexed { _, item ->
                    RoutinesItem(
                        name = item.name,
                        muscleGroups = item.muscleGroups,
                        showDetails = {
//                        switchToDetails(item)
//                        navModel.switchWithArgs(Screen.RoutineDetails, item.name, item.name)
                            navModel.switchScreen(Screen.RoutineDetails)
                            navModel.updateTopBarText(item.name)
                            navController.navigate(Screen.RoutineDetails.withArgs(item.name))
                        }
                    )
                }
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            muscleModel.clearMuscles()
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
        modifier = modifier.fillMaxWidth()
            .clickable {
                showDetails()
            }
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            text = "Muscles Targeted: " + formatElementsInOneLine(muscleGroups),
            style = MaterialTheme.typography.bodyMedium
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
