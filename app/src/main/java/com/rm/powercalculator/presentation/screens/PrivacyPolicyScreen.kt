package com.rm.powercalculator.presentation.screens

import android.graphics.Color as AndroidColor
import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.rm.powercalculator.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var screenState by remember { mutableStateOf<ScreenState>(ScreenState.Loading) }
    val url = "https://gist.githubusercontent.com/umarpazir11/02ccfb12a0038f77ecea2ba6b21676fe/raw/dcd0eacc4460e211ab37d7047949cc1c0ee3a800/gistfile1.txt"

    // Use LaunchedEffect to fetch the text data from the network safely.
    LaunchedEffect(Unit) {
        launch(Dispatchers.IO) {
            try {
                // Fetch the raw text from the URL.
                val textContent = URL(url).readText()

                // Construct the full HTML document with our styles.
                val htmlData = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <style>
                            body {
                                color: #EBEBF5; /* Use a near-white color for better readability */
                                background-color: transparent;
                                font-family: sans-serif;
                                margin: 16px;
                            }
                            pre {
                                white-space: pre-wrap; /* Ensures long lines wrap correctly */
                                word-wrap: break-word;
                            }
                        </style>
                    </head>
                    <body>
                        <pre>$textContent</pre>
                    </body>
                    </html>
                """.trimIndent()

                // Switch back to the Main thread to update the state.
                withContext(Dispatchers.Main) {
                    screenState = ScreenState.Success(htmlData)
                }
            } catch (e: Exception) {
                // Handle exceptions like no network connection.
                withContext(Dispatchers.Main) {
                    screenState = ScreenState.Error
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Privacy Policy") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.content_description_back),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            // Use a 'when' block to display UI based on the current state.
            when (val state = screenState) {
                is ScreenState.Loading -> {
                    CircularProgressIndicator()
                }
                is ScreenState.Success -> {
                    AndroidView(
                        factory = { context ->
                            WebView(context).apply {
                                // Set background to transparent to see the Scaffold's background.
                                setBackgroundColor(AndroidColor.TRANSPARENT)
                                // Load the pre-styled HTML data. No complex client needed.
                                loadDataWithBaseURL(null, state.htmlData, "text/html", "UTF-8", null)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                is ScreenState.Error -> {
                    Text("Failed to load content. Please check your internet connection.")
                }
            }
        }
    }
}

// A sealed interface to represent the different states of our screen.
private sealed interface ScreenState {
    object Loading : ScreenState
    data class Success(val htmlData: String) : ScreenState
    object Error : ScreenState
}
