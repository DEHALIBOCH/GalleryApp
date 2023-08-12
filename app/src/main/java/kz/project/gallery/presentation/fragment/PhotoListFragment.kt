package kz.project.gallery.presentation.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentPhotoListBinding


class PhotoListFragment : Fragment(R.layout.fragment_photo_list) {

    private val binding: FragmentPhotoListBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val popular = arguments?.getBoolean(POPULAR) ?: false
        val new = arguments?.getBoolean(NEW) ?: false

        // TODO убрать, начало работы над PhotoRepository и UserRepository
        if (popular) binding.testTextView.text = POPULAR
        if (new) binding.testTextView.text = NEW
    }

    companion object {
        const val FRAGMENT_TAG = "PhotoListFragment"
        const val POPULAR = "Popular"
        const val NEW = "New"
    }
}