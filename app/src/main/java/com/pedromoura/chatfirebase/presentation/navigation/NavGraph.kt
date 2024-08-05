package com.pedromoura.chatfirebase.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.pedromoura.chatfirebase.presentation.chat.ChatScreen
import com.pedromoura.chatfirebase.presentation.chat.ChatViewModel
import com.pedromoura.chatfirebase.presentation.login.LoginScreen
import com.pedromoura.chatfirebase.presentation.login.LoginViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Chat : Screen("chat")
}

@Composable
fun NavGraph(startDestination: String = Screen.Login.route) {

    FirebaseApp.initializeApp(LocalContext.current)
    val database = FirebaseDatabase.getInstance()

    val navController = rememberNavController()
    val viewModel = LoginViewModel(LocalContext.current)
    val chatViewModel = ChatViewModel(database, LocalContext.current)

    NavHost(navController = navController, startDestination = startDestination ) {
        composable(Screen.Login.route) { LoginScreen(viewModel, navController = navController) }
        composable(Screen.Chat.route) { ChatScreen(chatViewModel)  }
    }
}