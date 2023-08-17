package kz.project.gallery.presentation.fragment.base_fragmnents

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import kz.project.gallery.presentation.viewmodel.MultiViewModelFactory
import javax.inject.Inject

/**
 * Базовый класс для фрагментов использующих делегат by viewModels.
 * Необходимо обязательно переопределить onCreate и проинжектить там factory.
 */
open class BaseFragment<ViewModel>(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {

    open lateinit var factory: MultiViewModelFactory
    protected val viewModel: ViewModel by viewModels { factory }

}