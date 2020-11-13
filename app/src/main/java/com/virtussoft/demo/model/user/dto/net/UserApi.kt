package com.virtussoft.demo.model.user.dto.net

import android.content.Context
import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.virtussoft.demo.model.user.User
import com.virtussoft.demo.model.user.dto.toRemoteDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserApi @Inject constructor(
    private val firestore: FirebaseFirestore,
    @ApplicationContext private val context: Context
) {

    companion object {
        const val COLLECTION_NAME = "users"
    }

    suspend fun createUser(user: User, avatar: Uri?): UserNet {
        val newUserRef = firestore.collection(COLLECTION_NAME).document()

//        val avatarUrl = avatar?.let {
//                FirebaseStorage
//                    .getInstance()
//                    .reference
//                    .child(newUserRef.id)
//                    .putFile(avatar)
//                    .await()
//                    .storage
//                    .downloadUrl
//                    .await()
//                    .toString()
//        }
        val userNet = user.toRemoteDto(newUserRef.id, "")
        newUserRef.set(userNet).await()
        return userNet
    }

    internal suspend fun getUser(id: Long) = firestore
        .collection(COLLECTION_NAME)
        .document(id.toString())
        .get()
        .await()
        .toObject(UserNet::class.java)


    internal suspend fun getUsers(since: String, count: Int) = firestore
        .collection(COLLECTION_NAME)
        .orderBy(UserNet::id.name)
        .limit(count.toLong())
        .startAfter(since)
        .get()
        .await()
        .toObjects(UserNet::class.java)
}