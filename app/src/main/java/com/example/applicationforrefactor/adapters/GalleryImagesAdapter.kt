package com.example.applicationforrefactor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationforrefactor.R
import com.example.applicationforrefactor.databinding.ItemGalleryImageBinding
import com.example.applicationforrefactor.extentions.GetGalleryData.loadImage
import com.example.applicationforrefactor.interfaces.GalleryListener
import com.example.applicationforrefactor.models.GalleryImage

class GalleryImagesAdapter(
    private var list: MutableList<GalleryImage>,
    private var selectImages: ArrayList<String>,
    private var listener: GalleryListener
) :
    RecyclerView.Adapter<GalleryImagesAdapter.GalleryImagesViewHolder>() {

    var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryImagesViewHolder {
        val binding: ItemGalleryImageBinding? = DataBindingUtil.bind(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_gallery_image, parent, false)
        )
        context = parent.context

        return GalleryImagesViewHolder(
            binding!!
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: GalleryImagesViewHolder, position: Int) {
        val image = list[position]
        holder.binding.image.loadImage(holder.itemView.context, image.imagePath)

        for (imagePath in selectImages) {
            if (imagePath == image.imagePath)
                image.isSelected = true
        }

        if (image.isSelected!!) {
            holder.binding.selectImage.visibility = View.VISIBLE
        } else {
            holder.binding.selectImage.visibility = View.GONE
        }

        holder.binding.itemView.setOnClickListener { itemViewOnClickListener(image, position) }

        holder.binding.selectImage.setOnClickListener { selectImageOnClickListener(image, position) }
    }


    //region onClickListeners
    private fun itemViewOnClickListener(image: GalleryImage, position: Int){
        val count = 10
        if (selectImages.size < count) {
            notifyItemChanged(image,position)
        }
    }

    private fun selectImageOnClickListener(image: GalleryImage, position: Int){
        notifyItemChanged(image, position)
    }

    private fun notifyItemChanged(image: GalleryImage, position: Int){
        image.isSelected = !image.isSelected!!
        listener.onClickItem(image.isSelected!!, image)
        notifyItemChanged(position)
    }
    //endregion

    //region ViewHolders
    class GalleryImagesViewHolder(
        var binding: ItemGalleryImageBinding
    ) :
        RecyclerView.ViewHolder(binding.root)
    //endregion
}