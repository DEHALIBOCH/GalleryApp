package kz.project.gallery.presentation.adapter

sealed class PhotoItemType {

    object BigItemTwoColumns : PhotoItemType()

    object SmallItemFourColumns : PhotoItemType()
}