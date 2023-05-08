package com.example.gym

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import com.example.gym.routines.formatElementsInOneLine
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.roundToInt

@Composable
fun DashboardScreen(
    repoModel: DatabaseViewModel,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    val routines = repoModel.retrieveRoutinesFromDB().collectAsState(initial = listOf())
    var clicked by remember {
        mutableStateOf(false)
    }
    var selectedRoutine: Routine? by remember {
        mutableStateOf(null)
    }
    var sessions = repoModel.retrieveMostRecentSessionEntriesFromDB().collectAsState(initial = listOf())
    LazyColumn(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.padding(horizontal = 8.dp).fillMaxWidth()
    ) {
        item {
            Text("Add new results")
            Text("Choose a routine")
        }
        itemsIndexed(routines.value) { _, routine ->
            OutlinedButton(onClick = {
                if (!clicked) {
                    clicked = true
                    selectedRoutine = routine
                } else if (selectedRoutine == routine) {
                    selectedRoutine = null
                    clicked = false
                }
            }) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box {
                        Text(text = routine.name)
                    }
                    Box {
                        Text(text = formatElementsInOneLine(routine.muscleGroups))
                    }
                }
            }
        }
        item {
            if (clicked) {
                selectedRoutine?.let {
                    Column() {
                        var textFields = remember { (List(it.exercises.size) {TextFieldValue("")}).toMutableStateList() }
//                        var textFields = mutableListOf<TextFieldValue>()
//                        remember {
//                            textFields.addAll(List(it.exercises.size) { TextFieldValue("") })
//                        }
                        it.exercises.forEachIndexed { index, exercise ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
//                                var enteredCount = remember { mutableStateOf(TextFieldValue("")) }
                                Text(text = exercise.name, modifier = Modifier.width(196.dp))
                                OutlinedTextField(
                                    value = textFields[index].text,
                                    onValueChange = { currentEntered ->
                                        textFields[index] = TextFieldValue(currentEntered)
                                    },
                                    label = {
                                            Text(text = "Enter rep count for this exercise")
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                )
                            }
                        }
                        ElevatedButton(onClick = {
                            it.exercises.forEachIndexed { index, exercise ->
                                Log.d("Dashboard Screen", "Exercise: ${exercise.name}, Rep count: ${textFields[index].text}")
                            }
                            val repCounts = mutableListOf<Int>()
                            val dateTime = LocalDateTime.now()
                            textFields.forEach { textFieldValue ->
                                if (textFieldValue.text.equals("")) {
                                    repCounts.add(0)
                                } else {
                                    repCounts.add(textFieldValue.text.toDouble().roundToInt())
                                }
                            }
                            repoModel.storeSessionEntryInDB(
                                SessionEntry(it.name, repCounts = repCounts, dateCreated = dateTime)
                            )
                            selectedRoutine = null
                            clicked = false
                        },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                            Text(text = "Submit")
                        }
                    }
                }
            }
        }
        item {
            Text("Last Sessions")
        }
        itemsIndexed(sessions.value) { index, session ->
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box {
                        Text(session.routineName)
                    }
                    Box {
                        Text("${session.dateCreated.month.name.toLowerCase().capitalize()} ${session.dateCreated.dayOfMonth}")
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    session.repCounts.forEach {
                        Box {
                            Text(it.toString())
                        }
                    }
                }
            }
        }
    }

}