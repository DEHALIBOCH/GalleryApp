package kz.project.gallery.presentation.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kz.project.domain.model.photo.Photo
import kz.project.gallery.databinding.PhotoListItemBinding
import kz.project.gallery.utils.Constants

class PhotoViewHolder(private val binding: PhotoListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(photo: Photo) {
        val imageName = photo.image.name
        val loadingUrl = "${Constants.IMAGE_LOADING_URL}$imageName"
        Glide.with(binding.photoImageView)
            .load(loadingUrl)
            .placeholder(ColorDrawable(Color.TRANSPARENT))
            .fitCenter()
            .into(binding.photoImageView)
    }

    companion object {

        fun create(parent: ViewGroup): PhotoViewHolder {

            val binding = PhotoListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return PhotoViewHolder(binding)
        }
    }
}