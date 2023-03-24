package com.example.gym.routines

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.gym.Routine
import com.example.gym.R

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
            IconButton(onClick = { /*TODO*/ }) {
                Icon(painter = painterResource(R.drawable.baseline_edit_24),
                    contentDescription = stringResource(R.string.edit_routine)
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
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