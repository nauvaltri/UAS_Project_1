package com.example.uas_project_1.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase


// Anotasi @Database digunakan untuk menandai kelas sebagai kelas database dan mendefinisikan entitas serta versi database.
@Database(entities = [Note::class], version = 2, exportSchema = false)
abstract class NoteRoomDatabase : RoomDatabase() {

    // Abstrak method dao() digunakan untuk mendapatkan objek Data Access Object (DAO) untuk operasi database.
    abstract fun dao(): NoteDao


    // Companion object berfungsi sebagai penyimpanan singleton untuk instance database.
    companion object {

        // Anotasi @Volatile digunakan untuk menandai bahwa nilai INSTANCE dapat diakses oleh beberapa utas secara aman.
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null


        // Fungsi getDatabase digunakan untuk mendapatkan instance database menggunakan metode singleton.
        fun getDatabase(context: Context): NoteRoomDatabase {
            return INSTANCE ?: synchronized(this) {

                // Membuat instance database jika belum ada.
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteRoomDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}