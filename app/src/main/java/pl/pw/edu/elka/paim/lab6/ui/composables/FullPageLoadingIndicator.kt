package pl.pw.edu.elka.paim.lab6.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FullPageLoadingIndicator() {
    Box(
        modifier = Modifier.Companion.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularWavyProgressIndicator()
    }
}