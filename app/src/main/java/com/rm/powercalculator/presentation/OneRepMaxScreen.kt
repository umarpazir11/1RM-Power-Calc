package com.rm.powercalculator.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.rm.powercalculator.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneRepMaxScreen(
    state: OneRepMaxState,
    onEvent: (OneRepMaxEvent) -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            // Subtitle
            Text(
                text = stringResource(id = R.string.subtitle_calculator),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                ),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Main Calculator Card
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
                    // Weight Input
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
                    
                    // Reps Input
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
                    
                    // Calculate Button
                    Button(
                        onClick = { onEvent(OneRepMaxEvent.OnCalculateClick) },
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
            
            // Result Display
            AnimatedVisibility(
                visible = state.estimatedMax > 0.0,
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
                                targetState = state.estimatedMax,
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
            
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
