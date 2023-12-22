package com.example.uas_project_1.Database


import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


// Anotasi @Entity digunakan untuk mendeklarasikan bahwa kelas ini adalah entitas (tabel) dalam database.
@Entity(tableName = "note_table")
data class Note(

    // Anotasi @PrimaryKey menandakan bahwa variabel id adalah kunci utama (primary key) dari tabel.
    // autoGenerate = false menandakan bahwa nilai id tidak akan dihasilkan secara otomatis oleh SQLite.
    @PrimaryKey(autoGenerate = false)
    @NonNull
    var id:  String,


    // Anotasi @ColumnInfo digunakan untuk memberikan nama kolom dalam tabel yang sesuai dengan nama variabel.
    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "author")
    var author: String,

    @ColumnInfo(name = "desc")
    var desc: String,

    @ColumnInfo(name = "imageurl")
    var imageurl: String
)
{

    // Konstruktor tambahan tanpa argumen digunakan untuk deserialisasi Firebase.
    // Firebase memerlukan konstruktor tanpa argumen untuk mengonversi data dari database Firebase ke objek.
    // Add a no-argument constructor for Firebase deserialization
    constructor() : this("", "", "","","")
}