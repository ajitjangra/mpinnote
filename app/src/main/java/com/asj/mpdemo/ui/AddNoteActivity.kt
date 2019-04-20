package com.asj.mpdemo.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem

import com.asj.mpdemo.R
import com.asj.mpdemo.driver.NoteManager
import com.asj.mpdemo.model.Note
import com.asj.mpdemo.model.NoteViewModel
import com.asj.mpdemo.ui.AddNoteActivity.ActivityBuilder.Companion.NOTE_ID
import com.asj.mpdemo.util.Utility
import kotlinx.android.synthetic.main.add_note_activity.*


class AddNoteActivity : BaseActivity() {

    class ActivityBuilder private constructor(noteId: String?) {
        private val mExtras: Bundle = Bundle()

        init {
            mExtras.putString(NOTE_ID, noteId)
        }

        fun build(ctx: Context): Intent {
            val i = Intent(ctx, AddNoteActivity::class.java)
            i.putExtras(mExtras)
            return i
        }

        companion object {
            var NOTE_ID = "note_id"

            fun getBuilder(noteId: String?): ActivityBuilder {
                return ActivityBuilder(noteId)
            }
        }
    }

    private var noteId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_note_activity)
        initActionBar()

        if (intent.hasExtra(NOTE_ID)) {
            noteId = intent.getStringExtra(NOTE_ID)
        }

        updateNote()
    }

    private fun initActionBar() {
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = getString(R.string.add_note_ab_title)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
    }

    private fun updateNote() {
        if (!TextUtils.isEmpty(noteId)) {
            var model = ViewModelProviders.of(this).get(NoteViewModel::class.java)
            model.alNote.observe(this, Observer<ArrayList<Note>> { list ->
                if (list != null) {
                    for (note in list) {
                        if (note != null && !TextUtils.isEmpty(note.id) && note.id.equals(noteId)) {
                            etTitle.setText(note.title)
                            etDesc.setText(note.description)
                        }
                    }
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menuParam: Menu): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menuParam)
        return super.onCreateOptionsMenu(menuParam)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        super.onPrepareOptionsMenu(menu)
        val itemSave = menu.findItem(R.id.item_save)
        val itemDelete = menu.findItem(R.id.item_delete)

        itemDelete.isVisible = !TextUtils.isEmpty(noteId)

        itemSave.icon = ContextCompat.getDrawable(this@AddNoteActivity, R.drawable.ic_save_black_24dp)
        itemDelete.icon = ContextCompat.getDrawable(this@AddNoteActivity, R.drawable.ic_delete_forever_black_24dp)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when {
            item.itemId == android.R.id.home -> finish()
            item.itemId == R.id.item_save -> saveNote()
            item.itemId == R.id.item_delete -> deleteNote()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun saveNote() {
        when {
            TextUtils.isEmpty(etTitle.text.toString()) -> Utility.showToast(this@AddNoteActivity, getString(R.string.add_note_title_empty))
            TextUtils.isEmpty(etDesc.text.toString()) -> Utility.showToast(this@AddNoteActivity, getString(R.string.add_note_desc_empty))
            else -> {
                var note = Note()
                note.title = etTitle.text.toString()
                note.description = etDesc.text.toString()
                NoteManager.getInstance()?.createNote(note, noteId)

                Utility.showToast(this@AddNoteActivity, getString(R.string.add_note_saved_success))
                finish()
            }
        }
    }

    private fun deleteNote() {
        NoteManager.getInstance()?.deleteNote(noteId)
        Utility.showToast(this@AddNoteActivity, getString(R.string.add_note_deleted_success))
        finish()
    }
}
