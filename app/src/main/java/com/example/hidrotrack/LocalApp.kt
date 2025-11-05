package com.example.hidrotrack

class LocalApp : android.app.Application() {
    companion object {
        lateinit var app: LocalApp
            private set
    }
    override fun onCreate() {
        super.onCreate()
        app = this
    }
}
