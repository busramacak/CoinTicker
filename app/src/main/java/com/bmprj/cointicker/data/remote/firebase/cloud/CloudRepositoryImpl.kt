@file:Suppress("Since15")

package com.bmprj.cointicker.data.remote.firebase.cloud

import android.util.Log
import com.bmprj.cointicker.model.FavouriteCoin
import com.bmprj.cointicker.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CloudRepositoryImpl @Inject constructor(
    private val firebaseCloud: FirebaseFirestore
) : CloudRepository {
    override suspend fun addFavourite(
        userID:String,
        favouriteCoin: FavouriteCoin,
    ): Flow<Boolean> =flow{

        val snap = firebaseCloud
            .collection(Constants.COLLECTION_COINS)
            .document(userID)
            .collection(Constants.COLLECTION_FAVLIST)
            .document(favouriteCoin.id)
            .set(favouriteCoin).isSuccessful
        emit(snap)

    }

    override suspend fun getFavourite(
        userID:String,
        coinId: String
    ): Flow<Boolean> = flow {

        val snap = firebaseCloud
            .collection(Constants.COLLECTION_COINS)
            .document(userID)
            .collection(Constants.COLLECTION_FAVLIST)
            .document(coinId)
            .get().await().exists()

        emit(snap)
    }

    override suspend fun getAllFavourites(
        userID:String
    ):Flow<List<FavouriteCoin>> =flow{

        val snap = firebaseCloud
            .collection(Constants.COLLECTION_COINS)
            .document(userID)
            .collection(Constants.COLLECTION_FAVLIST)
            .get().await().documents.mapNotNull { document ->
            document.toObject(FavouriteCoin::class.java)}

        emit(snap)
    }

    override suspend fun delete(
        userID:String,
        coinId: String
    ): Flow<Boolean> = flow{

        emit(firebaseCloud
            .collection(Constants.COLLECTION_COINS)
            .document(userID)
            .collection(Constants.COLLECTION_FAVLIST)
            .document(coinId).delete().isSuccessful)
    }

    override suspend fun deleteUserInfo(userID: String): Flow<Boolean> =flow{
        println("deleteuserinfo cloudrepoimpl")

        val deleteResult = firebaseCloud.collection(Constants.COLLECTION_COINS).document(userID).delete().await()
        if(deleteResult!=null){
            emit(true)
        }else{
            emit(false)
        }




    }
}

