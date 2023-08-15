package kz.project.gallery.presentation.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import kz.project.domain.model.photo.Photo
import kz.project.gallery.R
import kz.project.gallery.utils.Constants

class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val photoImageView: ShapeableImageView = itemView.findViewById(R.id.photoImageView)

    fun bind(photo: Photo) {
        val imageName = photo.image.name
        val loadingUrl = "${Constants.IMAGE_LOADING_URL}$imageName"
        Glide.with(photoImageView)
            .load(loadingUrl)
            .placeholder(ColorDrawable(Color.TRANSPARENT))
            .error(AppCompatResources.getDrawable(itemView.context, R.drawable.error_with_loading))
            .fitCenter()
            .into(photoImageView)
    }

}