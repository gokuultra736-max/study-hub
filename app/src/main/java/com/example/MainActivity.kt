package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppTab
import com.example.ui.MainViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(viewModel: MainViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val activeChapter by viewModel.activeReadingChapter.collectAsState()
    val activePdf by viewModel.activeReadingPdf.collectAsState()

    // If an NCERT chapter or imported PDF is opened, display the full Reader Screen
    if (activeChapter != null || activePdf != null) {
        BookReaderScreen(
            chapter = activeChapter,
            pdfBook = activePdf,
            onClose = { viewModel.closeReader() },
            viewModel = viewModel
        )
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                tonalElevation = 8.dp,
                modifier = Modifier.testTag("main_bottom_nav")
            ) {
                val navItems = listOf(
                    Triple(AppTab.HOME, Icons.Filled.Home to Icons.Outlined.Home, "Home"),
                    Triple(AppTab.TIMETABLE, Icons.Filled.CalendarMonth to Icons.Outlined.CalendarMonth, "Timetable"),
                    Triple(AppTab.HOMEWORK, Icons.Filled.Assignment to Icons.Outlined.Assignment, "Homework"),
                    Triple(AppTab.BOOKS, Icons.Filled.MenuBook to Icons.Outlined.MenuBook, "NCERT"),
                    Triple(AppTab.FLASHCARDS, Icons.Filled.Style to Icons.Outlined.Style, "Cards"),
                    Triple(AppTab.QUIZ, Icons.Filled.Quiz to Icons.Outlined.Quiz, "Quiz"),
                    Triple(AppTab.PROGRESS, Icons.Filled.BarChart to Icons.Outlined.BarChart, "Progress")
                )

                navItems.forEach { (tab, icons, label) ->
                    val isSelected = currentTab == tab
                    val (selectedIcon, unselectedIcon) = icons
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { viewModel.setTab(tab) },
                        icon = {
                            Icon(
                                if (isSelected) selectedIcon else unselectedIcon,
                                contentDescription = label,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        label = { Text(label, fontSize = 10.sp, maxLines = 1) },
                        alwaysShowLabel = false,
                        modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                AppTab.HOME -> HomeScreen(viewModel = viewModel)
                AppTab.TIMETABLE -> TimetableScreen(viewModel = viewModel)
                AppTab.HOMEWORK -> HomeworkScreen(viewModel = viewModel)
                AppTab.BOOKS -> NcertBooksScreen(viewModel = viewModel)
                AppTab.FLASHCARDS -> FlashcardsScreen(viewModel = viewModel)
                AppTab.QUIZ -> QuizScreen(viewModel = viewModel)
                AppTab.PROGRESS -> ProgressScreen(viewModel = viewModel)
            }
        }
    }
}
