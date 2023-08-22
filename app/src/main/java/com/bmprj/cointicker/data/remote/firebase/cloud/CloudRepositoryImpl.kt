@file:Suppress("Since15")

package com.bmprj.cointicker.data.remote.firebase.cloud

import com.bmprj.cointicker.utils.Resource
import com.bmprj.cointicker.utils.await
import com.bmprj.cointicker.model.FavouriteCoin
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
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

        emit(firebaseCloud
            .collection("coins")
            .document(userID)
            .collection("favouriteList")
            .document(favouriteCoin.id)
            .set(favouriteCoin).isSuccessful)

    }

    override suspend fun getFavourite(
        userID:String,
        coinId: String
    ): Flow<Boolean> = flow {

        emit( firebaseCloud
            .collection("coins")
            .document(userID)
            .collection("favouriteList")
            .document(coinId)
            .get().result.exists())
    }

    override suspend fun getAllFavourites(
        userID:String
    ): Flow<List<FavouriteCoin>> =flow{

        emit(firebaseCloud
            .collection("coins")
            .document(userID)
            .collection("favouriteList")
            .get().result.documents.mapNotNull { document ->
                document.toObject(FavouriteCoin::class.java)
            })
    }

    override suspend fun delete(
        userID:String,
        coinId: String
    ): Flow<Boolean> = flow{

        emit(
            firebaseCloud
            .collection("coins")
            .document(userID)
            .collection("favouriteList")
            .document(coinId).delete().isSuccessful)
    }
}