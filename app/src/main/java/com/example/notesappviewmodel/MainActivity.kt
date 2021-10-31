package com.example.notesappviewmodel

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var rvMain: RecyclerView
    private lateinit var adapter: NoteRecyclerViewAdapter
    private lateinit var input: EditText
    private lateinit var addButton: Button

    lateinit var myViewModel: MyViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        myViewModel.getNotes().observe(this, {
                notes -> adapter.update(notes)
        })

        rvMain = findViewById(R.id.rvMain)
        input = findViewById(R.id.et_note)
        addButton = findViewById(R.id.btn_add_note)
        adapter = NoteRecyclerViewAdapter(this)
        rvMain.adapter = adapter
        rvMain.layoutManager = LinearLayoutManager(this)

        addButton.setOnClickListener {
            if(input.text.isNotEmpty()){
                myViewModel.addNote(input.text.toString())
                input.text.clear()
                input.clearFocus()
            }
        }

    }

    fun update(note: RoomNote){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update note")
        val ed = EditText(this)
        ed.hint = note.note
        builder.setView(ed)
        builder.setNegativeButton("Cancel"){_, _ -> }
        builder.setPositiveButton("Yes"){_, _ ->
            if(ed.text.isNotEmpty())
                myViewModel.editNote(note.pk, ed.text.toString())
        }
        builder.show()
    }

}