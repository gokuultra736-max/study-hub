package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        HomeworkEntity::class,
        TimetableEntity::class,
        FlashcardEntity::class,
        QuizResultEntity::class,
        OfflineBookEntity::class,
        StudyProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ncert_study_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.appDao())
                    }
                }
            }

            suspend fun populateInitialData(dao: AppDao) {
                // Initial Profile
                dao.updateStudyProfile(
                    StudyProfileEntity(
                        id = 1,
                        selectedGrade = 10,
                        studentName = "Aarav Sharma",
                        dailyGoalMinutes = 120,
                        studyMinutesToday = 65,
                        streakDays = 7,
                        lastActiveDate = "2026-11-12"
                    )
                )

                // Sample Timetable for Monday to Friday
                val sampleClasses = listOf(
                    Triple("08:30 AM - 09:15 AM", "Mathematics", "Room 102 (Mr. Verma)"),
                    Triple("09:20 AM - 10:05 AM", "Science (Physics)", "Physics Lab (Mrs. Sen)"),
                    Triple("10:10 AM - 10:55 AM", "English Language", "Room 102 (Ms. Roy)"),
                    Triple("11:15 AM - 12:00 PM", "Social Science", "Room 204 (Mr. Gupta)"),
                    Triple("12:05 PM - 12:50 PM", "Hindi / Sanskrit", "Room 102 (Dr. Mishra)"),
                    Triple("01:30 PM - 02:15 PM", "Computer / AI", "Computer Lab (Sir Alex)")
                )

                val days = listOf("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY")
                days.forEach { day ->
                    sampleClasses.forEachIndexed { index, (time, subject, teacher) ->
                        val parts = time.split(" - ")
                        dao.insertTimetable(
                            TimetableEntity(
                                dayOfWeek = day,
                                periodIndex = index + 1,
                                subject = subject,
                                startTime = parts[0].trim(),
                                endTime = parts[1].trim(),
                                roomOrTeacher = teacher,
                                notes = "NCERT Chapter revision & problem solving"
                            )
                        )
                    }
                }

                // Sample Homework Tasks
                dao.insertHomework(
                    HomeworkEntity(
                        title = "Solve Exercise 4.2 - Quadratic Equations",
                        subject = "Mathematics",
                        grade = 10,
                        dueDate = "Tomorrow, 8:00 AM",
                        priority = com.example.data.model.Priority.HIGH,
                        isCompleted = false,
                        notes = "Questions 1 to 6 on factorization method from NCERT 2026 book."
                    )
                )
                dao.insertHomework(
                    HomeworkEntity(
                        title = "Draw Ray Diagrams for Concave Mirrors",
                        subject = "Science",
                        grade = 10,
                        dueDate = "Friday, 10:00 AM",
                        priority = com.example.data.model.Priority.MEDIUM,
                        isCompleted = false,
                        notes = "Light - Reflection and Refraction chapter, Section 10.2."
                    )
                )
                dao.insertHomework(
                    HomeworkEntity(
                        title = "Write Summary of 'A Letter to God'",
                        subject = "English",
                        grade = 10,
                        dueDate = "Next Monday",
                        priority = com.example.data.model.Priority.LOW,
                        isCompleted = true,
                        notes = "Include character sketch of Lencho and theme of faith."
                    )
                )

                // Sample Initial Quiz History
                dao.insertQuizResult(
                    QuizResultEntity(
                        title = "Class 10 Science: Chemical Reactions",
                        grade = 10,
                        subject = "Science",
                        score = 4,
                        total = 5,
                        timestamp = System.currentTimeMillis() - 86400000L
                    )
                )
            }
        }
    }
}
