package com.pedromoura.chatfirebase.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.database.FirebaseDatabase
import com.pedromoura.chatfirebase.presentation.model.Message
import com.pedromoura.chatfirebase.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(chatViewModel: ChatViewModel) {

    val messages by chatViewModel.messages.collectAsState()

    Column {
        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = false,
            contentPadding = PaddingValues(8.dp)
        ) {
            items(messages) { message ->
                ChatItem(message)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            TextField(
                value = chatViewModel.messageText,
                onValueChange = { chatViewModel.onMessageTextChanged(it) },
                modifier = Modifier.weight(1f)
            )
            Button( onClick = { chatViewModel.sendMessage() }) {
                Text("Enviar")
            }
        }

    }
}

@Composable
fun ChatItem(message: Message) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        if(message.senderId == "1") {
            Spacer(modifier = Modifier.weight(1f))
        }

        Box (
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(4.dp)
                .background(
                    if (message.senderId == "1") {
                        Purple40
                    } else {
                        Color.Green
                    }
                )
                .padding(8.dp)
        ) {
            message.text?.let {
                Text(
                    text = it,
                    color = Color.White
                )
            }
        }

        if(message.senderId == "1") {
            Spacer(modifier = Modifier.weight(1f))
        }

    }
}

@Preview
@Composable
fun PreviewChat() {
    val database = FirebaseDatabase.getInstance()
    val fakeViewModel = ChatViewModel(database, LocalContext.current)
    ChatScreen(fakeViewModel)
}