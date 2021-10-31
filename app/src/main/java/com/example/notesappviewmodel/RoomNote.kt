package com.example.notesappviewmodel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Notes")
class RoomNote(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "pk") val pk: Int = 0,
    @ColumnInfo(name = "note") val note: String,
)