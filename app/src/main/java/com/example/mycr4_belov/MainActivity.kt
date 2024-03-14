package com.example.mycr4_belov

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mycr4_belov.ui.theme.Mycr4_belovTheme
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mycr4_belovTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

enum class TaskStatus {
    ACTIVE,
    DONE
}

data class Task(val id: Int, val title: String, val status: TaskStatus, val description: String, val date: Date)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(tasks: List<Task>, onItemClick: (Task) -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Список завдань") }
            )
        }


    ) { paddingValues-> LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(tasks) { task ->
                TaskListItem(task = task) { onItemClick(task)}
            }
        }
    }
}

@Composable
fun TaskListItem(task: Task, onItemClick: () -> Unit) {
    val backgroundColor = if (task.status == TaskStatus.ACTIVE) Color.Green else Color.Gray
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(backgroundColor)
            .clickable(onClick = {Log.d("TaskListItem", "Task ${task.id} clicked")
                onItemClick()}),
        contentAlignment = Alignment.Center
    ) {
        Text(text = task.title, color = Color.White, modifier = Modifier.padding(8.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(task: Task, onTaskStatusChange: (TaskStatus) -> Unit, onBackClick: () -> Unit) {
    val arback= Icons.Default.ArrowBack
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = task.title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = arback, contentDescription = "Back")
                    }
                }
            )
        },
        content = {paddingValues->
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ) {
                Text(text = task.description, textAlign = TextAlign.Center)
                Text(text = task.date.toString(), textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp))
                if (task.status == TaskStatus.ACTIVE) {
                    Button(onClick = { onTaskStatusChange(TaskStatus.DONE) }) {
                        Text(text = "Done")
                    }
                }
            }
        }
    )
}

@Composable
fun App() {

    var selectedTask by remember { mutableStateOf<Task?>(null) }

    val tasks = remember {
        listOf(
            Task(1, "Task 1", TaskStatus.ACTIVE, "Task 1 Description", Date()),
            Task(2, "Task 2", TaskStatus.DONE, "Task 2 Description", Date()),
            Task(3, "Task 3", TaskStatus.ACTIVE, "Task 3 Description", Date())
        )
    }

    selectedTask?.let { task ->
        TaskDetailScreen(
            task = task,
            onTaskStatusChange = { newStatus ->
                Log.d("App", "Task ${task.id} status changed to $newStatus")
                selectedTask = task.copy(status = newStatus) },
            onBackClick = { selectedTask = null }
        )
    } ?: TaskListScreen(tasks = tasks) { task ->
        Log.d("App", "Clicked on task: ${task.id}")
        selectedTask = task
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Mycr4_belovTheme {
        App()
    }
}