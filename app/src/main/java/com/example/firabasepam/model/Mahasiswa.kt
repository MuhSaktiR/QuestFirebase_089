package com.example.firabasepam.model

data class Mahasiswa(
    val nim: String,
    val nama: String,
    val jenis_kelamin: String,
    val alamat: String,
    val kelas: String,
    val angkatan: String,
    val judulSkripsi: String,
    val dosPem1: String,
    val dosPem2: String
) {
    constructor() : this("","","","","","","","","")
}