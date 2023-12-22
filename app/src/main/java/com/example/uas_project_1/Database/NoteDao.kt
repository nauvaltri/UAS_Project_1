package com.example.uas_project_1.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NoteDao {

    // Mendapatkan semua data film dari tabel dan mengembalikan hasilnya sebagai LiveData
    @Query("SELECT * FROM note_table")
    fun getAllFilm(): LiveData<List<Note>>


    // Memasukkan data film ke dalam tabel
    // Jika ada konflik (data dengan kunci utama yang sama), maka data lama akan digantikan dengan data baru
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFilm(film: List<Note>)


    // Menghapus satu data film dari tabel
    @Delete
    fun deleteFilm(film: Note)


    // Menghapus semua data film dari tabel
    @Query("DELETE FROM note_table")
    fun deleteAllFilm()

}