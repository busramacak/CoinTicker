package com.bmprj.cointicker.data.remote.firebase.storage

import android.net.Uri
import com.bmprj.cointicker.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val storage:FirebaseStorage
): StorageRepository {
    override suspend fun changePhoto(userID: String, photoUri: Uri): Flow<Boolean> = flow {
        val ref = storage.reference.child(Constants.STORAGE_PATH).child(userID+".jpg")
        val task = ref.putFile(photoUri)

        emit(task.isSuccessful)
    }

    override suspend fun getPhoto(userID: String): Flow<Uri> = flow{
        val ref = storage.reference.child(Constants.STORAGE_PATH).child(userID+".jpg").downloadUrl.await()

        emit(ref)
    }
}