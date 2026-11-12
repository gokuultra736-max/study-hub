package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Homework
    @Query("SELECT * FROM homework WHERE grade = :grade ORDER BY isCompleted ASC, dueDate ASC")
    fun getHomeworkByGrade(grade: Int): Flow<List<HomeworkEntity>>

    @Query("SELECT * FROM homework ORDER BY isCompleted ASC, dueDate ASC")
    fun getAllHomework(): Flow<List<HomeworkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHomework(homework: HomeworkEntity): Long

    @Update
    suspend fun updateHomework(homework: HomeworkEntity)

    @Delete
    suspend fun deleteHomework(homework: HomeworkEntity)

    // Timetable
    @Query("SELECT * FROM timetable WHERE dayOfWeek = :day ORDER BY periodIndex ASC")
    fun getTimetableForDay(day: String): Flow<List<TimetableEntity>>

    @Query("SELECT * FROM timetable ORDER BY dayOfWeek, periodIndex ASC")
    fun getAllTimetable(): Flow<List<TimetableEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimetable(entry: TimetableEntity): Long

    @Update
    suspend fun updateTimetable(entry: TimetableEntity)

    @Delete
    suspend fun deleteTimetable(entry: TimetableEntity)

    // Custom Flashcards
    @Query("SELECT * FROM flashcards WHERE grade = :grade ORDER BY id DESC")
    fun getCustomFlashcards(grade: Int): Flow<List<FlashcardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcard(flashcard: FlashcardEntity): Long

    @Update
    suspend fun updateFlashcard(flashcard: FlashcardEntity)

    @Delete
    suspend fun deleteFlashcard(flashcard: FlashcardEntity)

    // Quiz Results
    @Query("SELECT * FROM quiz_results WHERE grade = :grade ORDER BY timestamp DESC")
    fun getQuizResultsByGrade(grade: Int): Flow<List<QuizResultEntity>>

    @Query("SELECT * FROM quiz_results ORDER BY timestamp DESC")
    fun getAllQuizResults(): Flow<List<QuizResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(result: QuizResultEntity): Long

    // Offline / Imported Books
    @Query("SELECT * FROM offline_books ORDER BY addedTimestamp DESC")
    fun getAllOfflineBooks(): Flow<List<OfflineBookEntity>>

    @Query("SELECT * FROM offline_books WHERE grade = :grade ORDER BY addedTimestamp DESC")
    fun getOfflineBooksByGrade(grade: Int): Flow<List<OfflineBookEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOfflineBook(book: OfflineBookEntity): Long

    @Delete
    suspend fun deleteOfflineBook(book: OfflineBookEntity)

    // Profile & Streak
    @Query("SELECT * FROM study_profile WHERE id = 1 LIMIT 1")
    fun getStudyProfile(): Flow<StudyProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateStudyProfile(profile: StudyProfileEntity)
}
