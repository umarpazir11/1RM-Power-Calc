package com.rm.powercalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rm.powercalculator.data.AppDatabase
import com.rm.powercalculator.data.HistoryRepositoryImpl
import com.rm.powercalculator.domain.AddHistoryRecordUseCase
import com.rm.powercalculator.domain.CalculateOneRepMaxUseCase
import com.rm.powercalculator.domain.DeleteHistoryRecordUseCase
import com.rm.powercalculator.domain.GetHistoryUseCase
import com.rm.powercalculator.presentation.HistoryScreen
import com.rm.powercalculator.presentation.OneRepMaxEvent
import com.rm.powercalculator.presentation.OneRepMaxScreen
import com.rm.powercalculator.presentation.OneRepMaxViewModel
import com.rm.powercalculator.ui.theme._1RMPowerCalcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _1RMPowerCalcTheme {
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
    }
}

@Composable
fun OneRepMaxScreenContainer(
    onNavigateToHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val historyRepository = HistoryRepositoryImpl(database.historyDao())
    
    val viewModel: OneRepMaxViewModel = viewModel {
        OneRepMaxViewModel(
            calculateOneRepMaxUseCase = CalculateOneRepMaxUseCase(),
            addHistoryRecordUseCase = AddHistoryRecordUseCase(historyRepository),
            getHistoryUseCase = GetHistoryUseCase(historyRepository),
            deleteHistoryRecordUseCase = DeleteHistoryRecordUseCase(historyRepository)
        )
    }
    
    val state by viewModel.state.collectAsState()
    
    OneRepMaxScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onHistoryClick = onNavigateToHistory,
        modifier = modifier
    )
}

@Composable
fun HistoryScreenContainer(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val historyRepository = HistoryRepositoryImpl(database.historyDao())
    
    val viewModel: OneRepMaxViewModel = viewModel {
        OneRepMaxViewModel(
            calculateOneRepMaxUseCase = CalculateOneRepMaxUseCase(),
            addHistoryRecordUseCase = AddHistoryRecordUseCase(historyRepository),
            getHistoryUseCase = GetHistoryUseCase(historyRepository),
            deleteHistoryRecordUseCase = DeleteHistoryRecordUseCase(historyRepository)
        )
    }
    
    val history by viewModel.history.collectAsState()
    val showUndoSnackbar by viewModel.showUndoSnackbar.collectAsState()
    
    HistoryScreen(
        historyRecords = history,
        onNavigateBack = onNavigateBack,
        onDeleteRecord = { record ->
            viewModel.onEvent(OneRepMaxEvent.OnDeleteHistoryRecord(record))
        },
        onUndoDelete = { record ->
            viewModel.onEvent(OneRepMaxEvent.OnUndoDeleteHistoryRecord(record))
        },
        showUndoSnackbar = showUndoSnackbar,
        onHideUndoSnackbar = {
            viewModel.hideUndoSnackbar()
        },
        viewModel = viewModel,
        modifier = modifier
    )
}