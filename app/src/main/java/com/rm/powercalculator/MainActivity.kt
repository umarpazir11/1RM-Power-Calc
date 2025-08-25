package com.rm.powercalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rm.powercalculator.presentation.OneRepMaxScreen
import com.rm.powercalculator.presentation.OneRepMaxViewModel
import com.rm.powercalculator.ui.theme._1RMPowerCalcTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _1RMPowerCalcTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    OneRepMaxScreenContainer(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun OneRepMaxScreenContainer(
    modifier: Modifier = Modifier,
    viewModel: OneRepMaxViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    OneRepMaxScreen(
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}