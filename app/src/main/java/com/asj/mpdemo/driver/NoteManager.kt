package com.asj.mpdemo.driver

import android.text.TextUtils
import com.asj.mpdemo.model.Note
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import java.util.HashMap

class NoteManager {
    companion object {
        var noteManager: NoteManager? = null

        fun getInstance(): NoteManager? {
            if (noteManager == null) {
                synchronized(NoteManager::class.java) {
                    if (noteManager == null) {
                        noteManager = NoteManager()
                    }
                }
            }

            return noteManager
        }
    }


    fun getNoteFD(): DatabaseReference {
        var mFirebaseInstance = FirebaseDatabase.getInstance()
        return mFirebaseInstance.getReference("notes");
    }

    fun createNote(note: Note, noteId: String?) {
        var mFirebaseDatabase = getNoteFD()

        if (TextUtils.isEmpty(noteId)) {
            var id = mFirebaseDatabase.push().key
            id?.let { mFirebaseDatabase.child(it).setValue(note) }
        } else {
            val map = HashMap<String, Any>()
            map["title"] = note.title
            map["description"] = note.description

            noteId?.let {
                mFirebaseDatabase.child(it).updateChildren(map, null)
            }
        }
    }

    fun deleteNote(id: String?) {
        id?.let {
            var mFirebaseDatabase = getNoteFD()
            mFirebaseDatabase.child(it).removeValue()
        }
    }
}
