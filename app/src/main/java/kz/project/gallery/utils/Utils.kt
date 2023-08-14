package kz.project.gallery.utils

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