package com.rm.powercalculator.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun OneRepMaxScreen(
    state: OneRepMaxState,
    onEvent: (OneRepMaxEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "1RM Power Calculator",
            style = MaterialTheme.typography.headlineMedium
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = state.weightInput,
            onValueChange = { onEvent(OneRepMaxEvent.OnWeightChange(it)) },
            label = { Text("Weight (kg)") },
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )
        
        OutlinedTextField(
            value = state.repsInput,
            onValueChange = { onEvent(OneRepMaxEvent.OnRepsChange(it)) },
            label = { Text("Reps") },
            keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )
        
        Button(
            onClick = { onEvent(OneRepMaxEvent.OnCalculateClick) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text("CALCULATE")
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        if (state.estimatedMax > 0.0) {
            Text(
                text = "${state.estimatedMax} kg",
                style = MaterialTheme.typography.displayMedium
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
    }
}
