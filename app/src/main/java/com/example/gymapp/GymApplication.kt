package com.example.gymapp



import android.app.Application
import com.example.gymapp.di.AppContainer

class GymApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}