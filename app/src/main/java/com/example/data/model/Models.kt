package com.example.data.model

enum class Priority {
    HIGH, MEDIUM, LOW
}

enum class DayOfWeekEnum(val displayName: String, val shortName: String) {
    MONDAY("Monday", "Mon"),
    TUESDAY("Tuesday", "Tue"),
    WEDNESDAY("Wednesday", "Wed"),
    THURSDAY("Thursday", "Thu"),
    FRIDAY("Friday", "Fri"),
    SATURDAY("Saturday", "Sat")
}

data class NcertBook(
    val id: String,
    val title: String,
    val grade: Int, // 5 to 12
    val subject: String,
    val edition: String = "2026 Revised Edition",
    val description: String,
    val totalChapters: Int,
    val chapters: List<NcertChapter>
)

data class NcertChapter(
    val id: String,
    val bookId: String,
    val chapterNumber: Int,
    val title: String,
    val overview: String,
    val keyPoints: List<String>,
    val importantFormulasOrDefinitions: List<Pair<String, String>>,
    val practiceQuestions: List<String>,
    val fullReadingContent: String,
    val isDownloaded: Boolean = false,
    val localPdfPath: String? = null
)

data class FlashcardItem(
    val id: Long = 0,
    val deckName: String,
    val grade: Int,
    val subject: String,
    val question: String,
    val answer: String,
    val hint: String? = null,
    val isMastered: Boolean = false,
    val isCustom: Boolean = false
)

data class QuizQuestion(
    val id: Int,
    val grade: Int,
    val subject: String,
    val chapter: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class QuizResult(
    val id: Long = 0,
    val title: String,
    val grade: Int,
    val subject: String,
    val score: Int,
    val total: Int,
    val timestamp: Long
)
