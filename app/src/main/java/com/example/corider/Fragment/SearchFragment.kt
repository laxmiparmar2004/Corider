package com.example.corider.Fragment

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.corider.R
import java.util.*

class SearchFragment : Fragment() {
    private lateinit var dateTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        dateTextView = view.findViewById(R.id.textView10)
        dateTextView.setOnClickListener { openDatePicker() }

        return view
    }

    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                // Set the selected date to the TextView
                dateTextView.text = getString(R.string.date_format, selectedDay, selectedMonth + 1, selectedYear)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    companion object {
        // Add any static methods or properties if needed
    }
}
