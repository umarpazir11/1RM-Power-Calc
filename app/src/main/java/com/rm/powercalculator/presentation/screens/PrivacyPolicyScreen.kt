package com.rm.powercalculator.presentation.screens

import android.graphics.Color as AndroidColor
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import com.rm.powercalculator.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    val url = "https://gist.githubusercontent.com/umarpazir11/02ccfb12a0038f77ecea2ba6b21676fe/raw/dcd0eacc4460e211ab37d7047949cc1c0ee3a800/gistfile1.txt"

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
            AndroidView(
                factory = {
                    WebView(it).apply {
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                isLoading = false
                                // Inject CSS to style the page for dark mode
                                view?.loadUrl("javascript:(function() { " +
                                        "var parent = document.getElementsByTagName('head').item(0);" +
                                        "var style = document.createElement('style');" +
                                        "style.type = 'text/css';" +
                                        "style.innerHTML = 'body { color: #FFFFFF; }';" +
                                        "parent.appendChild(style)" +
                                        "})();")
                            }

                            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                                super.onReceivedError(view, errorCode, description, failingUrl)
                                isLoading = false
                                hasError = true
                            }
                        }
                        setBackgroundColor(AndroidColor.TRANSPARENT)
                        settings.javaScriptEnabled = true
                        loadUrl(url)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .let { if (isLoading || hasError) it.alpha(0f) else it }
            )

            if (isLoading) {
                CircularProgressIndicator()
            }

            if (hasError) {
                Text("Failed to load content. Please check your internet connection.")
            }
        }
    }
}
