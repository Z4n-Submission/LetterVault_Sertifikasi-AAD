package com.google.developers.lettervault.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.google.developers.lettervault.R
import com.google.developers.lettervault.data.Letter
import com.google.developers.lettervault.util.Event
import com.google.developers.lettervault.util.LETTER_ID
import kotlinx.android.synthetic.main.activity_letter_detail.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Display a unlocked letter or a lock if letter is still in vault.
 */
class LetterDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: LetterDetailViewModel
    private lateinit var simpleDate: SimpleDateFormat
    private var shareIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_letter_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = LetterDetailViewModelFactory(this, intent.getLongExtra(LETTER_ID, 0L))
        viewModel = ViewModelProviders.of(this, factory).get(LetterDetailViewModel::class.java)
        simpleDate = SimpleDateFormat("MMM d Y, h:mm a", Locale.getDefault())

        shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_locked))
        }

        viewModel.letter.observe(this, Observer(this::init))
        viewModel.canOpen.observe(this, Observer(this::runEvent))
    }

    private fun runEvent(eventLetter: Event<Letter>) {
        val letter = eventLetter.getContentIfNotHandled() ?: return
        if (letter.expires > System.currentTimeMillis()) {
            Snackbar.make(
                toolbar,
                getString(R.string.cannot_open, simpleDate.format(letter.expires)),
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Attach letter to the view and update title and share intent.
     */
    private fun init(letter: Letter?) {
        if (letter == null) return
        if (letter.expires > System.currentTimeMillis()) {
            viewModel.tryOpening(letter)
            lock.visibility = View.VISIBLE
            supportActionBar?.title =
                getString(R.string.title_opening, simpleDate.format(letter.expires))
            return
        } else {
            lock.visibility = View.GONE
        }

        viewModel.open(letter)

        supportActionBar?.title = getString(R.string.title_opened, simpleDate.format(letter.opened))
        shareIntent?.putExtra(Intent.EXTRA_TEXT, letter.content)

        subject.text = letter.subject
        content.text = letter.content

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                startActivity(shareIntent)
            }
            R.id.action_delete -> {
                AlertDialog.Builder(this).apply {
                    setMessage(getString(R.string.alert_delete_message))
                    setNegativeButton(getString(R.string.alert_delete_negative), null)
                    setPositiveButton(getString(R.string.alert_delete_positive)) { _, _ ->
                        viewModel.delete()
                        finish()
                    }
                    show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
