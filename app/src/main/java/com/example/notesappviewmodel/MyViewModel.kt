package com.example.notesappviewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MyViewModel(application: Application): AndroidViewModel(application) {

    private val repository: NoteRepository
    private val notes: LiveData<List<RoomNote>>

    init {
        val noteDao = NoteDatabase.getInstance(application).noteDao()
        repository = NoteRepository(noteDao)
        notes = repository.getNotes
    }

    fun getNotes(): LiveData<List<RoomNote>>{
        return notes
    }

    fun addNote(noteText: String){
        CoroutineScope(IO).launch {
            repository.addNote(RoomNote(0, noteText))
        }
    }

    fun editNote(noteID: Int, noteText: String){
        CoroutineScope(IO).launch {
            repository.updateNote(noteText, noteID)
        }
    }

    fun deleteNote(noteID: Int){
        CoroutineScope(IO).launch {
            repository.deleteNote(RoomNote(noteID,""))
        }
    }

}