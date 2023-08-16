package com.bmprj.cointicker.data.firebase.cloud

import com.bmprj.cointicker.data.firebase.di.Resource
import com.bmprj.cointicker.model.FavouriteCoin
import com.google.firebase.auth.FirebaseUser

interface CloudRepository {

    suspend fun addFavourite(userID:String,favouriteCoin:FavouriteCoin): Resource<Unit>

    suspend fun getFavourite(userID:String, coinId:String) : Resource<Boolean>

    suspend fun getAllFavourites(userID:String): Resource<List<FavouriteCoin>>

    suspend fun delete(userID:String,coinId:String): Resource<Unit>




}