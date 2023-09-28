package com.bmprj.cointicker.di

import android.content.Context
import androidx.room.Room
import com.bmprj.cointicker.data.db.CoinDAO
import com.bmprj.cointicker.data.db.CoinDatabase
import com.bmprj.cointicker.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class DbModule {

    @Provides
    @ViewModelScoped
    fun provideDatabase(@ApplicationContext context: Context): CoinDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CoinDatabase::class.java,
                Constants.DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    @ViewModelScoped
    fun provideDAO(db: CoinDatabase) : CoinDAO {
        return db.coinDAO()
    }
    // todo shared pref için bir yapı yaz ve ihtiyaçlarını karşılasın
//  (karşıladı mı?)
    // todo runtime da veri tutmak için bir yapı yaz ve ihtiyaçlarını karşılasın
//  (shared pref içerisinde tuttuğum nanosaniye oluyor mu? başka bir şekilde mi ? )
    // TODO loading için bir tane custom wigdet yaz
    // todo (custom xml, custom widget(frame layouttan kalıtım) )
    //denedim ama eksik kaldı ->çalışıyor
    // TODO cihazın rootlu olup olmaması
    //https://medium.com/@deekshithmoolyakavoor/root-detection-in-android-device-9144b7c2ae07
    //bir yerde saklamadım ama şifreleyip çözümledim.
    //todo kullanıcı verilerini şifreleyrek firebase de sakla yada shared preds

}