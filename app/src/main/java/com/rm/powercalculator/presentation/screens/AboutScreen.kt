package com.rm.powercalculator.presentation.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.rm.powercalculator.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val darkBackground = Color(0xFF121212)
    val cardBackground = Color(0xFF1E1E1E)
    val accentColor = Color(0xFFFF9800)

    Scaffold(
        modifier = modifier.background(darkBackground),
        topBar = {
            TopAppBar(
                title = { Text("About", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.content_description_back),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = cardBackground)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Formula Used",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White
            )
            Text(
                text = "This app uses the Brzycki formula to estimate your one-rep max (1RM):",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.8f)
            )
            Text(
                text = "Weight / (1.0278 - 0.0278 * Reps)",
                style = MaterialTheme.typography.bodyLarge,
                color = accentColor,
                fontWeight = FontWeight.Bold
            )

            val annotatedString = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.White, fontSize = MaterialTheme.typography.bodyLarge.fontSize)) {
                    append("For more information, please see our ")
                }
                pushStringAnnotation(tag = "URL", annotation = "https://google.com")
                withStyle(style = SpanStyle(color = accentColor, fontWeight = FontWeight.Bold, fontSize = MaterialTheme.typography.bodyLarge.fontSize)) {
                    append("Privacy Policy")
                }
                pop()
            }

            ClickableText(
                text = annotatedString,
                onClick = {
                    annotatedString.getStringAnnotations(tag = "URL", start = it, end = it)
                        .firstOrNull()?.let { annotation ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                            context.startActivity(intent)
                        }
                }
            )
        }
    }
}
