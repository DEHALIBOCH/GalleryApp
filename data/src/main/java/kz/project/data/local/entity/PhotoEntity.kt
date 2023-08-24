package kz.project.data.local.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class PhotoEntity : RealmObject {
    @PrimaryKey
    var id: Int? = null
    var dateCreate: String? = null
    var description: String? = null
    var imageName: String? = null
    var imageId: Int? = null
    var name: String? = null
    var isNew: Boolean? = null
    var isPopular: Boolean? = null
    var user: String? = null
}