package kz.project.gallery.utils

import android.widget.AbsListView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class RecyclerViewScrollListener(private val getPagingData: () -> Unit) : RecyclerView.OnScrollListener() {

    private var isScrolling = false

    abstract fun isLoading(): Boolean
    abstract fun isLastPage(): Boolean

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            isScrolling = true
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager as GridLayoutManager
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val visibleItemsCount = layoutManager.childCount
        val totalItemsCount = layoutManager.itemCount

        val isNotLoadingAndNotLastPage = !isLoading() && !isLastPage()
        val isAtLastItem = firstVisibleItemPosition + visibleItemsCount >= totalItemsCount - 2
        val isNotAtBeginning = firstVisibleItemPosition >= 0
        val isTotalMoreThanVisible = totalItemsCount >= Constants.LIMIT
        val shouldPaginate =
            isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

        if (shouldPaginate) {
            getPagingData.invoke()
            isScrolling = false
        }
    }
}