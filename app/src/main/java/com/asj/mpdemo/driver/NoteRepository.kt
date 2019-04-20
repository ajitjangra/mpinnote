package com.asj.mpdemo.driver

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import android.view.View
import com.asj.mpdemo.model.Note
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.note_list_activity.*
import java.util.ArrayList


class NoteRepository {
    companion object {
        var noteRepository: NoteRepository? = null

        fun getRepository(): NoteRepository? {
            if (noteRepository == null) {
                synchronized(NoteRepository::class.java) {
                    if (noteRepository == null) {
                        noteRepository = NoteRepository()
                    }
                }
            }

            return noteRepository
        }
    }

    fun start(alNote: MutableLiveData<ArrayList<Note>>) {
        var mFirebaseDatabase = NoteManager.getInstance()?.getNoteFD()
        mFirebaseDatabase?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot != null) {
                    val arrayList = java.util.ArrayList<Note>()
                    for (postSnapshot in dataSnapshot.children) {
                        try {
                            val key = postSnapshot.key
                            val note = postSnapshot.getValue<Note>(Note::class.java!!)
                            if (note != null) {
                                note.id = key
                                arrayList.add(note)
                            }
                        } catch (e: Exception) {
                            Log.d("NoteManager", e.message)
                        }
                    }

                    alNote.postValue(arrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun getSelectedNote(id: String?, note: MutableLiveData<Note>) {

    }
}
