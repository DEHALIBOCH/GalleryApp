package kz.project.gallery.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.viewbinding.library.fragment.viewBinding
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kz.project.gallery.R
import kz.project.gallery.databinding.FragmentHomeBinding
import kz.project.gallery.presentation.adapter.ViewPagerAdapterPhotosList
import kz.project.gallery.utils.Constants
import kz.project.gallery.utils.SearchQueryEventBus
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()
    private lateinit var disposable: Disposable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchView()
        setupViewPager()
    }

    /**
     * Настраивает ViewPager
     */
    private fun setupViewPager() = binding.apply {

        viewPager.adapter = ViewPagerAdapterPhotosList(childFragmentManager, lifecycle)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> requireContext().getString(R.string.tab_new)
                1 -> requireContext().getString(R.string.tab_popular)
                else -> throw IllegalArgumentException()
            }
        }.attach()
    }

    /**
     * Настраивает searchView
     */
    private fun setupSearchView() = binding.apply {
//        TODO move subscribe to viewModel
        disposable = searchEditText.textChanges()
            .debounce(300, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { query ->
                    if (query.isNotBlank()) SearchQueryEventBus.sendSearchText(query.toString())
                },
                { error ->
                    Log.e(Constants.SEARCH_QUERY_ERROR, error.message ?: Constants.UNEXPECTED_ERROR)
                }
            )
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard(searchEditText, requireContext())
                true
            } else {
                false
            }
        }
    }

    private fun hideKeyboard(editText: EditText, context: Context) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.dispose()
    }

    companion object {
        const val FRAGMENT_TAG = "HomeFragment"
    }
}