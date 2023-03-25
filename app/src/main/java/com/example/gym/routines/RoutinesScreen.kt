package com.example.gym.routines

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gym.*
import com.example.gym.R
import com.example.gym.ui.theme.Green700
import com.example.gym.ui.theme.Grey300
import com.example.gym.ui.theme.Grey500

private const val TAG = "Routines Screen"

@Composable
fun RoutinesScreen(
    switchToDetails: (Routine) -> Unit,
    switchToNestedScreen: (Int) -> Unit,
    muscleModel: MuscleViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val mockRoutines = getSampleRoutines()
    var enteredText = remember { mutableStateOf(TextFieldValue("")) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            OutlinedButton(onClick = { switchToNestedScreen(NavViewModel.SCREENCONTANTS.ADD_ROUTINES) }) {
                Text(
                    text = stringResource(R.string.add_routine)
                )
            }
            ElevatedButton(onClick = {
                val newExercise = Exercise(
                    name = enteredText.value.text,
                    muscleGroups = muscleModel.muscles.toList()
                )
                Log.d(TAG, newExercise.toString())
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green700,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(R.string.create_exercises)
                )
            }
        }
        NameTextField(enteredName = enteredText, labelText = "Enter exercise name")
        Divider(color = Grey300)
        MuscleCheckboxes(addMuscles = { muscle -> muscleModel.addMuscle(muscle) },
            removeMuscles = { muscle -> muscleModel.removeMuscle(muscle) },
            modifier = Modifier.padding(horizontal = 8.dp)
            )
        Divider(color = Grey500)
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
