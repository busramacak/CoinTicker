package com.bmprj.cointicker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bmprj.cointicker.model.CoinMarketItem

@Dao
interface CoinDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCoins(coinList:List<Entity>)

    @Query("SELECT * FROM coin")
    suspend fun getCoins():List<Entity>

    @Query("Select * FROM coin WHERE symbol LIKE :q || '%' OR name LIKE :q || '%'")
    suspend fun getCoin(q : String) : List<Entity>

}