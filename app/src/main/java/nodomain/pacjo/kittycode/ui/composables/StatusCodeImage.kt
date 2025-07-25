package nodomain.pacjo.kittycode.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import nodomain.pacjo.kittycode.data.StatusCodeInfo

@Composable
fun StatusCodeImage(
    modifier: Modifier = Modifier,
    statusCodeInfo: StatusCodeInfo
) {
    AsyncImage(
        model = statusCodeInfo.imageUrl,
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentScale = ContentScale.Fit
    )
}