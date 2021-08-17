package com.google.developers.lettervault.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.developers.lettervault.R
import com.google.developers.lettervault.data.Letter
import com.google.developers.lettervault.ui.add.AddLetterActivity
import com.google.developers.lettervault.ui.detail.LetterDetailActivity
import com.google.developers.lettervault.ui.list.ListActivity
import com.google.developers.lettervault.ui.setting.SettingActivity
import com.google.developers.lettervault.util.DataViewModelFactory
import com.google.developers.lettervault.util.LETTER_ID
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*

class HomeActivity : AppCompatActivity() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        val factory = DataViewModelFactory(this)
        homeViewModel = ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
        homeViewModel.setRecentLetter()
        homeViewModel.getRecentLetter().observe(this, {
            showRecent(it)
        })

    }

    /**
     * Display letter data in the view.
     */
    private fun showRecent(letter: Letter?) {
        if (letter == null) {
            content.text = getString(R.string.no_letter)
            lock.visibility = View.INVISIBLE
            return
        }
        lock.visibility = View.VISIBLE
        content.text = letter.content

        if (letter.expires < System.currentTimeMillis() && letter.opened != 0L) {
            lock.setImageDrawable(getDrawable(R.drawable.ic_lock_open))
        } else {
            lock.setImageDrawable(getDrawable(R.drawable.ic_lock))
        }

        letterView.setOnClickListener {
            val intent = Intent(this, LetterDetailActivity::class.java)
            intent.putExtra(LETTER_ID, letter.id)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_add -> {
                val addIntent = Intent(this, AddLetterActivity::class.java)
                startActivity(addIntent)
                true
            }
            R.id.action_list -> {
                val listIntent = Intent(this, ListActivity::class.java)
                startActivity(listIntent)
                true
            }
            R.id.action_settings -> {
                val settingIntent = Intent(this, SettingActivity::class.java)
                startActivity(settingIntent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}