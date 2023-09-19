package com.bmprj.cointicker.data.remote.firebase.cloud

import com.bmprj.cointicker.model.FavouriteCoin
import com.bmprj.cointicker.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
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
        val ref = firebaseCloud
            .collection(Constants.COLLECTION_COINS)
            .document(userID)
            .collection(Constants.COLLECTION_FAVLIST)

        val snaps = ref.get().await()


        val deleteTask = snaps.documents.map{document->
            ref.document(document.id).delete().continueWith{task->
                task.isSuccessful
            }
        }

        val results = deleteTask.map { it.await() }

        val hasFailure = results.any { !it }

        emit(!hasFailure)


    }
}

