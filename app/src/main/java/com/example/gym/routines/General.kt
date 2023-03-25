package com.example.gym.routines

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.gym.MuscleGroup
import com.example.gym.getSampleMuscles


@Composable
fun NameTextField(
    enteredName: MutableState<TextFieldValue>,
    labelText: String,
    modifier: Modifier = Modifier
) {
    TextField(value = enteredName.value,
        onValueChange = { currentEntered -> enteredName.value = currentEntered },
        label = { Text(labelText) },
        singleLine = true,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuscleCheckboxes(
    addMuscles: (String) -> Unit,
    removeMuscles: (String) -> Unit,
    modifier: Modifier = Modifier,
    muscleGroups: List<MuscleGroup> = getSampleMuscles(),
) {
    val colState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .horizontalScroll(colState)
            .height(264.dp)
    ) {
        muscleGroups.map { group ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    group.type.toString().lowercase().capitalize(),
                    modifier = Modifier
                        .width(104.dp)
                        .align(Alignment.CenterVertically)
                )
                group.list.map { muscle ->
                    var checked by rememberSaveable { mutableStateOf(false) }
                    Card {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text(
                                muscle,
                                modifier = Modifier.width(120.dp)
                            )
                            Checkbox(checked = checked, onCheckedChange = { current ->
                                checked = current
                                if (current) {
                                    addMuscles(muscle)
                                } else {
                                    removeMuscles(muscle)
                                }
                            },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.tertiary,
                                    uncheckedColor = Color.Black,
                                    checkmarkColor = Color.Yellow
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
