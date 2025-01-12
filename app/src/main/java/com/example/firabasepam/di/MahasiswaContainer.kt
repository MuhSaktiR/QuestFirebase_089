package com.example.firabasepam.di

import com.example.firabasepam.repository.MahasiswaRepository
import com.example.firabasepam.repository.NetworkMahasiswaRepository
import com.google.firebase.firestore.FirebaseFirestore

interface AppContainer {
    val mahasiswaRepository: MahasiswaRepository
}

class MahasiswaContainer : AppContainer {
    private val firestore : FirebaseFirestore = FirebaseFirestore.getInstance() // getInstance untuk mengakses ke firestore
    override val mahasiswaRepository: MahasiswaRepository by lazy {
        NetworkMahasiswaRepository(firestore)
    }
}