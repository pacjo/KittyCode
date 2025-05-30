package pl.pw.edu.elka.paim.lab6.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import pl.pw.edu.elka.paim.lab6.data.StatusCodeInfo

@Composable
fun StatusCodeImage(statusCodeInfo: StatusCodeInfo) {
    AsyncImage(
        model = statusCodeInfo.imageUrl,
        contentDescription = null,
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(16.dp),
        contentScale = ContentScale.Companion.Fit
    )
}