package com.rm.powercalculator.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rm.powercalculator.data.HistoryRecordEntity
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.res.stringResource
import com.rm.powercalculator.R
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    historyRecords: List<HistoryRecordEntity>,
    onNavigateBack: () -> Unit,
    onDeleteRecord: (HistoryRecordEntity) -> Unit,
    onUndoDelete: (HistoryRecordEntity) -> Unit,
    showUndoSnackbar: HistoryRecordEntity?,
    onHideUndoSnackbar: () -> Unit,
    viewModel: OneRepMaxViewModel,
    modifier: Modifier = Modifier
) {
    val darkBackground = Color(0xFF121212)
    val cardBackground = Color(0xFF1E1E1E)
    val accentColor = Color(0xFFFF9800)
    
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message = message)
            }
        }
    }
    
    Scaffold(
        modifier = modifier.background(darkBackground),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_history),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.content_description_back),
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
        if (historyRecords.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.empty_state_title),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = Color.Gray
                        ),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(id = R.string.empty_state_subtitle),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray.copy(alpha = 0.7f)
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // History list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = historyRecords,
                    key = { record -> record.id }
                ) { record ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.EndToStart) {
                                // Trigger the delete event but let the animation complete
                                onDeleteRecord(record)
                                // Return true to allow the item to animate off-screen
                                true
                            } else {
                                false
                            }
                        }
                    )
                    
                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            SwipeBackground(dismissState = dismissState)
                        },
                        content = {
                            HistoryRecordCard(
                                record = record,
                                accentColor = accentColor,
                                cardBackground = cardBackground
                            )
                        }
                    )
                }
            }
        }
        
        // Undo Snackbar
        AnimatedVisibility(
            visible = showUndoSnackbar != null,
            enter = fadeIn(animationSpec = tween(300)),
            exit = fadeOut(animationSpec = tween(300))
        ) {
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    TextButton(
                        onClick = {
                            showUndoSnackbar?.let { onUndoDelete(it) }
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.snackbar_undo),
                            color = accentColor
                        )
                    }
                },
                dismissAction = {
                    TextButton(
                        onClick = {
                            onHideUndoSnackbar()
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.snackbar_dismiss),
                            color = Color.White
                        )
                    }
                }
            ) {
                Text(
                    text = stringResource(id = R.string.snackbar_record_deleted),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun SwipeBackground(
    dismissState: SwipeToDismissBoxState
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.Red,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 20.dp),
        contentAlignment = when (dismissState.dismissDirection) {
            SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
            SwipeToDismissBoxValue.EndToStart -> Alignment.CenterEnd
            else -> Alignment.Center
        }
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(id = R.string.content_description_delete),
            tint = Color.White,
            modifier = Modifier
                .size(24.dp)
                .scale(
                    scale = 0.5f + (dismissState.progress * 0.5f)
                )
                .alpha(
                    alpha = dismissState.progress
                )
        )
    }
}

@Composable
private fun HistoryRecordCard(
    record: HistoryRecordEntity,
    accentColor: Color,
    cardBackground: Color
) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(record.timestamp))
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = cardBackground
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header with date
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            )
            
            // Values row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Weight
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(id = R.string.label_weight_short),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray
                        )
                    )
                    Text(
                        text = "${record.weight} kg",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                
                // Reps
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.label_reps_short),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray
                        )
                    )
                    Text(
                        text = "${record.reps}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                
                // 1RM
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = stringResource(id = R.string.label_1rm_short),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color.Gray
                        )
                    )
                    
                    Text(
                        text = "${String.format("%.1f", record.oneRepMax)} kg",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = accentColor,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
