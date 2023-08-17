package kz.project.gallery.utils

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import kz.project.gallery.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Парсит дату из той что прилетает с api в дату для отображения
 */
fun parseDate(dateCreate: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val date: Date = inputFormat.parse(dateCreate) ?: Date()

    return outputFormat.format(date)
}

/**
 * Парсит текущие миллисекунды в дату по типу 12.12.2012
 */
fun parseDate(timestamp: Long): String {
    val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    val date = Date(timestamp)
    return dateFormat.format(date)
}

/**
 * Настраивает progressBar под дизайн
 */
fun createCircularProgressDrawable(context: Context, @ColorRes colorId: Int) =
    CircularProgressDrawable(context).apply {
        strokeWidth = context.resources.getDimensionPixelSize(R.dimen.progress_bar_stroke_width).toFloat()
        centerRadius = context.resources.getDimensionPixelSize(R.dimen.progress_bar_radius).toFloat()
        setColorSchemeColors(context.getColor(colorId))
    }