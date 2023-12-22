package com.example.uas_project_1.Database


import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = false)
    @NonNull
    var id:  String,

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
    // Add a no-argument constructor for Firebase deserialization
    constructor() : this("", "", "","","")
}