package com.example.gym.routines

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gym.*
import com.example.gym.R
import com.example.gym.database.DatabaseViewModel
import com.example.gym.navigation.NavViewModel
import com.example.gym.ui.theme.Green100
import com.example.gym.ui.theme.Green700
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "Routine Details Screen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineDetailsScreen(
    detailName: String?,
    repoModel: DatabaseViewModel,
    navController: NavController,
    navModel: NavViewModel,
    firestoreDb: FirebaseFirestore,
    uEmail: String?,
    exerciseModel: ExerciseViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var routine: Routine? by remember {
        mutableStateOf(null)
    }
    var editing by remember {
        mutableStateOf(false)
    }
    var numUpdates by remember {
        mutableStateOf(0)
    }
    var addedExistingExercises by remember {
        mutableStateOf(false)
    }

    val enteredInSearch = remember { mutableStateOf(TextFieldValue("")) }
    LaunchedEffect(key1 = numUpdates) {
        if (detailName != null) {
            routine = repoModel.retrieveRoutineByName(detailName)
            if (!addedExistingExercises) {
                exerciseModel.addAllExercises(routine!!.exercises)
                addedExistingExercises = true
            }
        } else {
            routine = null
        }
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier.padding(start = 8.dp, end = 8.dp, top = 16.dp)
    ) {
//        if (routine != null) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                FilledTonalIconButton(
                    onClick = {
                        if (editing) {
                            updateDb(
                                uEmail = uEmail,
                                firestoreDb = firestoreDb,
                                routine = routine,
                                exercises = routine?.exercises,
                                repoModel = repoModel
                            )

                            numUpdates += 1
                        }
                        editing = !editing
                    },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = Green100,
                        contentColor = Green700
                    ),
                    modifier = Modifier.size(54.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_edit_24),
                        contentDescription = stringResource(R.string.edit_routine),
                    )
                }
                FilledTonalIconButton(
                    onClick = {
                        routine?.let {
                            repoModel.deleteRoutineInDB(it)
                            uEmail?.let { email ->
                                firestoreDb.collection("data").document(email).collection("routine")
                                    .document(it.name)
                                    .delete()
                                    .addOnSuccessListener {
                                        Log.d(
                                            TAG,
                                            "DocumentSnapshot successfully deleted!"
                                        )
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w(
                                            TAG,
                                            "Error deleting document",
                                            e
                                        )
                                    }
                            }
                            navController.navigate(navModel.switchBackToScreen().route)
                        }
                    },
                    modifier = Modifier.size(54.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_delete_24),
                        contentDescription = stringResource(R.string.delete_routine)
                    )
                }
            }
            Text(text = "Exercises")
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                routine?.let {
                    itemsIndexed(exerciseModel.exercises) { _, exercise ->
                        Row(
//                        horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = exercise.name, modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .width(64.dp)
                            )
                            Text(
                                text = formatElementsInOneLine(exercise.muscleGroups),
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            if (editing) {
                                IconButton(
                                    onClick = {
                                        var updatedExercises =
                                            routine!!.exercises as MutableList<Exercise>
                                        updatedExercises.remove(exercise)
                                        updateDb(
                                            uEmail = uEmail,
                                            firestoreDb = firestoreDb,
                                            routine = routine,
                                            exercises = updatedExercises,
                                            repoModel = repoModel
                                        )
                                        exerciseModel.removeExercise(exercise)
                                        numUpdates += 1
                                    },
                                    modifier = Modifier.width(24.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_delete_24),
                                        contentDescription = "Remove exercise"
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.width(24.dp))
                            }
                        }
                    }
                }
                if (editing) {
                    item {
                        SearchComponent(
                            enteredInSearch = enteredInSearch,
                            exerciseModel = exerciseModel,
                            repoModel = repoModel
                        )
                    }
                }
            }
            DisposableEffect(Unit) {
                onDispose {
                    exerciseModel.clearExercises()
                }
            }
//        } else {
//            Text(text = "Unable to Retrieve Routine")
//        }
    }
}

fun updateDb(
    uEmail: String?,
    firestoreDb: FirebaseFirestore,
    routine: Routine?,
    exercises: List<Exercise>?,
    repoModel: DatabaseViewModel
) {
    if (routine != null && exercises != null) {
        repoModel.updateRoutineInDB(
            Routine(
                name = routine.name,
                exercises = exercises,
                muscleGroups = routine.muscleGroups
            )
        )
        uEmail?.let {
            firestoreDb.collection("data").document(it).collection("routine").document(
                routine.name
            )
                .update(
                    hashMapOf(
                        "name" to routine.name,
                        "exercises" to exercises,
                        "muscleGroups" to routine.muscleGroups
                    )
                )
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
        }
    }
}
