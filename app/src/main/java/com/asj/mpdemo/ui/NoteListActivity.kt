package com.asj.mpdemo.ui

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager

import com.asj.mpdemo.R
import com.asj.mpdemo.model.Note
import com.asj.mpdemo.ui.adapter.NoteListAdapter
import kotlinx.android.synthetic.main.note_list_activity.*
import android.arch.lifecycle.ViewModelProviders
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.asj.mpdemo.driver.NoteManager
import com.asj.mpdemo.model.NoteViewModel
import java.util.*
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import java.util.concurrent.TimeUnit

class NoteListActivity : BaseActivity() {

    private val alNote: ArrayList<Note> = ArrayList()
    private val alTemp: ArrayList<Note> = ArrayList()
    private lateinit var adapter: NoteListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_list_activity)

        initAdapter()
        initFAB()
        initSearch()
        var model = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        model.alNote.observe(this, Observer<ArrayList<Note>> { list ->
            updateNoteList(list)
        })
    }

    private fun initSearch() {
        Observable.create(ObservableOnSubscribe<String> { subscriber ->
            etSearchNote.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                }

                override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    subscriber.onNext(query.toString())
                }
            })
        })
                .map { text -> text.toLowerCase().trim() }
                .debounce(100, TimeUnit.MILLISECONDS)
                .subscribe { query ->
                    if (adapter != null) {
                        filter(query)
                    }
                }
    }

    private fun initAdapter() {
        val mCallBack = object : NoteListAdapter.NoteListAdapterCallBack {
            override fun deleteNote(id: String?) {
                NoteManager.getInstance()?.deleteNote(id)
            }

            override fun noteClick(noteId: String?) {
                launchAddNote(noteId)
            }
        }

        adapter = NoteListAdapter(this@NoteListActivity, alNote, mCallBack);
        rvNote.adapter = adapter
        rvNote.layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
    }

    private fun initFAB() {
        fabAddNote.setImageDrawable(ContextCompat.getDrawable(this@NoteListActivity, R.drawable.ic_note_add_black_24dp));
        fabAddNote.setOnClickListener { launchAddNote(null) }
    }

    private fun updateNoteList(list: ArrayList<Note>?) {
        if (list != null) {
            if (list?.size > 0) {
                rvNote.visibility = View.VISIBLE
                tvNoNote.visibility = View.GONE
                alNote.clear()
                alTemp.clear()

                list?.let {
                    alNote.addAll(it)
                    alTemp.addAll(it)
                }

                adapter.notifyDataSetChanged()
            } else {
                rvNote.visibility = View.GONE
                tvNoNote.visibility = View.VISIBLE
            }
        }
    }

    fun filter(query: String?) {
        alNote.clear()
        if (TextUtils.isEmpty(query)) {
            alNote.clear()
            alNote.addAll(alTemp)
        } else {

            for (note in alTemp) {
                if (note != null && !TextUtils.isEmpty(note.description) && note.description.contains(query!!)) {
                    alNote.add(note)
                }
            }
        }

        runOnUiThread(Runnable { adapter.notifyDataSetChanged() })
    }

    private fun launchAddNote(noteId: String?) {
        startActivity(AddNoteActivity.ActivityBuilder.getBuilder(noteId).build(this@NoteListActivity))
    }
}
