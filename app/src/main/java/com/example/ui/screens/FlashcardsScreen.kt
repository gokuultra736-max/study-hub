package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FlashcardItem
import com.example.data.repository.NcertDataProvider
import com.example.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlashcardsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val grade by viewModel.currentGrade.collectAsState()
    val customCards by viewModel.customFlashcards.collectAsState()
    val currentIndex by viewModel.flashcardIndex.collectAsState()
    val isFlipped by viewModel.isCardFlipped.collectAsState()

    var selectedSubject by remember { mutableStateOf("All") }
    var showAddDialog by remember { mutableStateOf(false) }

    // Merge preloaded NCERT 2026 flashcards + user's custom flashcards
    val preloadedCards = remember(grade, selectedSubject) {
        NcertDataProvider.getPreloadedFlashcards(grade, selectedSubject)
    }

    val customCardItems = remember(customCards, selectedSubject) {
        customCards
            .filter { selectedSubject == "All" || it.subject.equals(selectedSubject, ignoreCase = true) }
            .map {
                FlashcardItem(
                    id = it.id,
                    deckName = it.deckName,
                    grade = it.grade,
                    subject = it.subject,
                    question = it.question,
                    answer = it.answer,
                    hint = it.hint,
                    isMastered = it.isMastered,
                    isCustom = true
                )
            }
    }

    val allCards = remember(preloadedCards, customCardItems) {
        preloadedCards + customCardItems
    }

    val safeIndex = if (allCards.isNotEmpty()) currentIndex.coerceIn(0, allCards.size - 1) else 0
    val currentCard = allCards.getOrNull(safeIndex)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.testTag("add_flashcard_fab")
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Flashcard")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Class and Subject Filter Bar
            Surface(
                tonalElevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                LazyRow(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val subjects = listOf("All", "Science", "Mathematics", "Physics", "Chemistry")
                    items(subjects) { s ->
                        FilterChip(
                            selected = (s == selectedSubject),
                            onClick = { selectedSubject = s },
                            label = { Text(s, fontSize = 12.sp) },
                            modifier = Modifier.testTag("flashcard_subject_${s.lowercase()}")
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (allCards.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Style,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No flashcards found for Class $grade ($selectedSubject)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap the + button to create a custom study flashcard!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else if (currentCard != null) {
                // Card Counter & Deck Tag
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = currentCard.subject,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = "Card ${safeIndex + 1} of ${allCards.size}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (currentCard.isCustom) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = "My Card",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Flippable 3D Flashcard
                val rotation by animateFloatAsState(
                    targetValue = if (isFlipped) 180f else 0f,
                    animationSpec = tween(durationMillis = 400),
                    label = "flipAnimation"
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .graphicsLayer {
                            rotationY = rotation
                            cameraDistance = 12f * density
                        }
                        .clickable { viewModel.flipCard() }
                        .testTag("flashcard_flip_card"),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isFlipped)
                            MaterialTheme.colorScheme.secondaryContainer
                        else
                            MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (rotation <= 90f) {
                            // Front View (Question)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "QUESTION",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = currentCard.question,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.TouchApp,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Tap to reveal answer",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        } else {
                            // Back View (Answer) - Rotate text back so it is readable
                            Column(
                                modifier = Modifier.graphicsLayer { rotationY = 180f },
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "ANSWER & EXPLANATION",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = currentCard.answer,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                if (!currentCard.hint.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                    ) {
                                        Text(
                                            text = "💡 Hint: ${currentCard.hint}",
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Navigation Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FilledTonalButton(
                        onClick = { viewModel.prevCard(allCards.size) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("flashcard_prev_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Previous")
                    }

                    FilledTonalButton(
                        onClick = { viewModel.flipCard() },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Flip, contentDescription = "Flip")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Flip")
                    }

                    Button(
                        onClick = { viewModel.nextCard(allCards.size) },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("flashcard_next_button")
                    ) {
                        Text("Next")
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
                    }
                }

                Spacer(modifier = Modifier.height(96.dp))
            }
        }
    }

    if (showAddDialog) {
        AddFlashcardDialog(
            grade = grade,
            onDismiss = { showAddDialog = false },
            onConfirm = { deck, subject, question, answer, hint ->
                viewModel.addCustomFlashcard(deck, subject, question, answer, hint)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AddFlashcardDialog(
    grade: Int,
    onDismiss: () -> Unit,
    onConfirm: (deck: String, subject: String, question: String, answer: String, hint: String?) -> Unit
) {
    var deck by remember { mutableStateOf("Class $grade Revision") }
    var subject by remember { mutableStateOf("Science") }
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    var hint by remember { mutableStateOf("") }

    val subjects = listOf("Science", "Mathematics", "Social Science", "English", "Physics", "Chemistry", "Biology")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Create Custom Flashcard") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
                    value = question,
                    onValueChange = { question = it },
                    label = { Text("Question / Term (Front)") },
                    placeholder = { Text("e.g. What is the value of G in gravitation?") },
                    modifier = Modifier.fillMaxWidth().testTag("flashcard_question_input")
                )

                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it },
                    label = { Text("Answer / Definition (Back)") },
                    placeholder = { Text("e.g. 6.674 × 10⁻¹¹ N·m²/kg²") },
                    modifier = Modifier.fillMaxWidth().testTag("flashcard_answer_input")
                )

                OutlinedTextField(
                    value = hint,
                    onValueChange = { hint = it },
                    label = { Text("Hint (Optional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("flashcard_hint_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (question.isNotBlank() && answer.isNotBlank()) {
                        onConfirm(deck, subject, question, answer, hint.ifBlank { null })
                    }
                },
                enabled = question.isNotBlank() && answer.isNotBlank(),
                modifier = Modifier.testTag("save_flashcard_button")
            ) {
                Text("Save Card")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
