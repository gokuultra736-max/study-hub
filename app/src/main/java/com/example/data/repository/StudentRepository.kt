package com.example.data.repository

import android.content.Context
import android.net.Uri
import com.example.data.local.*
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class StudentRepository(
    private val appDao: AppDao,
    private val context: Context
) {
    // Homework
    fun getHomeworkForGrade(grade: Int): Flow<List<HomeworkEntity>> =
        appDao.getHomeworkByGrade(grade)

    suspend fun addHomework(homework: HomeworkEntity) = withContext(Dispatchers.IO) {
        appDao.insertHomework(homework)
    }

    suspend fun toggleHomeworkCompleted(homework: HomeworkEntity) = withContext(Dispatchers.IO) {
        appDao.updateHomework(homework.copy(isCompleted = !homework.isCompleted))
    }

    suspend fun deleteHomework(homework: HomeworkEntity) = withContext(Dispatchers.IO) {
        appDao.deleteHomework(homework)
    }

    // Timetable
    fun getTimetableForDay(day: String): Flow<List<TimetableEntity>> =
        appDao.getTimetableForDay(day)

    fun getAllTimetable(): Flow<List<TimetableEntity>> =
        appDao.getAllTimetable()

    suspend fun addOrUpdateTimetable(entry: TimetableEntity) = withContext(Dispatchers.IO) {
        if (entry.id == 0L) {
            appDao.insertTimetable(entry)
        } else {
            appDao.updateTimetable(entry)
        }
    }

    suspend fun deleteTimetable(entry: TimetableEntity) = withContext(Dispatchers.IO) {
        appDao.deleteTimetable(entry)
    }

    // Flashcards
    fun getCustomFlashcards(grade: Int): Flow<List<FlashcardEntity>> =
        appDao.getCustomFlashcards(grade)

    suspend fun addCustomFlashcard(
        deckName: String,
        grade: Int,
        subject: String,
        question: String,
        answer: String,
        hint: String?
    ) = withContext(Dispatchers.IO) {
        appDao.insertFlashcard(
            FlashcardEntity(
                deckName = deckName,
                grade = grade,
                subject = subject,
                question = question,
                answer = answer,
                hint = hint,
                isMastered = false,
                isCustom = true
            )
        )
    }

    suspend fun toggleFlashcardMastered(entity: FlashcardEntity) = withContext(Dispatchers.IO) {
        appDao.updateFlashcard(entity.copy(isMastered = !entity.isMastered))
    }

    suspend fun deleteFlashcard(entity: FlashcardEntity) = withContext(Dispatchers.IO) {
        appDao.deleteFlashcard(entity)
    }

    // Quizzes & Results
    fun getQuizHistory(grade: Int): Flow<List<QuizResultEntity>> =
        appDao.getQuizResultsByGrade(grade)

    suspend fun saveQuizResult(
        title: String,
        grade: Int,
        subject: String,
        score: Int,
        total: Int
    ) = withContext(Dispatchers.IO) {
        appDao.insertQuizResult(
            QuizResultEntity(
                title = title,
                grade = grade,
                subject = subject,
                score = score,
                total = total,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    // Offline & Imported NCERT Books
    fun getOfflineBooks(grade: Int): Flow<List<OfflineBookEntity>> =
        appDao.getOfflineBooksByGrade(grade)

    suspend fun importPdfFromUri(
        uri: Uri,
        title: String,
        grade: Int,
        subject: String,
        chapterTitle: String
    ): Result<OfflineBookEntity> = withContext(Dispatchers.IO) {
        try {
            val booksDir = File(context.filesDir, "imported_ncert_books")
            if (!booksDir.exists()) booksDir.mkdirs()

            val sanitizedTitle = title.replace(Regex("[^a-zA-Z0-9._-]"), "_")
            val targetFile = File(booksDir, "${System.currentTimeMillis()}_$sanitizedTitle.pdf")

            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                FileOutputStream(targetFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            } ?: return@withContext Result.failure(Exception("Could not open file stream"))

            val entity = OfflineBookEntity(
                bookId = "imported_${System.currentTimeMillis()}",
                title = title,
                grade = grade,
                subject = subject,
                chapterNumber = 1,
                chapterTitle = chapterTitle,
                localFilePath = targetFile.absolutePath,
                fileSizeBytes = targetFile.length(),
                isUserImported = true
            )
            val id = appDao.insertOfflineBook(entity)
            Result.success(entity.copy(id = id))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteOfflineBook(book: OfflineBookEntity) = withContext(Dispatchers.IO) {
        try {
            val file = File(book.localFilePath)
            if (file.exists()) file.delete()
        } catch (_: Exception) {}
        appDao.deleteOfflineBook(book)
    }

    // Profile & Settings
    fun getStudyProfile(): Flow<StudyProfileEntity?> = appDao.getStudyProfile()

    suspend fun updateSelectedGrade(newGrade: Int) = withContext(Dispatchers.IO) {
        val current = appDao.getStudyProfile().firstOrNull() ?: StudyProfileEntity()
        appDao.updateStudyProfile(current.copy(selectedGrade = newGrade))
    }

    suspend fun addStudyMinutes(minutes: Int) = withContext(Dispatchers.IO) {
        val current = appDao.getStudyProfile().firstOrNull() ?: StudyProfileEntity()
        appDao.updateStudyProfile(
            current.copy(
                studyMinutesToday = current.studyMinutesToday + minutes
            )
        )
    }
}
