package ru.sorokin.gb_material.view.notes

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.sorokin.gb_material.R
import ru.sorokin.gb_material.databinding.NotesItemBinding
import ru.sorokin.gb_material.model.notes.Note

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private var notes: MutableList<Note> = mutableListOf()
    private var currentPosition: Int = RecyclerView.NO_POSITION

    fun setNotes(list: List<Note>) {
        notes.clear()
        notes.addAll(list)
        notifyDataSetChanged()
    }

    fun addNote(note: Note) {
        val position = if (currentPosition != RecyclerView.NO_POSITION) {
            currentPosition
        } else {
            MIN_INDEX
        }
        notes.add(position, note)
        currentPosition = position
        notifyItemInserted(position)
    }

    private fun checkCurrentPosition() {
        if (currentPosition < MIN_INDEX) {
            currentPosition = MIN_INDEX
        }
        if (notes.size == 0) {
            currentPosition = RecyclerView.NO_POSITION
        }
    }

    fun deleteCurrentNote() {
        if (currentPosition != RecyclerView.NO_POSITION) {
            notes.removeAt(currentPosition)
            notifyItemRemoved(currentPosition)
            currentPosition--
            checkCurrentPosition()
        }
    }

    fun updateCurrentNote(newTitle: String, newBody: String) {
        if (currentPosition != RecyclerView.NO_POSITION) {
            notes[currentPosition].title = newTitle
            notes[currentPosition].body = newBody
            notifyItemChanged(currentPosition)
        }
    }

    fun onItemMoved(fromPosition: Int, toPosition: Int) {
        notes.removeAt(fromPosition).apply {
            notes.add(
                if (toPosition > fromPosition) {
                    toPosition - 1
                } else {
                    toPosition
                }, this
            )
        }
        notifyItemMoved(fromPosition, toPosition)
        checkCurrentPosition()
    }

    fun onItemDismiss(position: Int) {
        notes.removeAt(position)
        notifyItemRemoved(position)
        checkCurrentPosition()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(
                if (viewType == CURRENT_TYPE) {
                    R.layout.notes_item_current
                } else {
                    R.layout.notes_item
                }, parent, false
            ) as View
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount() = notes.size

    override fun getItemViewType(position: Int): Int {
        return if (currentPosition != RecyclerView.NO_POSITION && currentPosition == position) {
            CURRENT_TYPE
        } else {
            USUAL_TYPE
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = NotesItemBinding.bind(view)

        fun bind(note: Note) = with(binding) {
            itemView.apply {
                notesItemTitle.text = note.title
                notesItemCreated.text = note.created.toString()
                notesItemBody.text = note.body
                notesItemBody.visibility = if (note.isBodyVisible) {
                    View.VISIBLE
                } else {
                    View.GONE
                }

                setOnClickListener {
                    currentPosition = layoutPosition
                    notifyItemChanged(layoutPosition)
                }

                setOnLongClickListener {
                    notes[layoutPosition].isBodyVisible = notes[layoutPosition].isBodyVisible.not()
                    notifyItemChanged(layoutPosition)
                    true
                }
            }
        }

        fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        fun onItemClear() {
            itemView.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    companion object {
        private const val USUAL_TYPE = 0
        private const val CURRENT_TYPE = 1
        private const val MIN_INDEX = 0
    }
}