package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.Priority

@Entity(tableName = "homework")
data class HomeworkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val subject: String,
    val grade: Int,
    val dueDate: String,
    val priority: Priority,
    val isCompleted: Boolean = false,
    val notes: String = ""
)

@Entity(tableName = "timetable")
data class TimetableEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dayOfWeek: String, // MONDAY..SATURDAY
    val periodIndex: Int,
    val subject: String,
    val startTime: String,
    val endTime: String,
    val roomOrTeacher: String,
    val notes: String = ""
)

@Entity(tableName = "flashcards")
data class FlashcardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val deckName: String,
    val grade: Int,
    val subject: String,
    val question: String,
    val answer: String,
    val hint: String? = null,
    val isMastered: Boolean = false,
    val isCustom: Boolean = true
)

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val grade: Int,
    val subject: String,
    val score: Int,
    val total: Int,
    val timestamp: Long
)

@Entity(tableName = "offline_books")
data class OfflineBookEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val bookId: String,
    val title: String,
    val grade: Int,
    val subject: String,
    val chapterNumber: Int,
    val chapterTitle: String,
    val localFilePath: String, // Absolute path or cache URI
    val fileSizeBytes: Long = 0,
    val isUserImported: Boolean = false,
    val addedTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "study_profile")
data class StudyProfileEntity(
    @PrimaryKey
    val id: Int = 1,
    val selectedGrade: Int = 10,
    val studentName: String = "Student",
    val dailyGoalMinutes: Int = 120,
    val studyMinutesToday: Int = 45,
    val streakDays: Int = 5,
    val lastActiveDate: String = ""
)
