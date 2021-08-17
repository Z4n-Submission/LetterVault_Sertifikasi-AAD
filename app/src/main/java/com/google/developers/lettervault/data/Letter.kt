package com.google.developers.lettervault.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Model class holds information about the letter and defines table for the Room
 * database with auto generate primary key.
 */

@Entity(tableName = "letter")
data class Letter(

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    val id: Long = 0,

    @NonNull
    @ColumnInfo(name = "subject")
    val subject: String,

    @NonNull
    @ColumnInfo(name = "content")
    val content: String,

    @NonNull
    @ColumnInfo(name = "created")
    val created: Long,

    @NonNull
    @ColumnInfo(name = "expires")
    val expires: Long,

    @NonNull
    @ColumnInfo(name = "opened")
    val opened: Long = 0
)
