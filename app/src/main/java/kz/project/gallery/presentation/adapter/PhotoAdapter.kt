package kz.project.gallery.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import kz.project.domain.model.photo.Photo
import kz.project.gallery.R

class PhotoAdapter(private val photoItemType: PhotoItemType) :
    ListAdapter<Photo, PhotoViewHolder>(DIFF_UTIL) {

    private var onItemClickListener: ((Photo) -> Unit)? = null

    fun setOnItemClickListener(listener: (Photo) -> Unit) {
        onItemClickListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (photoItemType) {
        is PhotoItemType.BigItemTwoColumns -> {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.photo_list_item_big, parent, false)

            PhotoViewHolder(view)
        }

        is PhotoItemType.SmallItemFourColumns -> {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.photo_list_item_small, parent, false)

            PhotoViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        getItem(position)?.let { photo ->
            holder.bind(photo = photo)
            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(photo) }
            }
        }
    }

    companion object {

        val DIFF_UTIL = object : DiffUtil.ItemCallback<Photo>() {

            override fun areItemsTheSame(oldItem: Photo, newItem: Photo) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo) =
                oldItem == newItem
        }
    }
}