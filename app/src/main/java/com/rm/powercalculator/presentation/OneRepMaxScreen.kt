package com.rm.powercalculator.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rm.powercalculator.R
import com.rm.powercalculator.domain.model.FormulaType
import com.rm.powercalculator.presentation.screens.OneRepMaxEvent
import com.rm.powercalculator.presentation.screens.OneRepMaxScreenState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneRepMaxScreen(
    state: OneRepMaxScreenState,
    onEvent: (OneRepMaxEvent) -> Unit,
    onHistoryClick: () -> Unit,
    onAboutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val darkBackground = Color(0xFF121212)
    val cardBackground = Color(0xFF1E1E1E)
    val accentColor = Color(0xFFFF9800) // Vibrant Orange

    Scaffold(
        modifier = modifier.background(darkBackground),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_main),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onHistoryClick) {
                        Icon(
                            imageVector = Icons.Default.List,
                            contentDescription = stringResource(id = R.string.content_description_view_history),
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = onAboutClick) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "About",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = cardBackground
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(id = R.string.subtitle_calculator),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = cardBackground
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        FormulaType.values().forEachIndexed { index, formulaType ->
                            SegmentedButton(
                                selected = state.formulaType == formulaType,
                                onClick = { onEvent(OneRepMaxEvent.OnFormulaChange(formulaType)) },
                                shape = when (index) {
                                    0 -> RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                                    FormulaType.values().lastIndex -> RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp)
                                    else -> RoundedCornerShape(0.dp)
                                }
                            ) {
                                Text(formulaType.name)
                            }
                        }
                    }

                    OutlinedTextField(
                        value = state.exerciseName,
                        onValueChange = { onEvent(OneRepMaxEvent.OnExerciseNameChange(it)) },
                        label = { 
                            Text(
                                "Exercise Name",
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentColor,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                            focusedLabelColor = accentColor,
                            unfocusedLabelColor = Color.Gray.copy(alpha = 0.7f),
                            cursorColor = accentColor,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = state.weightInput,
                        onValueChange = { onEvent(OneRepMaxEvent.OnWeightChange(it)) },
                        label = { 
                            Text(
                                stringResource(id = R.string.label_weight),
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        },
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentColor,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                            focusedLabelColor = accentColor,
                            unfocusedLabelColor = Color.Gray.copy(alpha = 0.7f),
                            cursorColor = accentColor,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = state.repsInput,
                        onValueChange = { onEvent(OneRepMaxEvent.OnRepsChange(it)) },
                        label = { 
                            Text(
                                stringResource(id = R.string.label_reps),
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        },
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = accentColor,
                            unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                            focusedLabelColor = accentColor,
                            unfocusedLabelColor = Color.Gray.copy(alpha = 0.7f),
                            cursorColor = accentColor,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Button(
                        onClick = {
                            keyboardController?.hide()
                            onEvent(OneRepMaxEvent.OnCalculateClick)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            stringResource(id = R.string.button_calculate),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        )
                    }
                }
            }
            
            AnimatedVisibility(
                visible = state.oneRepMax != null && state.oneRepMax > 0.0,
                enter = fadeIn(
                    animationSpec = tween(800)
                ) + slideInVertically(
                    animationSpec = tween(800),
                    initialOffsetY = { 50 }
                ),
                exit = fadeOut(
                    animationSpec = tween(300)
                ) + slideOutVertically(
                    animationSpec = tween(300),
                    targetOffsetY = { -50 }
                )
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = cardBackground
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(id = R.string.label_estimated_1rm),
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            AnimatedContent(
                                targetState = state.oneRepMax ?: 0.0,
                                transitionSpec = {
                                    ContentTransform(
                                        targetContentEnter = slideInVertically(
                                            animationSpec = tween(800),
                                            initialOffsetY = { 100 }
                                        ) + fadeIn(
                                            animationSpec = tween(800)
                                        ),
                                        initialContentExit = slideOutVertically(
                                            animationSpec = tween(300),
                                            targetOffsetY = { -100 }
                                        ) + fadeOut(
                                            animationSpec = tween(300)
                                        )
                                    )
                                }
                            ) { targetValue ->
                                Text(
                                    text = "${String.format("%.1f", targetValue)}",
                                    style = MaterialTheme.typography.displayLarge.copy(
                                        color = accentColor,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 64.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = stringResource(id = R.string.unit_kg),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    color = Color.Gray,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                }
            }
            
            if (state.percentages.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = cardBackground
                    ),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Percentages",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                        )
                        state.percentages.forEach { p ->
                            ListItem(
                                headlineContent = { Text("${p.percentage}%", color = Color.White) },
                                trailingContent = { Text("${String.format("%.1f", p.weight)} kg", color = accentColor, fontWeight = FontWeight.SemiBold) },
                                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))

            AdMobBanner(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }
    }
}

@Composable
fun AdMobBanner(modifier: Modifier = Modifier) {
    val context = LocalContext.current
}
