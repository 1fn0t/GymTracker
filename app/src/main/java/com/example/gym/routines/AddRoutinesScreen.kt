package com.example.gym.routines

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.gym.Exercise
import com.example.gym.MuscleGroup
import com.example.gym.getSampleExercises
import com.example.gym.getSampleMuscles
import java.util.*
import com.example.gym.R


@Composable
fun AddRoutinesScreen(
    modifier: Modifier = Modifier
) {
    var enteredName by rememberSaveable { mutableStateOf("") }
    val enteredInSearch = remember { mutableStateOf(TextFieldValue("")) }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(8.dp)
    ) {
        TextField(value = enteredName,
            onValueChange = { enteredName = it},
            label = { Text("Enter routine name") },
            singleLine = true,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(text = "Check the muscles targeted")
        RoutineMuscleCheckboxes()
        Text(text = "Add Exercises")
        TextField(value = enteredInSearch.value,
            onValueChange = { currentEntered -> enteredInSearch.value = currentEntered },
            label = { Text("Search for exercise") },
            trailingIcon = {
                Icon(painter = painterResource(R.drawable.baseline_search_24),
                    contentDescription = null)
            },
            singleLine = true,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        SearchResults(enteredText = enteredInSearch)
        Button(onClick = { /*TODO*/ },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Create Routine")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineMuscleCheckboxes(
    muscleGroups: List<MuscleGroup> = getSampleMuscles(),
    modifier: Modifier = Modifier
) {
    val colState = rememberScrollState()
    Column (
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .horizontalScroll(colState)
            .fillMaxSize(0.4f)
            ) {
        muscleGroups.map { group ->
            Row (
                horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                Text(group.type.toString(),
                modifier = Modifier
                    .width(160.dp)
                    .align(Alignment.CenterVertically)
                    )
                group.list.map { muscle ->
                    Card {
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                                ){
                            Text(
//                        normalizeStringLength(muscle, 50),
                                muscle,
                                modifier = Modifier.width(200.dp)
                                )
                            Checkbox(checked = false, onCheckedChange = {})
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResults(
    enteredText: MutableState<TextFieldValue>,
    items: List<Exercise> = getSampleExercises()
) {
    var filteredItems: List<String>
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        val searchedText = enteredText.value.text
        val resultList = ArrayList<String>()
        filteredItems =
            if (searchedText.isEmpty()) {
                for (item in items) {
                    resultList.add(item.name)
                }
                resultList
            } else {
                for (item in items) {
                    if (item.name.lowercase(Locale.getDefault())
                            .contains(searchedText.lowercase(Locale.getDefault()))
                    ) {
                        resultList.add(item.name)
                    }
                }
                resultList
            }
        itemsIndexed(filteredItems) { _, filteredItem ->
            SearchResultsItem(
                itemText = filteredItem,
                onItemClick = { /*Click event code needs to be implement*/ }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultsItem(
    itemText: String,
    onItemClick: (String) -> Unit,
) {
    Card(onClick = { onItemClick(itemText) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ){
            Text(itemText)
            Icon(painter = painterResource(R.drawable.ic_add), contentDescription = "Add Exercise"
            )
        }
    }
}