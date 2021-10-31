package com.example.notesappviewmodel

import androidx.lifecycle.LiveData

class NoteRepository(private val noteDao: NoteDao) {

    val getNotes: LiveData<List<RoomNote>> = noteDao.getNotes()

    suspend fun addNote(note: RoomNote){
        noteDao.addNote(note)
    }

    suspend fun updateNote(note: String, pk: Int){
        noteDao.updateNote(note, pk)
    }

    suspend fun deleteNote(note: RoomNote){
        noteDao.deleteNote(note)
    }
}