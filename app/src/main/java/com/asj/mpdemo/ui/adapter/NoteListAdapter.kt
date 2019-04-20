package com.asj.mpdemo.ui.adapter

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.asj.mpdemo.R
import com.asj.mpdemo.model.Note
import kotlinx.android.synthetic.main.note_list_item.view.*

class NoteListAdapter(private val mContext: Activity, private var alNote: ArrayList<Note>, private val mCallBack: NoteListAdapterCallBack) : RecyclerView.Adapter<NoteListAdapter.NoteViewHolder>() {

    interface NoteListAdapterCallBack {
        fun noteClick(noteId: String?)
        fun deleteNote(id: String?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, intViewType: Int): NoteViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return NoteViewHolder(layoutInflater.inflate(R.layout.note_list_item, parent, false))
    }

    override fun onBindViewHolder(viewHolder: NoteViewHolder, position: Int) {
        val note = alNote[position]

        if (note != null) {
            viewHolder.noteView.tvTitle.text = note.title
            viewHolder.noteView.tvDesc.text = note.description

            viewHolder.noteView.ivDelete.setOnClickListener(View.OnClickListener { if (mCallBack != null) mCallBack.deleteNote(note.id) })
            viewHolder.noteView.clMain.setOnClickListener(View.OnClickListener { if (mCallBack != null) mCallBack.noteClick(note.id) })
        }
    }

    override fun getItemCount(): Int = alNote.size

    class NoteViewHolder(val noteView: View) : RecyclerView.ViewHolder(noteView)
}
