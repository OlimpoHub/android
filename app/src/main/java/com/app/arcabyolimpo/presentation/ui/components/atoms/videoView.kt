package com.app.arcabyolimpo.presentation.ui.components.atoms

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
/**
 * A square place to let users view videos from YouTube. NEEDS CHANGE DEPENDING ON WHERE THE VIDEOS
 * ARE UPLOADED
 *
 * @param modifier Modifier to adjust the video's layout (size, padding, etc.).
 * @param videoUrl URL from Youtube with the video's ID.
 *
 */

@Composable
fun videoView(
    modifier : Modifier = Modifier,
    videoUrl: String
){
    val videoid = "v=([^&]+)".toRegex().find(videoUrl)?.groupValues?.get(1)
    val html = """
        <body style="margin:0px;padding:0px;">
            <iframe 
                width="100%" 
                height="100%" 
                src="https://www.youtube.com/embed/$videoid" 
                frameborder="0" 
                allow="autoplay; encrypted-media" 
                allowfullscreen
            ></iframe>
        </body>
    """.trimIndent()
    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadDataWithBaseURL(
                    "https://www.youtube.com",
                    html,
                    "text/html",
                    "utf-8",
                    null
                )
            }
        }
    )
}