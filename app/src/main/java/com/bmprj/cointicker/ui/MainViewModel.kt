package com.bmprj.cointicker.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import com.bmprj.cointicker.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(application: Application):BaseViewModel(application){

// TODO base viewmodel kod tekrarını engelleyen fonksiyonşar loglama da barındırsın
    //loglama için çözümüm yok. kod tekrarını biraz engellediğimi düşünüyorum.
}