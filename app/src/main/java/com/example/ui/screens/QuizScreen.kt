package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.MainViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val grade by viewModel.currentGrade.collectAsState()
    val quizState by viewModel.quizState.collectAsState()
    val quizHistory by viewModel.quizHistory.collectAsState()

    var selectedSubject by remember { mutableStateOf("All") }

    val currentQuestion = quizState.questions.getOrNull(quizState.currentIndex)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 96.dp)
    ) {
        // Quiz Header & Subject selector
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "NCERT 2026 Practice Quizzes",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Class $grade • Multiple-choice chapter assessments",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val subjects = listOf("All", "Science", "Mathematics", "Physics")
                        items(subjects) { s ->
                            FilterChip(
                                selected = (s == selectedSubject),
                                onClick = {
                                    selectedSubject = s
                                    viewModel.startQuiz(if (s == "All") null else s)
                                },
                                label = { Text(s, fontSize = 12.sp) },
                                modifier = Modifier.testTag("quiz_subject_${s.lowercase()}")
                            )
                        }
                    }
                }
            }
        }

        // Active Quiz Container
        if (quizState.isCompleted) {
            // Quiz Results Summary Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Quiz Completed!",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        val percentage = if (quizState.questions.isNotEmpty()) {
                            (quizState.score.toFloat() / quizState.questions.size * 100).toInt()
                        } else 0

                        Text(
                            text = "You scored ${quizState.score} out of ${quizState.questions.size} ($percentage%)",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        val feedback = when {
                            percentage >= 80 -> "Outstanding! You have mastered these NCERT 2026 concepts!"
                            percentage >= 50 -> "Good effort! Review the chapter summary to strengthen fundamentals."
                            else -> "Keep practicing! Use the built-in flashcards for quick revision."
                        }
                        Text(
                            text = feedback,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { viewModel.resetQuiz() },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("retake_quiz_button")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Take Another Quiz")
                        }
                    }
                }
            }
        } else if (currentQuestion != null) {
            // Live Question Player
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer
                            ) {
                                Text(
                                    text = "${currentQuestion.subject} • ${currentQuestion.chapter}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Text(
                                text = "Question ${quizState.currentIndex + 1} of ${quizState.questions.size}",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = currentQuestion.question,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 22.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Options
                        currentQuestion.options.forEachIndexed { optIndex, optionText ->
                            val isSelected = quizState.selectedOptionIndex == optIndex
                            val isSubmitted = quizState.isAnswerSubmitted
                            val isCorrectAnswer = optIndex == currentQuestion.correctIndex

                            val containerColor = when {
                                isSubmitted && isCorrectAnswer -> Color(0xFFD1FAE5) // Soft green
                                isSubmitted && isSelected && !isCorrectAnswer -> Color(0xFFFEE2E2) // Soft red
                                isSelected -> MaterialTheme.colorScheme.primaryContainer
                                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            }

                            val borderColor = when {
                                isSubmitted && isCorrectAnswer -> Color(0xFF10B981)
                                isSubmitted && isSelected && !isCorrectAnswer -> Color(0xFFEF4444)
                                isSelected -> MaterialTheme.colorScheme.primary
                                else -> Color.Transparent
                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable(enabled = !isSubmitted) {
                                        viewModel.selectQuizOption(optIndex)
                                    }
                                    .testTag("quiz_option_$optIndex"),
                                colors = CardDefaults.cardColors(containerColor = containerColor),
                                border = if (borderColor != Color.Transparent) CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(borderColor)) else null
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val optionLabel = ('A'.code + optIndex).toChar().toString()
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = optionLabel,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                            fontSize = 12.sp
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Text(
                                        text = optionText,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                        modifier = Modifier.weight(1f)
                                    )

                                    if (isSubmitted && isCorrectAnswer) {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = "Correct",
                                            tint = Color(0xFF10B981)
                                        )
                                    } else if (isSubmitted && isSelected && !isCorrectAnswer) {
                                        Icon(
                                            Icons.Default.Cancel,
                                            contentDescription = "Incorrect",
                                            tint = Color(0xFFEF4444)
                                        )
                                    }
                                }
                            }
                        }

                        // Explanation card shown after submitting
                        AnimatedVisibility(visible = quizState.isAnswerSubmitted) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "NCERT Explanation:",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = currentQuestion.explanation,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Submit / Next Question action
                        if (!quizState.isAnswerSubmitted) {
                            Button(
                                onClick = { viewModel.submitAnswer() },
                                enabled = quizState.selectedOptionIndex != null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("submit_quiz_answer_button"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Check Answer")
                            }
                        } else {
                            Button(
                                onClick = { viewModel.nextQuestion() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("next_quiz_question_button"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                val isLast = quizState.currentIndex + 1 == quizState.questions.size
                                Text(if (isLast) "See Results" else "Next Question")
                            }
                        }
                    }
                }
            }
        }

        // Quiz History
        if (quizHistory.isNotEmpty()) {
            item {
                Text(
                    text = "Recent Test Scores",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(quizHistory) { result ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = result.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            val dateStr = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(result.timestamp))
                            Text(
                                text = "${result.subject} • $dateStr",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "${result.score} / ${result.total}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
