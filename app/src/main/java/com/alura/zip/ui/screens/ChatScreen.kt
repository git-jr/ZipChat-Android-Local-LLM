package com.alura.zip.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alura.zip.samples.sampleMessages

data class Message(
    val date: String,
    val author: String,
    val content: String,
    val color: Color = Color(0xFFF63073)
)

@Composable
fun ChatScreen(
    listSample: List<Message> = sampleMessages,
    showSummarizeScreen: () -> Unit = {}
) {
    val messages: SnapshotStateList<Message> =
        remember { mutableStateListOf(*listSample.toTypedArray()) }

    var message by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAEEDA))
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages, key = { it.content.hashCode() }) { message ->
                MessageBubble(message)
            }
        }
        Row(
            modifier = Modifier
                .sizeIn(minHeight = 56.dp)
                .background(Color(0xFFFDFDFD)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                placeholder = {
                    Text(
                        text = "Digite uma mensagem...",
                        color = Color(0xFF00162C)
                    )
                },
                value = message,
                onValueChange = { message = it },
                modifier = Modifier
                    .weight(5f)
                    .background(color = Color.Transparent),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(
                    onSend = {
                        messages.add(Message("10:46", "User", message))
                        message = ""
                        focusManager.clearFocus()
                    }
                )
            )

            IconButton(onClick = {
                focusManager.clearFocus()
                showSummarizeScreen()
            }) {
                Icon(
                    Icons.AutoMirrored.Filled.List,
                    contentDescription = "Resumir",
                    tint = Color(0xFF00162C),
                    modifier = Modifier.weight(1f)
                )
            }

            IconButton(onClick = {
                messages.add(Message("10:46", "User", message))
                message = ""
                focusManager.clearFocus()
            }) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Enviar",
                    tint = Color(0xFF00162C),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@Composable
fun MessageBubble(message: Message) {
    val isUser = message.author == "Eu"
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = if (isUser) Color(0xFFFDFDFD) else Color(0xFFD6FAD0),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = message.author,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = message.color
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "● " + message.date, style = MaterialTheme.typography.bodySmall)
            }

            Text(text = message.content, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatScreenPreview() {
    ChatScreen()
}
