package kz.project.data.local.entity

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class UserEntity : RealmObject {
    @PrimaryKey
    var id: Int? = null
    var birthday: String? = null
    var email: String? = null
    var enabled: Boolean? = null
    var fullName: String? = null
    var phone: String? = null
    var username: String? = null
}