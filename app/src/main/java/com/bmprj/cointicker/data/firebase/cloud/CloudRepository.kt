package com.bmprj.cointicker.data.firebase.cloud

import com.bmprj.cointicker.data.firebase.di.Resource
import com.bmprj.cointicker.model.FavouriteCoin
import com.google.firebase.auth.FirebaseUser

interface CloudRepository {

    suspend fun addFavourite(user:FirebaseUser,favouriteCoin:FavouriteCoin): Resource<Unit>

    suspend fun getFavourite(user:FirebaseUser, coinId:String) : Resource<Boolean>

    suspend fun getAllFavourites(user:FirebaseUser): Resource<List<FavouriteCoin>>

    suspend fun delete(user:FirebaseUser,coinId:String): Resource<Unit>




}