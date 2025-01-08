package com.example.firabasepam

import android.app.Application
import com.example.firabasepam.di.AppContainer
import com.example.firabasepam.di.MahasiswaContainer

class MahasiswaApplication: Application(){
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = MahasiswaContainer()
    }
}