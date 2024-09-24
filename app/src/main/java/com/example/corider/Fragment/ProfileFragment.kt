package com.example.corider.Fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.corider.R
import com.google.android.material.card.MaterialCardView

class ProfileFragment : Fragment() {

    private lateinit var profileImageView: ImageView
    private val IMAGE_REQUEST_CODE = 1001
    private lateinit var userName: EditText
    private lateinit var aboutYou: EditText
    private lateinit var travelPreferencesSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize views
        profileImageView = view.findViewById(R.id.profileImageView)
        userName = view.findViewById(R.id.userName)
        aboutYou = view.findViewById(R.id.aboutYou)
        travelPreferencesSpinner = view.findViewById(R.id.travelPreferencesSpinner)

        // Set up the spinner
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.travel_preferences,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        travelPreferencesSpinner.adapter = adapter

        // Set onClickListener to the ImageView to upload an image
        profileImageView.setOnClickListener {
            openImagePicker()
        }

        return view
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                profileImageView.setImageURI(uri)  // Set the selected image to ImageView
            }
        }
    }
}
