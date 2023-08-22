package kz.project.gallery.presentation.fragment.base_fragmnents

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kz.project.domain.model.photo.Photo
import kz.project.gallery.R
import kz.project.gallery.presentation.adapter.PhotoAdapter
import kz.project.gallery.presentation.adapter.PhotoItemType
import kz.project.gallery.presentation.fragment.PhotoDetailsFragment
import kz.project.gallery.utils.RecyclerViewScrollListener
import kz.project.gallery.utils.createCircularProgressDrawable

abstract class PagingPhotoFragment<Binding : ViewBinding>(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    private var _binding: Binding? = null
    protected val binding: Binding
        get() = _binding!!

    abstract fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /** Адаптер для RecyclerView */
    protected lateinit var photoAdapter: PhotoAdapter

    /** Необходима для пагинации, необходимо обязательно менять ее значения при загрузке, получении фото или ошибки */
    protected var isLoading = false

    /** Необходима для пагинации, при успешном получении фото необходимо обязательно обновлять значение */
    protected var isLastPage = false

    /** Настраивает прогресс бар */
    protected fun setupProgressBar(progressBar: ProgressBar, @ColorRes colorId: Int, context: Context) {
        progressBar.indeterminateDrawable = createCircularProgressDrawable(context, colorId)
    }

    /** Настраивает RecyclerView */
    protected fun setupRecyclerView(
        context: Context,
        photoItemType: PhotoItemType,
        spanCount: Int,
        recyclerView: RecyclerView,
    ) {
        photoAdapter = PhotoAdapter(photoItemType)
        photoAdapter.setOnItemClickListener { photo ->
            goToPhotoDetailsFragment(photo)
        }
        recyclerView.apply {
            layoutManager = GridLayoutManager(context, spanCount)
            adapter = photoAdapter
            addOnScrollListener(recyclerViewScrollListener)
        }
    }

    /** Коллбэк для загрузки дополнительных фото при достижении определенного уровня скролла */
    abstract val getPagingData: () -> Unit

    /** Скролл листенер для recyclerView */
    private val recyclerViewScrollListener by lazy {
        object : RecyclerViewScrollListener(
            getPagingData
        ) {
            override fun isLoading(): Boolean = isLoading

            override fun isLastPage(): Boolean = isLastPage
        }
    }

    private fun goToPhotoDetailsFragment(photo: Photo) = requireActivity().supportFragmentManager.commit {
        setReorderingAllowed(true)
        replace<PhotoDetailsFragment>(
            R.id.mainActivityFragmentContainerView,
            PhotoDetailsFragment.FRAGMENT_TAG,
            bundleOf(PhotoDetailsFragment.PHOTO_TAG to photo)
        )
        addToBackStack(null)
    }
}