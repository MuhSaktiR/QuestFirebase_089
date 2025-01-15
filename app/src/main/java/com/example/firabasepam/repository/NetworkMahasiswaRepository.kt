package com.example.firabasepam.repository

import com.example.firabasepam.model.Mahasiswa
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class NetworkMahasiswaRepository(
    private val firestore: FirebaseFirestore
) : MahasiswaRepository {
    override suspend fun getMahasiswa(): Flow<List<Mahasiswa>> = callbackFlow { // Mengapa menggunakan callbackflow?
        val mhsCollection = firestore.collection("Mahasiswa")
            .orderBy("nama", Query.Direction.DESCENDING)
            .addSnapshotListener{value, error -> // membuka collection
                if (value != null) {
                    val mhsList = value.documents.mapNotNull {
                        it.toObject(Mahasiswa::class.java)!!
                    }
                    trySend(mhsList) // Try send memberikan fungsi untuk mengirim data ke flow
                }
            }
        awaitClose{
            mhsCollection.remove() // Menutup collection
        }
    }

    override suspend fun insertMahasiswa(mahasiswa: Mahasiswa) {
        try {
            firestore.collection("Mahasiswa").add(mahasiswa).await()
        } catch (e: Exception) {
            throw Exception("Gagal menambahkan data mahasiswa: ${e.message}")
        }
    }

    override suspend fun updateMahasiswa(mahasiswa: Mahasiswa) {
        try {
            firestore.collection("Mahasiswa")
                .document(mahasiswa.nim)
                .set(mahasiswa)
                .await()
        } catch (e: Exception) {
            throw Exception("Gagal mengupdate data mahasiswa: ${e.message}")
        }
    }

    override suspend fun deleteMahasiswa(mahasiswa: Mahasiswa) {
        try {
            val querySnapshot = firestore.collection("Mahasiswa")
                .whereEqualTo("nim", mahasiswa.nim)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val documentId = querySnapshot.documents[0].id
                firestore.collection("Mahasiswa")
                    .document(documentId)
                    .delete()
                    .await()
            } else {
                throw Exception("Mahasiswa dengan NIM ${mahasiswa.nim} tidak ditemukan.")
            }
        } catch (e: Exception) {
            throw Exception("Error deleting Mahasiswa: ${e.message}")
        }
    }

    override suspend fun getMahasiswaByNim(nim: String) : Flow<Mahasiswa> = callbackFlow {
        val mhsCollection = firestore.collection("Mahasiswa")
            .whereEqualTo("nim", nim)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    close(error)
                } else {
                    if (value != null && value.documents.isNotEmpty()) {
                        val mahasiswa = value.documents.firstOrNull()?.toObject(Mahasiswa::class.java)
                        mahasiswa?.let {
                            trySend(it)
                        } ?: close(Exception("Mahasiswa tidak ditemukan"))
                    } else {
                        close(Exception("Mahasiswa tidak ditemukan"))
                    }
                }
            }

        awaitClose { mhsCollection.remove() }
    }
}