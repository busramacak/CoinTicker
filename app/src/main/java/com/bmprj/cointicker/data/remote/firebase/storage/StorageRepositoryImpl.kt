package com.bmprj.cointicker.data.remote.firebase.storage

import android.net.Uri
import com.bmprj.cointicker.utils.Constants
import com.bmprj.cointicker.utils.Constants.jpg
import com.google.firebase.storage.FirebaseStorage as UserInfoStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    private val storage:UserInfoStorage
): StorageRepository {
    override suspend fun changePhoto(userID: String, photoUri: Uri): Flow<Boolean> = flow {
        val ref = storage.reference.child(Constants.STORAGE_PATH).child(userID.jpg)
        val task = ref.putFile(photoUri)

        emit(task.isSuccessful)
    }

    override suspend fun getPhoto(userID: String): Flow<Uri> = flow{
        val ref = storage.reference.child(Constants.STORAGE_PATH).child(userID.jpg).downloadUrl.await()

        emit(ref)
    }

    override suspend fun deletePhoto(userID: String): Flow<Void> = flow {


        val photoRef = storage.reference.child(Constants.STORAGE_PATH).child(userID.jpg)

        val ref = photoRef.listAll().await()

        val isPhotoExists = ref.items.any{ it.name == photoRef.name }
        println(isPhotoExists)
        if(isPhotoExists){
            print("isPhotoExistiçi")
            emit(photoRef.delete().await())
        }
    }
}