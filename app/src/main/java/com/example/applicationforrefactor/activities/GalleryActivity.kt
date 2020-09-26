package com.example.applicationforrefactor.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.applicationforrefactor.R
import com.example.applicationforrefactor.databinding.ActivityGalleryBinding
import com.google.gson.Gson
import com.example.applicationforrefactor.adapters.GalleryImagesAdapter
import com.example.applicationforrefactor.extentions.GetGalleryData.listOfImages
import com.example.applicationforrefactor.interfaces.GalleryListener
import com.example.applicationforrefactor.models.GalleryImage

class GalleryActivity : AppCompatActivity(), GalleryListener {
    private val PERMISSION_REQUEST_CODE = 100
    private lateinit var binding: ActivityGalleryBinding
    private lateinit var images: ArrayList<GalleryImage>
    private var selectImages = ArrayList<String>()

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_gallery
        )

        checkExternalStoragePermission()

        binding.closeButton.setOnClickListener { onBackPressed() }

        binding.doneButton.setOnClickListener { buttonDoneOnClickListener() }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun loadImages() {
        binding.galleryRecycler.setHasFixedSize(true)
        binding.galleryRecycler.layoutManager = GridLayoutManager(this, 4)
        images = listOfImages(this)
        binding.galleryRecycler.adapter =
            GalleryImagesAdapter(
                images,
                selectImages,
                this
            )
        setImageState()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImages()
            } else {
                Toast.makeText(this, "", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onClickItem(isSelected: Boolean, galleryImage: GalleryImage) {
        if (isSelected) {
            selectImages.add(galleryImage.imagePath!!)
        } else {
            selectImages.remove(galleryImage.imagePath)
        }
        setImageState()
    }

    private fun setImageState() {
        var state = "${selectImages.size} фото"

        if (!selectImages.isNullOrEmpty())  {
            state = getString(R.string.you_are_selected_image)
        }

        binding.imageSelectCount.text = state
    }

    //region OnClickListeners
    private fun buttonDoneOnClickListener(){
        val intent = Intent()
        intent.putExtra("images", Gson().toJson(selectImages))
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
    //endregion

    //region Permissions
    @RequiresApi(Build.VERSION_CODES.Q)
    private fun checkExternalStoragePermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        } else {
            loadImages()
        }
    }
    //endregion

}