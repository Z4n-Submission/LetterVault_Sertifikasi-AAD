package com.google.developers.lettervault.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.developers.lettervault.R
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

        val factory = DataViewModelFactory(this)
        addViewModel = ViewModelProviders.of(this, factory).get(AddLetterViewModel::class.java)
        simpleDate = SimpleDateFormat("MMM d Y, h:mm a", Locale.getDefault())

        //tambahkan fungsi untuk memanggil waktu data atau file dibuat
        supportActionBar?.title = getString(R.string.created_title, simpleDate)

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
                //buat fungsi looping ketika addMessage atau content kosong maka tidak dapat menyimpan pesan
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
        //ketika action time diklik, waktu yang diset ditambahkan ke db menggunakan fungsi pada VM
    }

}