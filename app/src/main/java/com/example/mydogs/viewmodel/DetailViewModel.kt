package com.example.mydogs.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mydogs.model.DogBreed
import com.example.mydogs.model.DogDatabase
import kotlinx.coroutines.launch
import java.util.*

class DetailViewModel(application: Application) : BaseViewModel(application) {

    val dogLiveData = MutableLiveData<DogBreed>()

    fun fetch(uuid: Int) {
     launch {
         val dog  =  DogDatabase(getApplication()).dogDao().getDog(uuid)
         dogLiveData.value = dog
     }

    }

}