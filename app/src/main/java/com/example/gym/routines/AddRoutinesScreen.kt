package com.example.gym.routines

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gym.*
import com.example.gym.R
import com.example.gym.database.DatabaseViewModel
import com.example.gym.ui.theme.Grey300
import com.example.gym.ui.theme.Grey500
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

private const val TAG = "Add Routines Scope"

@Composable
fun AddRoutinesScreen(
    context: Context,
    exerciseModel: ExerciseViewModel = viewModel(),
    muscleModel: MuscleViewModel = viewModel(),
    firestoreDb: FirebaseFirestore,
    uEmail: String?,
    repoModel: DatabaseViewModel,
    modifier: Modifier = Modifier
) {
    var enteredName = remember { mutableStateOf(TextFieldValue("")) }
    val enteredInSearch = remember { mutableStateOf(TextFieldValue("")) }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .padding(8.dp)
    ) {
        item {
            Box (
                modifier = Modifier.fillMaxWidth()
                    ){
                NameTextField(
                    enteredName = enteredName,
                    labelText = "Enter routine name",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        item {
            Divider(color = Grey300)
            Text(text = "Check the muscles targeted")
        }
        item {
            MuscleCheckboxes(
                addMuscles = { muscle -> muscleModel.addMuscle(muscle) },
                removeMuscles = { muscle -> muscleModel.removeMuscle(muscle) }
            )
        }
        item {
            Divider(color = Grey500)
            SearchComponent(enteredInSearch = enteredInSearch, exerciseModel = exerciseModel, repoModel = repoModel)
            Divider(color = Grey300)
        }
        item {
            Box (
                modifier = Modifier.fillMaxWidth()
                    ){
                Button(
                    onClick = {
                        repoModel.storeRoutineInDB(
                            name = enteredName.value.text,
                            exercises = exerciseModel.exercises.toList(),
                            muscleGroups = muscleModel.muscles.toList()
                        )
                        uEmail?.let {
                            firestoreDb.collection("data").document(it).collection("routine").document(enteredName.value.text)
                                .set(
                                    hashMapOf(
                                        "name" to enteredName.value.text,
                                        "exercises" to exerciseModel.exercises.toList(),
                                        "muscleGroups" to muscleModel.muscles.toList()
                                    )
                                )
                        }
                        enteredName.value = TextFieldValue("")
                        enteredInSearch.value = TextFieldValue("")
                        Toast.makeText(context, "Item Created", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Text(text = "Create Routine", style = MaterialTheme.typography.labelMedium)
                }
            }
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
fun SearchComponent(enteredInSearch: MutableState<TextFieldValue>, exerciseModel: ExerciseViewModel, repoModel: DatabaseViewModel) {
    Column {
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
            enteredText = enteredInSearch,
            repoModel = repoModel,
            exerciseModel = exerciseModel,
        )
    }

}

@Composable
fun SearchResults(
    addExercises: (Exercise) -> Unit,
    removeExercises: (Exercise) -> Unit,
    enteredText: MutableState<TextFieldValue>,
    repoModel: DatabaseViewModel,
    exerciseModel: ExerciseViewModel
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
//            var initialState by remember { mutableStateOf(false) }
//            LaunchedEffect(key1 = Unit) {
//                if (exerciseModel.exercises.contains(filteredItem)) {
//                    initialState = true
//                }
//            }
            SearchResultsItem(
                itemText = filteredItem.name,
//                itemInitialState = initialState,
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
//    itemInitialState: Boolean = false,
    onItemClick: (Boolean) -> Unit,
) {
//    var selected by rememberSaveable { mutableStateOf(itemInitialState) }
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
