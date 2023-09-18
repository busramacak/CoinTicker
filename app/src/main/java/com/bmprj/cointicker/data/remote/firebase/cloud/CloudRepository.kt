package com.bmprj.cointicker.data.remote.firebase.cloud

import android.net.Uri
import com.bmprj.cointicker.utils.Resource
import com.bmprj.cointicker.model.FavouriteCoin
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface CloudRepository {

    suspend fun addFavourite(userID:String,favouriteCoin:FavouriteCoin): Flow<Boolean>

    suspend fun getFavourite(userID:String, coinId:String) : Flow<Boolean>

    suspend fun getAllFavourites(userID:String): Flow<List<FavouriteCoin>>

    suspend fun delete(userID:String,coinId:String): Flow<Boolean>

    suspend fun deleteUserInfo(userID: String):Flow<Boolean>


}