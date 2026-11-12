package com.example.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.*
import com.example.data.model.*
import com.example.data.repository.NcertDataProvider
import com.example.data.repository.StudentRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

enum class AppTab(val title: String) {
    HOME("Home"),
    TIMETABLE("Timetable"),
    HOMEWORK("Homework"),
    BOOKS("NCERT Books"),
    FLASHCARDS("Flashcards"),
    QUIZ("Quizzes"),
    PROGRESS("Progress")
}

data class QuizSessionState(
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val score: Int = 0,
    val isCompleted: Boolean = false,
    val answersHistory: Map<Int, Int> = emptyMap() // questionIndex -> chosenOptionIndex
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    val repository = StudentRepository(database.appDao(), application)

    // Current Active Tab
    private val _currentTab = MutableStateFlow(AppTab.HOME)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    fun setTab(tab: AppTab) {
        _currentTab.value = tab
    }

    // Selected Student Grade (5 to 12)
    val profile: StateFlow<StudyProfileEntity> = repository.getStudyProfile()
        .map { it ?: StudyProfileEntity() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            StudyProfileEntity()
        )

    val currentGrade: StateFlow<Int> = profile
        .map { it.selectedGrade }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 10)

    fun changeGrade(newGrade: Int) {
        viewModelScope.launch {
            repository.updateSelectedGrade(newGrade)
        }
    }

    // Homework State
    val homeworkList: StateFlow<List<HomeworkEntity>> = currentGrade
        .flatMapLatest { grade -> repository.getHomeworkForGrade(grade) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _homeworkFilter = MutableStateFlow("All")
    val homeworkFilter: StateFlow<String> = _homeworkFilter.asStateFlow()

    fun setHomeworkFilter(filter: String) {
        _homeworkFilter.value = filter
    }

    fun addHomework(title: String, subject: String, dueDate: String, priority: Priority, notes: String) {
        viewModelScope.launch {
            repository.addHomework(
                HomeworkEntity(
                    title = title,
                    subject = subject,
                    grade = currentGrade.value,
                    dueDate = dueDate,
                    priority = priority,
                    notes = notes
                )
            )
        }
    }

    fun toggleHomework(homework: HomeworkEntity) {
        viewModelScope.launch {
            repository.toggleHomeworkCompleted(homework)
        }
    }

    fun deleteHomework(homework: HomeworkEntity) {
        viewModelScope.launch {
            repository.deleteHomework(homework)
        }
    }

    // Timetable State
    private val _selectedTimetableDay = MutableStateFlow(DayOfWeekEnum.MONDAY)
    val selectedTimetableDay: StateFlow<DayOfWeekEnum> = _selectedTimetableDay.asStateFlow()

    fun selectTimetableDay(day: DayOfWeekEnum) {
        _selectedTimetableDay.value = day
    }

    val timetableForSelectedDay: StateFlow<List<TimetableEntity>> = _selectedTimetableDay
        .flatMapLatest { day -> repository.getTimetableForDay(day.name) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todayTimetable: StateFlow<List<TimetableEntity>> = flow {
        val calendar = Calendar.getInstance()
        val dayEnum = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> DayOfWeekEnum.MONDAY
            Calendar.TUESDAY -> DayOfWeekEnum.TUESDAY
            Calendar.WEDNESDAY -> DayOfWeekEnum.WEDNESDAY
            Calendar.THURSDAY -> DayOfWeekEnum.THURSDAY
            Calendar.FRIDAY -> DayOfWeekEnum.FRIDAY
            Calendar.SATURDAY -> DayOfWeekEnum.SATURDAY
            else -> DayOfWeekEnum.MONDAY
        }
        emitAll(repository.getTimetableForDay(dayEnum.name))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTimetablePeriod(
        day: DayOfWeekEnum,
        periodIndex: Int,
        subject: String,
        startTime: String,
        endTime: String,
        teacher: String,
        notes: String
    ) {
        viewModelScope.launch {
            repository.addOrUpdateTimetable(
                TimetableEntity(
                    dayOfWeek = day.name,
                    periodIndex = periodIndex,
                    subject = subject,
                    startTime = startTime,
                    endTime = endTime,
                    roomOrTeacher = teacher,
                    notes = notes
                )
            )
        }
    }

    fun deleteTimetablePeriod(entry: TimetableEntity) {
        viewModelScope.launch {
            repository.deleteTimetable(entry)
        }
    }

    // NCERT Books State
    private val _selectedSubjectFilter = MutableStateFlow("All")
    val selectedSubjectFilter: StateFlow<String> = _selectedSubjectFilter.asStateFlow()

    private val _bookSearchQuery = MutableStateFlow("")
    val bookSearchQuery: StateFlow<String> = _bookSearchQuery.asStateFlow()

    fun setSubjectFilter(subject: String) {
        _selectedSubjectFilter.value = subject
    }

    fun setBookSearchQuery(query: String) {
        _bookSearchQuery.value = query
    }

    val offlineImportedBooks: StateFlow<List<OfflineBookEntity>> = currentGrade
        .flatMapLatest { grade -> repository.getOfflineBooks(grade) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Currently Active Reader
    private val _activeReadingChapter = MutableStateFlow<NcertChapter?>(null)
    val activeReadingChapter: StateFlow<NcertChapter?> = _activeReadingChapter.asStateFlow()

    private val _activeReadingPdf = MutableStateFlow<OfflineBookEntity?>(null)
    val activeReadingPdf: StateFlow<OfflineBookEntity?> = _activeReadingPdf.asStateFlow()

    fun openChapter(chapter: NcertChapter) {
        _activeReadingPdf.value = null
        _activeReadingChapter.value = chapter
    }

    fun openOfflinePdf(book: OfflineBookEntity) {
        _activeReadingChapter.value = null
        _activeReadingPdf.value = book
    }

    fun closeReader() {
        _activeReadingChapter.value = null
        _activeReadingPdf.value = null
    }

    fun importBookPdf(uri: Uri, title: String, subject: String, onComplete: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val result = repository.importPdfFromUri(
                uri = uri,
                title = title,
                grade = currentGrade.value,
                subject = subject,
                chapterTitle = "Imported NCERT PDF"
            )
            result.onSuccess {
                onComplete(true, "Successfully imported ${it.title} for offline reading!")
            }.onFailure {
                onComplete(false, "Failed to import file: ${it.localizedMessage}")
            }
        }
    }

    fun deleteImportedBook(book: OfflineBookEntity) {
        viewModelScope.launch {
            repository.deleteOfflineBook(book)
        }
    }

    // Flashcards State
    val customFlashcards: StateFlow<List<FlashcardEntity>> = currentGrade
        .flatMapLatest { grade -> repository.getCustomFlashcards(grade) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _flashcardIndex = MutableStateFlow(0)
    val flashcardIndex: StateFlow<Int> = _flashcardIndex.asStateFlow()

    private val _isCardFlipped = MutableStateFlow(false)
    val isCardFlipped: StateFlow<Boolean> = _isCardFlipped.asStateFlow()

    fun flipCard() {
        _isCardFlipped.value = !_isCardFlipped.value
    }

    fun nextCard(total: Int) {
        _isCardFlipped.value = false
        if (total > 0) {
            _flashcardIndex.value = (_flashcardIndex.value + 1) % total
        }
    }

    fun prevCard(total: Int) {
        _isCardFlipped.value = false
        if (total > 0) {
            _flashcardIndex.value = if (_flashcardIndex.value - 1 < 0) total - 1 else _flashcardIndex.value - 1
        }
    }

    fun addCustomFlashcard(deck: String, subject: String, question: String, answer: String, hint: String?) {
        viewModelScope.launch {
            repository.addCustomFlashcard(
                deckName = deck,
                grade = currentGrade.value,
                subject = subject,
                question = question,
                answer = answer,
                hint = hint
            )
        }
    }

    fun toggleCustomFlashcardMastered(card: FlashcardEntity) {
        viewModelScope.launch {
            repository.toggleFlashcardMastered(card)
        }
    }

    fun deleteCustomFlashcard(card: FlashcardEntity) {
        viewModelScope.launch {
            repository.deleteFlashcard(card)
        }
    }

    // Quizzes State
    private val _quizState = MutableStateFlow(QuizSessionState())
    val quizState: StateFlow<QuizSessionState> = _quizState.asStateFlow()

    val quizHistory: StateFlow<List<QuizResultEntity>> = currentGrade
        .flatMapLatest { grade -> repository.getQuizHistory(grade) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun startQuiz(subject: String? = null) {
        val questions = NcertDataProvider.getQuizzesForGrade(currentGrade.value, subject)
        _quizState.value = QuizSessionState(
            questions = questions.ifEmpty {
                // Fallback to all available questions for grade or general questions
                NcertDataProvider.getQuizzesForGrade(10)
            },
            currentIndex = 0,
            selectedOptionIndex = null,
            isAnswerSubmitted = false,
            score = 0,
            isCompleted = false,
            answersHistory = emptyMap()
        )
    }

    fun selectQuizOption(index: Int) {
        if (!_quizState.value.isAnswerSubmitted) {
            _quizState.value = _quizState.value.copy(selectedOptionIndex = index)
        }
    }

    fun submitAnswer() {
        val state = _quizState.value
        val currentQ = state.questions.getOrNull(state.currentIndex) ?: return
        val chosen = state.selectedOptionIndex ?: return

        val isCorrect = chosen == currentQ.correctIndex
        val newScore = if (isCorrect) state.score + 1 else state.score
        val newHistory = state.answersHistory + (state.currentIndex to chosen)

        _quizState.value = state.copy(
            isAnswerSubmitted = true,
            score = newScore,
            answersHistory = newHistory
        )
    }

    fun nextQuestion() {
        val state = _quizState.value
        if (state.currentIndex + 1 < state.questions.size) {
            _quizState.value = state.copy(
                currentIndex = state.currentIndex + 1,
                selectedOptionIndex = null,
                isAnswerSubmitted = false
            )
        } else {
            // Finish Quiz
            _quizState.value = state.copy(isCompleted = true)
            viewModelScope.launch {
                repository.saveQuizResult(
                    title = "NCERT 2026 Class ${currentGrade.value} Practice Test",
                    grade = currentGrade.value,
                    subject = state.questions.firstOrNull()?.subject ?: "General",
                    score = state.score,
                    total = state.questions.size
                )
            }
        }
    }

    fun resetQuiz() {
        startQuiz()
    }

    // Study minutes helper
    fun addQuickStudySession(minutes: Int) {
        viewModelScope.launch {
            repository.addStudyMinutes(minutes)
        }
    }
}
