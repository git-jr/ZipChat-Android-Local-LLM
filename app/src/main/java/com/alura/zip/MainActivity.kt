package com.alura.zip

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alura.zip.samples.sampleMessages
import com.alura.zip.ui.screens.ChatScreen
import com.alura.zip.ui.screens.ResumeScreen
import com.alura.zip.ui.theme.ZipTheme
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import com.google.mediapipe.tasks.genai.llminference.LlmInference.LlmInferenceOptions
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val options = LlmInferenceOptions.builder()
            .setModelPath("/data/local/tmp/gemma2.bin")
            .setMaxTokens(1500)
            .build()

       val llmInference = LlmInference.createFromOptions(this, options)

        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                val scope = rememberCoroutineScope()
                var showChatScreen by remember { mutableStateOf(true) }
                var resumeResult by remember { mutableStateOf<String?>(null) }
                var isLoading by remember { mutableStateOf(false) }
                val listMessages = sampleMessages

                Box(
                    modifier = Modifier.padding(innerPadding)
                ) {
                    Crossfade(showChatScreen) { show ->
                        if (show) {
                            ChatScreen {
                                showChatScreen = false
                            }
                        } else {
                            ResumeScreen(
                                resumeResult = resumeResult,
                                isLoading = isLoading,
                                onSummarize = { numberOfMessages ->
                                    scope.launch {
                                        isLoading = true
                                        withContext(IO) {
                                            val messages = listMessages.takeLast(numberOfMessages)
                                                .map { Pair(it.author, it.content) }

                                            resumeResult =
                                                llmInference.generateResponse("Resuma os principais pontos dessa conversa: $messages")

                                            isLoading = false
                                        }
                                    }
                                }
                            )
                        }
                    }
                }

                onBackPressedDispatcher.addCallback(this) {
                    if (!showChatScreen) {
                        showChatScreen = true
                    } else {
                        finish()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ZipTheme {
        Greeting("Android")
    }
}