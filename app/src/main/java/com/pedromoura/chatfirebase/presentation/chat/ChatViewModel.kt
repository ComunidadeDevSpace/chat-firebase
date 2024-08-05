package com.pedromoura.chatfirebase.presentation.chat

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pedromoura.chatfirebase.presentation.model.Message
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ChatViewModel(
    private val database: FirebaseDatabase,
    private val context: Context) : ViewModel()
{
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages: StateFlow<List<Message>> = _messages
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("login_prefs", Context.MODE_PRIVATE)
    var messageText by mutableStateOf("")
        private set

    var username: String = ""
    var password: String = ""
    var userId: String = ""

    init {
        val messageRef = database.reference.child("messages")

        messageRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val messagesList = mutableListOf<Message>()
                dataSnapshot.children.forEach { child ->
                    val message = child.getValue(Message::class.java)
                    message?.let { messagesList.add(it) }
                }

                _messages.value = messagesList
            }

            override fun onCancelled(error: DatabaseError) {
                // Tratar os erros
            }

        })
    }

    fun onMessageTextChanged(text: String) {
        messageText = text
    }

    fun sendMessage() {
        viewModelScope.launch {
            username = sharedPreferences.getString("USERNAME", "") ?: ""
            password = sharedPreferences.getString("PASSWORD", "") ?: ""
            userId = sharedPreferences.getString("USERID", "") ?: ""
        }

        val newMessage = Message(
            UUID.randomUUID().toString(),
            messageText,
            userId ?: "",
            System.currentTimeMillis() / 1000L //timestamp em segundos
        )

        database.reference.child("messages").push().setValue(newMessage)
            .addOnSuccessListener {
                //Depois que a message foi salva a gente pode tratar
            }
            .addOnFailureListener {
                // Tratamento de Falhas
            }

        messageText = ""
    }
}