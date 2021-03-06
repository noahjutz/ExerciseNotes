package com.noahjutz.exercisenotes.ui

import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.noahjutz.exercisenotes.data.Exercise
import com.noahjutz.exercisenotes.data.ExerciseDao
import com.noahjutz.exercisenotes.util.Ads
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

@Composable
fun EditExercise(
    exercise: Exercise,
    dao: ExerciseDao?,
    navTo: (Screens) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Exercise") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            CoroutineScope(IO).launch {
                                if (exercise.name.isBlank() && exercise.description.isBlank())
                                    dao!!.delete(exercise)
                                else dao!!.insert(exercise)
                            }
                            navTo(Screens.Exercises)
                        },
                        icon = { Icon(Icons.Filled.ArrowBack) }
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            CoroutineScope(IO).launch {
                                dao!!.delete(exercise)
                            }
                            navTo(Screens.Exercises)
                        },
                        icon = { Icon(Icons.Filled.Delete) }
                    )
                }
            )
        },
        bodyContent = {
            val setExerciseName = { name: String ->
                exercise.name = name
            }
            val setExerciseDesc = { desc: String ->
                exercise.description = desc
            }
            EditExerciseContent(exercise, setExerciseName, setExerciseDesc)
        }
    )
}

@Composable
fun EditExerciseContent(
    exercise: Exercise,
    setExerciseName: (String) -> Unit,
    setExerciseDesc: (String) -> Unit
) {
    val name = remember { mutableStateOf(exercise.name) }
    val desc = remember { mutableStateOf(exercise.description) }
    Column {
        ScrollableColumn(modifier = Modifier.padding(start = 16.dp, end = 16.dp).weight(1f)) {
            TextField(
                label = { Text("Name") },
                value = name.value,
                onValueChange = {
                    name.value = it
                    setExerciseName(name.value)
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp, top = 16.dp)
            )
            TextField(
                label = { Text("Description") },
                value = desc.value,
                onValueChange = {
                    desc.value = it
                    setExerciseDesc(desc.value)
                },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
        }
        BannerAd(adId = Ads.editExerciseBannerId)
    }
}

@Composable
@Preview
fun EditExercisePreview() {
    MaterialTheme {
        EditExercise(Exercise("Lunge", ""), null, {})
    }
}
