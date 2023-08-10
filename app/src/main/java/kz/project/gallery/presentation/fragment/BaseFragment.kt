package kz.project.gallery.presentation.fragment

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

    @Inject
    lateinit var factory: MultiViewModelFactory
    protected val viewModel: ViewModel by viewModels { factory }

}