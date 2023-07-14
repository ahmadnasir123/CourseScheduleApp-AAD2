package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var viewModel: AddCourseViewModel

    private lateinit var edCourseName: TextInputEditText
    private lateinit var edLecturer: TextInputEditText
    private lateinit var edNote: TextInputEditText
    private lateinit var spDay: Spinner

    private var startTime = "00:00"
    private var endTime = "00:00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = AddCourseViewModelFactory.createFactory(this)
        viewModel = ViewModelProvider(this, factory)[AddCourseViewModel::class.java]

        edCourseName = findViewById(R.id.ed_course_name)
        edLecturer = findViewById(R.id.ed_lecturer)
        edNote = findViewById(R.id.ed_note)
        spDay = findViewById(R.id.spinner_day)

        findViewById<ImageButton>(R.id.ib_start_time).setOnClickListener {
            val dialogFragment = TimePickerFragment()
            dialogFragment.show(supportFragmentManager, "startTime")
        }

        findViewById<ImageButton>(R.id.ib_end_time).setOnClickListener {
            val dialogFragment = TimePickerFragment()
            dialogFragment.show(supportFragmentManager, "endTime")
        }

        viewModel.saved.observe(this) {
            if (it.getContentIfNotHandled() == true) finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                val courseName = edCourseName.text.toString()
                val lecturer = edLecturer.text.toString()
                val note = edNote.text.toString()
                val day = spDay.selectedItemPosition
                viewModel.insertCourse(courseName, day, startTime, endTime, lecturer, note)
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
            .apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }

        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val formattedDate = formatter.format(calendar.time)

        when (tag) {
            "endTime" -> {
                endTime = formattedDate
            }
            "startTime" -> {
                startTime = formattedDate
            }
        }
        findViewById<TextView>(R.id.tv_start_time).text = startTime
        findViewById<TextView>(R.id.tv_end_time).text = endTime
    }
}