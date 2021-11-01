package com.example.notesappviewmodel

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {

    private lateinit var rvMain: RecyclerView
    private lateinit var adapter: NoteRecyclerViewAdapter
    private lateinit var input: EditText
    private lateinit var addButton: Button

    lateinit var myViewModel: MyViewModel

    private lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = FirebaseFirestore.getInstance()

        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        myViewModel.getNotes().observe(this, { notes ->
            adapter.update(notes)
        })

        rvMain = findViewById(R.id.rvMain)
        input = findViewById(R.id.et_note)
        addButton = findViewById(R.id.btn_add_note)
        adapter = NoteRecyclerViewAdapter(this)
        rvMain.adapter = adapter
        rvMain.layoutManager = LinearLayoutManager(this)

        addButton.setOnClickListener {
            if (input.text.isNotEmpty()) {
                myViewModel.addNote(input.text.toString())
                input.text.clear()
                input.clearFocus()
            }
        }

    }

    fun update(note: RoomNote) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update note")
        val ed = EditText(this)
        ed.hint = note.note
        builder.setView(ed)
        builder.setNegativeButton("Cancel") { _, _ -> }
        builder.setPositiveButton("Yes") { _, _ ->
            if (ed.text.isNotEmpty())
                myViewModel.editNote(note.pk, ed.text.toString())
        }
        builder.show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.firebase -> {
                val dataFromFirebase = mutableListOf<RoomNote>()
                var n = ""
                var pk = -1
                db.collection("notes")
                        .get()
                        .addOnSuccessListener { res ->
                            for (note in res) {
                                note.data.map { (key, value) ->
                                     if (key.toString() == "pk") {
                                            pk = value.toString().toInt()
                                        }
                                        else{
                                            n = value.toString()
                                        }

                                }
                                dataFromFirebase.add(RoomNote(pk, n))
                            }
                            adapter.update(dataFromFirebase)
                            if (dataFromFirebase.size > 0)
                                Toast.makeText(this, "${dataFromFirebase.size} items", Toast.LENGTH_LONG).show()
                            else
                                Toast.makeText(this, "Make sure you click the link button to link your local database with firebase every time", Toast.LENGTH_LONG).show()
                        }
            }
            R.id.link -> {

                val dataFromFirebase = mutableListOf<RoomNote>()
                var n = ""
                var pk = -1
                db.collection("notes")
                    .get()
                    .addOnSuccessListener { res ->
                        for (note in res) {
                            note.data.map { (key, value) ->
                                if (key.toString() == "pk") {
                                    pk = value.toString().toInt()
                                } else {
                                    n = value.toString()
                                }

                            }
                            dataFromFirebase.add(RoomNote(pk, n))
                        }
                        val notesFromSqLite = myViewModel.getNotes().value!!
                        val count = notesFromSqLite.size
                        var current = 0

                        for (i in notesFromSqLite) {
                            var bool = true
                            for (j in dataFromFirebase) {
                                if (j.pk == i.pk){
                                    Log.d("already existtt", "lfjlei")
                                    current++
                                    bool = false
                                }
                            }
                            if(bool){
                                db.collection("notes")
                                    .add(
                                        hashMapOf(
                                            "pk" to i.pk,
                                            "note" to i.note
                                        )
                                    )
                                    .addOnSuccessListener {
                                        current++
                                        Toast.makeText(this, "$count/$current", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                            }
                        }
                    }


            }
            R.id.sqlite -> {
                val notesFromSqLite = myViewModel.getNotes()
                adapter.update(notesFromSqLite.value!!)
            }

        }
        return super.onOptionsItemSelected(item)
    }


}