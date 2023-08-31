package com.bmprj.cointicker.data.remote.firebase

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface StorageRepository {

    suspend fun changePhoto(userID: String,photoUri: Uri): Flow<Boolean>

    suspend fun getPhoto(userID: String):Flow<Uri>
}