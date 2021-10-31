package com.example.notesappviewmodel

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.notes_item_row.view.*

class NoteRecyclerViewAdapter(private val ctx: MainActivity): RecyclerView.Adapter<NoteRecyclerViewAdapter.ItemViewHolder>() {
    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private var notes = emptyList<RoomNote>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.notes_item_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val note = notes[position]

        holder.itemView.apply {
            recycler_tv_note.text = note.note

            iv_delete.setOnClickListener {
                val builder = AlertDialog.Builder(holder.itemView.context)
                builder.setTitle("Are you sure you want to delete this note? ")
                builder.setNegativeButton("Cancel"){_, _ -> }
                builder.setPositiveButton("Yes"){_, _ -> ctx.myViewModel.deleteNote(note.pk)}
                builder.show()
            }

            iv_edit.setOnClickListener {
                ctx.update(note)
            }

        }
    }

    override fun getItemCount(): Int = notes.size

    fun update(notes: List<RoomNote>){
        println("UPDATING DATA")
        this.notes = notes
        notifyDataSetChanged()
    }

}