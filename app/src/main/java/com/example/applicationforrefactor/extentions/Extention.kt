package com.example.applicationforrefactor.extentions

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.transition.Transition
import com.example.applicationforrefactor.models.GalleryImage


object GetGalleryData {
    @SuppressLint("Recycle")
    @RequiresApi(Build.VERSION_CODES.Q)
    fun listOfImages(context : Context) :ArrayList<GalleryImage> {
        val listOfAllImages : ArrayList<GalleryImage> = ArrayList()
        var absolutePathOfImage : String
        val uri : Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val projection = arrayOf(MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val orderBy : String = MediaStore.Images.Media.DATE_TAKEN
        val cursor = context.contentResolver.query(uri, projection, null, null, "$orderBy DESC")!!
        val columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)

        while (cursor.moveToNext()){
            absolutePathOfImage = cursor.getString(columnIndexData)
            listOfAllImages.add(
                GalleryImage(
                    absolutePathOfImage,
                    false
                )
            )
        }

        return listOfAllImages
    }

    fun ImageView.loadImage(context: Context, url: String?, placeholder: Int = 0) {
        val imageView = this
        Glide.with(context)
            .asBitmap()
            .centerCrop()
            .placeholder(placeholder)
            .load(url)
            .apply(
                RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).priority(Priority.HIGH)
            )
            .into(object : BitmapImageViewTarget(this) {
                override fun onResourceReady(bitmap: Bitmap, transition: Transition<in Bitmap>?) {
                    val viewWidthToBitmapWidthRatio =
                        imageView.width.toDouble() / bitmap.width.toDouble()
                    imageView.layoutParams.height =
                        (bitmap.height * viewWidthToBitmapWidthRatio).toInt()
                    imageView.setImageBitmap(bitmap)
                }
            })
    }
}