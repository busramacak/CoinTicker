package com.bmprj.cointicker.data.firebase.cloud

import com.bmprj.cointicker.data.firebase.di.Resource
import com.bmprj.cointicker.data.utils.await
import com.bmprj.cointicker.model.FavouriteCoin
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class CloudRepositoryImpl @Inject constructor(
    private val firebaseCloud: FirebaseFirestore
) : CloudRepository {
    override suspend fun addFavourite(
        user: FirebaseUser,
        favouriteCoin: FavouriteCoin,
    ): Resource<Unit> {
        return try{
            firebaseCloud
                .collection("coins")
                .document(user.uid)
                .collection("favouriteList")
                .document(favouriteCoin.id)
                .set(favouriteCoin)

            Resource.Success(Unit)
        }catch (e:Exception){
            Resource.Failure(e)

        }
    }

    override suspend fun getFavourite(
        user: FirebaseUser,
        coinId: String
    ): Resource<Boolean> {
        return try{
            val snapsot = firebaseCloud
                .collection("coins")
                .document(user.uid)
                .collection("favouriteList")
                .document(coinId)
                .get().await()

            Resource.Success(snapsot.exists())

        }catch (e:Exception){
            Resource.Failure(e)
        }
    }

    override suspend fun getAllFavourites(
        user: FirebaseUser
    ): Resource<List<FavouriteCoin>> {
        return try{
            val snaps = firebaseCloud
                .collection("coins")
                .document(user.uid)
                .collection("favouriteList")
                .get().await()

            val fav = snaps.documents.mapNotNull { document ->
                document.toObject(FavouriteCoin::class.java)
            }

            Resource.Success(fav)


        }catch (e:Exception){
            Resource.Failure(e)

        }

    }

    override suspend fun delete(user: FirebaseUser, coinId: String): Resource<Unit> {
        return try {
            firebaseCloud
                .collection("coins")
                .document(user.uid)
                .collection("favourşteList")
                .document(coinId).delete().await()

            Resource.Success(Unit)
        }catch (e:Exception){
            Resource.Failure(e)
        }
    }
}