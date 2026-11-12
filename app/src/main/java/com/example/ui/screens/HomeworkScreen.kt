package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.HomeworkEntity
import com.example.data.model.Priority
import com.example.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeworkScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val homeworkList by viewModel.homeworkList.collectAsState()
    val currentFilter by viewModel.homeworkFilter.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredList = remember(homeworkList, currentFilter) {
        when (currentFilter) {
            "Pending" -> homeworkList.filter { !it.isCompleted }
            "Completed" -> homeworkList.filter { it.isCompleted }
            "High Priority" -> homeworkList.filter { it.priority == Priority.HIGH }
            else -> homeworkList
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("add_homework_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Homework")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Filter Chips Row
            Surface(
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filterOptions = listOf("All", "Pending", "Completed", "High Priority")
                    items(filterOptions) { filter ->
                        val isSelected = filter == currentFilter
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setHomeworkFilter(filter) },
                            label = { Text(filter) },
                            leadingIcon = if (isSelected) {
                                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null,
                            modifier = Modifier.testTag("homework_filter_${filter.lowercase().replace(" ", "_")}")
                        )
                    }
                }
            }

            // Summary counters
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val pendingCount = homeworkList.count { !it.isCompleted }
                val completedCount = homeworkList.count { it.isCompleted }
                Text(
                    text = "$pendingCount Pending • $completedCount Done",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Total: ${homeworkList.size} tasks",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Homework Tasks List
            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.AssignmentTurnedIn,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No homework in this view!",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Add tasks to keep track of your assignments and submissions.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(top = 4.dp, bottom = 96.dp)
                ) {
                    items(filteredList, key = { it.id }) { item ->
                        HomeworkItemCard(
                            item = item,
                            onToggle = { viewModel.toggleHomework(item) },
                            onDelete = { viewModel.deleteHomework(item) }
                        )
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddHomeworkDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, subject, dueDate, priority, notes ->
                viewModel.addHomework(title, subject, dueDate, priority, notes)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun HomeworkItemCard(
    item: HomeworkEntity,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("homework_card_${item.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isCompleted)
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (item.isCompleted) 0.dp else 2.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = item.isCompleted,
                    onCheckedChange = { onToggle() },
                    modifier = Modifier.testTag("homework_checkbox_${item.id}")
                )
                Spacer(modifier = Modifier.width(6.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (item.isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = item.subject,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = "Due: ${item.dueDate}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Priority Badge
                val (priorityColor, priorityBg) = when (item.priority) {
                    Priority.HIGH -> Color(0xFFEF4444) to Color(0xFFFEE2E2)
                    Priority.MEDIUM -> Color(0xFFF59E0B) to Color(0xFFFEF3C7)
                    Priority.LOW -> Color(0xFF10B981) to Color(0xFFD1FAE5)
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = priorityBg,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Text(
                        text = item.priority.name,
                        color = priorityColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(32.dp).testTag("delete_homework_${item.id}")
                ) {
                    Icon(
                        Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            if (item.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = item.notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 44.dp)
                )
            }
        }
    }
}

@Composable
fun AddHomeworkDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, subject: String, dueDate: String, priority: Priority, notes: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("Mathematics") }
    var dueDate by remember { mutableStateOf("Tomorrow, 5:00 PM") }
    var priority by remember { mutableStateOf(Priority.MEDIUM) }
    var notes by remember { mutableStateOf("") }

    val subjects = listOf("Mathematics", "Science", "Social Science", "English", "Hindi", "Physics", "Chemistry", "Biology")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Homework Assignment") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Task Description") },
                    placeholder = { Text("e.g. Solve Exercise 3.2 Q1-5") },
                    modifier = Modifier.fillMaxWidth().testTag("homework_title_input")
                )

                Text("Subject:", style = MaterialTheme.typography.labelMedium)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(subjects) { s ->
                        FilterChip(
                            selected = (subject == s),
                            onClick = { subject = s },
                            label = { Text(s, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Due Date & Time") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("homework_due_input")
                )

                Text("Priority:", style = MaterialTheme.typography.labelMedium)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Priority.values().forEach { p ->
                        FilterChip(
                            selected = (priority == p),
                            onClick = { priority = p },
                            label = { Text(p.name, fontSize = 11.sp) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Chapter Reference / Notes (Optional)") },
                    modifier = Modifier.fillMaxWidth().testTag("homework_notes_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onConfirm(title, subject, dueDate, priority, notes)
                    }
                },
                enabled = title.isNotBlank(),
                modifier = Modifier.testTag("save_homework_button")
            ) {
                Text("Add Task")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
