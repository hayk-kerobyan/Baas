package com.virtussoft.demo.ui.user_add

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.virtussoft.demo.R
import com.virtussoft.demo.model.user.User
import com.virtussoft.demo.ui.BaseFrag
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.frag_add_user.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddUserFrag : BaseFrag() {

    companion object {
        const val KEY_AVATAR_URI = "KEY_AVATAR_URL"
        const val REQUEST_IMAGE_CAPTURE = 1

    }

    private val viewModel: AddUserVm by viewModels()
    private var avatarUri: Uri? = null

    override fun resource() = R.layout.frag_add_user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(requireContext()).load(avatarUri ?: R.raw.user).into(avatarIV)
        if (requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            avatarIV.setOnClickListener {
                dispatchTakePictureIntent()

            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        avatarUri?.let { outState.putString(KEY_AVATAR_URI, it.path) }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let { bundle ->
            val path = bundle.getString(KEY_AVATAR_URI, null)
            avatarUri = path?.let { Uri.parse(it) }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.user_add_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Glide.with(avatarIV.context).load(avatarUri).into(avatarIV)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.confirm -> {
                createUser()
                requireActivity().onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createUser() {
        val user = User(
            id = "",
            databaseId = -1,
            firstName = firstNameET.text.toString(),
            lastName = lastNameET.text.toString(),
            avatarUrl = ""
        )
        viewModel.createUser(user, avatarUri)
    }

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Ensure that there's a camera activity to handle the intent
        takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
            // Create the File where the photo should go
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                // Error occurred while creating the File
                ex.printStackTrace()
                null
            }
            // Continue only if the File was successfully created
            photoFile?.also {
                avatarUri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.virtussoft.demo.fileprovider", it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, avatarUri)
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
}