package com.example.notesappviewmodel

import android.content.Context
import androidx.room.Database
import androidx.room.*


@Database(entities = [RoomNote::class], version = 1, exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {

    companion object{
        var instance: NoteDatabase?= null
        fun getInstance(ctx: Context): NoteDatabase{
            if(instance != null)
                return instance as NoteDatabase
            instance = Room.databaseBuilder(ctx, NoteDatabase::class.java, "Notes").run {
                allowMainThreadQueries()
            }.build()
            return instance as NoteDatabase
        }
    }

    abstract fun noteDao():NoteDao;

}