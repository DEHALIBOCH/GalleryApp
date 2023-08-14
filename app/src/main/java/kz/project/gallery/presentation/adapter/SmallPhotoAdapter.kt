package kz.project.gallery.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import kz.project.domain.model.photo.Photo
import kz.project.gallery.R

// TODO решить, оставлять все как есть или дропать Paging3
// Или и это переделать на paging3
class SmallPhotoAdapter(private val photoItemType: PhotoItemType) :
    ListAdapter<Photo, PhotoViewHolder>(PhotoAdapter.DIFF_UTIL) {

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
}