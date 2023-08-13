package kz.project.domain.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kz.project.domain.model.user.UpdatePasswordForm
import kz.project.domain.model.user.User

interface UserRepository {

    fun getCurrentUser(): Single<User>

    fun updatePassword(id: Int, updatePasswordForm: UpdatePasswordForm): Single<User>

    fun getUserById(id: Int): Single<User>

    fun deleteUser(currUserId: Int): Completable
}