package pl.pw.edu.elka.paim.lab6.ui.utils

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import pl.pw.edu.elka.paim.lab6.ui.theme.AppTheme

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PreviewScope(
    content: @Composable ((SharedTransitionScope, AnimatedContentScope) -> Unit)
) {
    AppTheme {
        SharedTransitionLayout {
            AnimatedContent(null) {
                content(this@SharedTransitionLayout, this@AnimatedContent)
            }
        }
    }
}