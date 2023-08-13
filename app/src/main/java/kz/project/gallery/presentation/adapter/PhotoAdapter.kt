package kz.project.gallery.presentation.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import kz.project.domain.model.photo.Photo

class PhotoAdapter : PagingDataAdapter<Photo, PhotoViewHolder>(COMPARATOR) {

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        getItem(position)?.let(holder::bind)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder =
        PhotoViewHolder.create(parent)

    companion object {

        private val COMPARATOR = object : DiffUtil.ItemCallback<Photo>() {

            override fun areItemsTheSame(oldItem: Photo, newItem: Photo) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo) =
                oldItem == newItem
        }
    }
}