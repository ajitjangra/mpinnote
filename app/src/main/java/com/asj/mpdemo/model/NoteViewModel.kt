package com.asj.mpdemo.model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.content.Context

import com.asj.mpdemo.driver.NoteRepository

import java.util.ArrayList

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: NoteRepository? = NoteRepository.getRepository()

    var alNote: MutableLiveData<ArrayList<Note>> = MutableLiveData()
    var note: MutableLiveData<Note> = MutableLiveData()

    init {
        repository?.start(alNote!!)
    }

    fun getOpenedNote(id: String?) {
        repository?.getSelectedNote(id, note)
    }
}
