package com.example.gym.routines

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.gym.Routine
import com.example.gym.R
import com.example.gym.ui.theme.Green100
import com.example.gym.ui.theme.Green50
import com.example.gym.ui.theme.Green700

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineDetailsScreen(
    routine: Routine,
    modifier: Modifier = Modifier
) {
    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = modifier
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ){
            FilledTonalIconButton(onClick = { /*TODO*/ },
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = Green100,
                contentColor = Green700
            ),
                modifier = Modifier.size(64.dp)
                ) {
                Icon(painter = painterResource(R.drawable.baseline_edit_24),
                    contentDescription = stringResource(R.string.edit_routine),
                )
            }
            FilledTonalIconButton(onClick = { /*TODO*/ },
                modifier = Modifier.size(64.dp)
                ) {
                Icon(painter = painterResource(R.drawable.baseline_delete_24),
                    contentDescription = stringResource(R.string.delete_routine)
                )
            }
        }
        Text(text = "Exercises")
        LazyColumn (
//            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ){
            itemsIndexed(routine.exercises) {_, exercise ->
                Row (
//                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(text = exercise.name)
                    Text(text = formatElementsInOneLine(exercise.muscleGroups))
                }
            }
        }
    }
}