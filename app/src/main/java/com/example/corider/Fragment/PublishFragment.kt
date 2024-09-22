package com.example.corider.Fragment

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.corider.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import java.text.SimpleDateFormat
import java.util.*

class PublishFragment : Fragment() {
    private lateinit var mapView: MapView
    private lateinit var fromField: EditText
    private lateinit var toField: EditText
    private lateinit var dateField: EditText
    private lateinit var timeField: EditText
    private lateinit var submitButton: Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_publish, container, false)

        // Initialize osmdroid configuration
        val context = requireContext()
        Configuration.getInstance().load(context, androidx.preference.PreferenceManager.getDefaultSharedPreferences(context))

        // Initialize map view
        mapView = view.findViewById(R.id.map)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setBuiltInZoomControls(true)
        mapView.setMultiTouchControls(true)

        // Set a default location (Eiffel Tower)
        val startPoint = GeoPoint(48.8588443, 2.2943506)
        mapView.controller.setZoom(15.0)
        mapView.controller.setCenter(startPoint)

        // Initialize UI elements
        fromField = view.findViewById(R.id.fromField)
        toField = view.findViewById(R.id.toField)
        dateField = view.findViewById(R.id.dateField)
        timeField = view.findViewById(R.id.timeField)
        submitButton = view.findViewById(R.id.submitButton)

        // Get location services client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Set up location listener
        getCurrentLocation()

        // Set up DatePickerDialog for date selection
        dateField.setOnClickListener { showDatePicker() }

        // Set up TimePickerDialog for time selection
        timeField.setOnClickListener { showTimePicker() }

        // Set a click listener for the submit button
        submitButton.setOnClickListener { handleSubmit() }

        return view
    }

    private fun showDatePicker() {
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                calendar.set(year, month, day)
                val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                dateField.setText(format.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {
        TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                val format = SimpleDateFormat("HH:mm", Locale.US)
                timeField.setText(format.format(calendar.time))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun handleSubmit() {
        val toLocation = toField.text.toString()
        val date = dateField.text.toString()
        val time = timeField.text.toString()

        if (toLocation.isNotEmpty() && date.isNotEmpty() && time.isNotEmpty()) {
            Toast.makeText(context, "From: ${fromField.text}\nTo: $toLocation\nDate: $date\nTime: $time", Toast.LENGTH_LONG).show()
            // Handle the input data here (e.g., save to database)
        } else {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val currentLat = it.latitude
                val currentLong = it.longitude
                fromField.setText("Lat: $currentLat, Long: $currentLong")
                mapView.controller.setCenter(GeoPoint(currentLat, currentLong))
            } ?: run {
                Toast.makeText(context, "Unable to fetch current location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getCurrentLocation() // Retry getting location
            } else {
                Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
