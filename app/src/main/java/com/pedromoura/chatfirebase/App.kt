package com.pedromoura.chatfirebase

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.initialize

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Firebase.initialize(this)
    }
}