package com.rm.powercalculator.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
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
import com.rm.powercalculator.domain.model.Calculation
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
import com.rm.powercalculator.presentation.viewmodel.OneRepMaxViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(
    historyRecords: List<Calculation>,
    onNavigateBack: () -> Unit,
    onDeleteRecord: (Calculation) -> Unit,
    onUndoDelete: (Calculation) -> Unit,
    showUndoSnackbar: Calculation?,
    onHideUndoSnackbar: () -> Unit,
    viewModel: OneRepMaxViewModel,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState() }
    
    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.title_history),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.content_description_back)
                        )
                    }
                }
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
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.empty_state_title),
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(id = R.string.empty_state_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = historyRecords,
                    key = { record -> record.id }
                ) { record ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.EndToStart) {
                                onDeleteRecord(record)
                                true
                            } else {
                                false
                            }
                        }
                    )
                    
                    SwipeToDismissBox(
                        state = dismissState,
                        modifier = Modifier.animateItemPlacement(),
                        backgroundContent = {
                            SwipeBackground(dismissState = dismissState)
                        },
                        content = { HistoryRecordCard(record = record) }
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
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                dismissAction = {
                    TextButton(
                        onClick = {
                            onHideUndoSnackbar()
                        }
                    ) {
                        Text(text = stringResource(id = R.string.snackbar_dismiss))
                    }
                }
            ) {
                Text(text = stringResource(id = R.string.snackbar_record_deleted))
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
                shape = MaterialTheme.shapes.medium
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
private fun HistoryRecordCard(record: Calculation) {
    val dateFormat = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(record.timestamp))
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = record.exerciseName,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = stringResource(id = R.string.label_weight_short),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "${record.weight} kg",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.label_reps_short),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "${record.reps}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = stringResource(id = R.string.label_1rm_short),
                        style = MaterialTheme.typography.bodySmall
                    )
                    
                    Text(
                        text = "${String.format("%.1f", record.oneRepMax)} kg",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
