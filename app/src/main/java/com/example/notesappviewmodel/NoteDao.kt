package com.example.notesappviewmodel

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface NoteDao {
    @Query("SELECT * FROM Notes")
    fun getNotes(): LiveData<List<RoomNote>>

    @Insert
    suspend fun addNote(note: RoomNote)

    @Delete
    suspend fun deleteNote(note: RoomNote)

    @Query("UPDATE Notes SET note = :note WHERE pk = :pk")
    suspend fun updateNote(note: String, pk: Int)
}