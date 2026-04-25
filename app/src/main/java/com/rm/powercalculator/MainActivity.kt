package com.rm.powercalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rm.powercalculator.presentation.HistoryScreen
import com.rm.powercalculator.presentation.OneRepMaxScreen
import com.rm.powercalculator.presentation.screens.AboutScreen
import com.rm.powercalculator.presentation.screens.PrivacyPolicyScreen
import com.rm.powercalculator.presentation.screens.OneRepMaxEvent
import com.rm.powercalculator.presentation.ui.theme.RMTheme
import com.rm.powercalculator.presentation.viewmodel.OneRepMaxViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RMTheme {
                AppNavigation(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "calculator",
        modifier = modifier
    ) {
        composable("calculator") {
            OneRepMaxScreenContainer(
                onNavigateToHistory = {
                    navController.navigate("history")
                },
                onNavigateToAbout = {
                    navController.navigate("about")
                }
            )
        }
        composable("history") {
            HistoryScreenContainer(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("about") {
            AboutScreen(
                onNavigateToPrivacyPolicy = {
                    navController.navigate("privacy_policy")
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable("privacy_policy") {
            PrivacyPolicyScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun OneRepMaxScreenContainer(
    onNavigateToHistory: () -> Unit,
    onNavigateToAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: OneRepMaxViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    OneRepMaxScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onHistoryClick = onNavigateToHistory,
        onAboutClick = onNavigateToAbout,
        modifier = modifier
    )
}

@Composable
fun HistoryScreenContainer(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: OneRepMaxViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()

    // TODO: The undo logic still needs to be implemented in the OneRepMaxViewModel.
    HistoryScreen(
        historyRecords = state.history,
        onNavigateBack = onNavigateBack,
        onDeleteRecord = { record -> viewModel.onEvent(OneRepMaxEvent.DeleteHistory(record)) },
        onUndoDelete = { /* record -> viewModel.onEvent(OneRepMaxEvent.OnUndoDeleteHistoryRecord(record)) */ },
        showUndoSnackbar = null,
        onHideUndoSnackbar = { /* viewModel.hideUndoSnackbar() */ },
        viewModel = viewModel,
        modifier = modifier
    )
}
