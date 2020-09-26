package com.example.applicationforrefactor.interfaces

import com.example.applicationforrefactor.models.GalleryImage

interface GalleryListener {
    fun onClickItem(isSelected: Boolean, galleryImage: GalleryImage)
}