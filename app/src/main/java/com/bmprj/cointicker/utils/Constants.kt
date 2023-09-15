package com.bmprj.cointicker.utils

object Constants {
    const val BASE_URL ="https://api.coingecko.com/api/v3/"
    const val COINGECKO_URL="https://www.coingecko.com/tr/api"
    const val DB_NAME = "coin"
    const val COLLECTION_COINS="coins"
    const val COLLECTION_FAV="fav"
    const val COLLECTION_FAVLIST="favouriteList"
    const val STORAGE_PATH="profile_image"
    val String.jpg get() ="$this.jpg"
}