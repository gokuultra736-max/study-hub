package com.example

import com.example.data.repository.NcertDataProvider
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testNcertGradesSupported() {
        val grades = NcertDataProvider.supportedGrades
        assertEquals(8, grades.size)
        assertTrue(grades.contains(5))
        assertTrue(grades.contains(10))
        assertTrue(grades.contains(12))
    }

    @Test
    fun testBooksAvailableForGrades() {
        val class10Books = NcertDataProvider.getBooksForGrade(10)
        assertTrue(class10Books.isNotEmpty())
        val mathBook = class10Books.find { it.subject == "Mathematics" }
        assertNotNull(mathBook)
        assertEquals(10, mathBook?.grade)

        val class6Books = NcertDataProvider.getBooksForGrade(6)
        assertTrue(class6Books.isNotEmpty())

        val class12Books = NcertDataProvider.getBooksForGrade(12)
        assertTrue(class12Books.isNotEmpty())
    }

    @Test
    fun testQuizzesAndFlashcards() {
        val quizzes = NcertDataProvider.getQuizzesForGrade(10)
        assertTrue(quizzes.isNotEmpty())
        val firstQ = quizzes[0]
        assertEquals(4, firstQ.options.size)
        assertTrue(firstQ.correctIndex in 0..3)

        val flashcards = NcertDataProvider.getPreloadedFlashcards(10)
        assertTrue(flashcards.isNotEmpty())
    }
}
