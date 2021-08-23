package com.google.developers.lettervault.ui.add

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.developers.lettervault.R
import com.google.developers.lettervault.ui.home.HomeActivity
import com.google.developers.lettervault.util.DataViewModelFactory
import com.google.developers.lettervault.util.TimePickerDialog
import kotlinx.android.synthetic.main.activity_letter_detail.*
import java.text.SimpleDateFormat
import java.util.*

class AddLetterActivity : AppCompatActivity(), TimePickerDialog.DialogTimeListener {

    private lateinit var addViewModel: AddLetterViewModel
    private lateinit var simpleDate: SimpleDateFormat
    private lateinit var subject: EditText
    private lateinit var addMessage: EditText
    private lateinit var close: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_letter)
        setSupportActionBar(toolbar)

        subject = findViewById(R.id.subjectText)
        addMessage = findViewById(R.id.addMessageText)
        close = findViewById(R.id.close_btn)
        close.setOnClickListener {
            val closeIntent = Intent(this, HomeActivity::class.java)
            startActivity(closeIntent)
        }

        val factory = DataViewModelFactory(this)
        addViewModel = ViewModelProviders.of(this, factory).get(AddLetterViewModel::class.java)
        simpleDate = SimpleDateFormat("MMM d Y, h:mm a", Locale.getDefault())
        supportActionBar?.title = getString(R.string.created_title, simpleDate.format(addViewModel.created))

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                val addSubject = subject.text.toString()
                val addMessage = addMessage.text.toString()
                if (addMessage.isEmpty()){
                    Toast.makeText(this, "Please fill the message", Toast.LENGTH_SHORT).show()
                } else {
                    addViewModel.save(addSubject, addMessage, applicationContext)
                    addViewModel.saved
                    Toast.makeText(this, "Message is saved", Toast.LENGTH_SHORT).show()
                    val finishIntent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(finishIntent)
                }
                true
            }
            R.id.action_time -> {
                val dialogFragment = TimePickerDialog()
                dialogFragment.show(supportFragmentManager, "timePicker")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDialogTimeSet(tag: String?, hourOfDay: Int, minute: Int) {
        addViewModel.setExpirationTime(hourOfDay, minute)
    }

}