package com.example.gym.routines

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gym.*
import com.example.gym.R
import com.example.gym.ui.theme.Grey300
import com.example.gym.ui.theme.Grey500
import java.util.*

private const val TAG = "Add Routines Scope"

@Composable
fun AddRoutinesScreen(
    context: Context,
    exerciseModel: ExerciseViewModel = viewModel(),
    muscleModel: MuscleViewModel = viewModel(),
    repoModel: DatabaseViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
//    val scrollState = rememberScrollState()
    var enteredName = remember { mutableStateOf(TextFieldValue("")) }
    val enteredInSearch = remember { mutableStateOf(TextFieldValue("")) }
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
//            .verticalScroll(scrollState)
            .padding(8.dp)
    ) {
        NameTextField(
            enteredName = enteredName,
            labelText = "Enter routine name",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Divider(color = Grey300)
        Text(text = "Check the muscles targeted")
        MuscleCheckboxes(
            addMuscles = { muscle -> muscleModel.addMuscle(muscle) },
            removeMuscles = { muscle -> muscleModel.removeMuscle(muscle) }
        )
        Divider(color = Grey500)
        Text(text = "Add Exercises")
        TextField(
            value = enteredInSearch.value,
            onValueChange = { currentEntered -> enteredInSearch.value = currentEntered },
            label = { Text("Search for exercise") },
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.baseline_search_24),
                    contentDescription = null
                )
            },
            singleLine = true,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        SearchResults(
            addExercises = { exercise -> exerciseModel.addExercise(exercise) },
            removeExercises = { exercise -> exerciseModel.removeExercise(exercise) },
            enteredText = enteredInSearch
        )
        Divider(color = Grey300)
        Button(
            onClick = {
                repoModel.storeRoutineInDB(
                    name = enteredName.value.text,
                    exercises = exerciseModel.exercises.toList(),
                    muscleGroups = muscleModel.muscles.toList()
                )
                enteredName.value = TextFieldValue("")
                enteredInSearch.value = TextFieldValue("")
                Toast.makeText(context, "Item Created", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Create Routine")
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            exerciseModel.clearExercises()
            muscleModel.clearMuscles()
        }
    }
}

@Composable
fun SearchResults(
    addExercises: (Exercise) -> Unit,
    removeExercises: (Exercise) -> Unit,
    enteredText: MutableState<TextFieldValue>,
//    items: List<Exercise> = getSampleExercises()
    repoModel: DatabaseViewModel = viewModel()
) {
    val items = repoModel.retrieveExercisesFromDB().collectAsState(initial = listOf())
    var filteredItems: List<Exercise>
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 228.dp)
    ) {
        val searchedText = enteredText.value.text
        val resultList = ArrayList<Exercise>()
        filteredItems =
            if (searchedText.isEmpty()) {
                for (item in items.value) {
                    resultList.add(item)
                }
                resultList
            } else {
                for (item in items.value) {
                    if (item.name.lowercase(Locale.getDefault())
                            .contains(searchedText.lowercase(Locale.getDefault()))
                    ) {
                        resultList.add(item)
                    }
                }
                resultList
            }
        itemsIndexed(filteredItems) { _, filteredItem ->
            SearchResultsItem(
                itemText = filteredItem.name,
                onItemClick = { selected ->
                    if (selected) addExercises(filteredItem)
                    else removeExercises(filteredItem)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsItem(
    itemText: String,
    onItemClick: (Boolean) -> Unit,
) {
    var selected by rememberSaveable { mutableStateOf(false) }
    Card(
        onClick = {
            selected = !selected
            onItemClick(selected)
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Text(itemText)
            if (!selected) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "Add Exercise",
                    tint = MaterialTheme.colorScheme.primary
                )
            } else {
                Icon(
                    painter = painterResource(R.drawable.baseline_remove_circle_24),
                    contentDescription = "Remove Exercise",
                    tint = Grey500
                )
            }
        }
    }
}
