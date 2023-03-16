package com.test.fitnessstudios.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.fitnessstudios.network.RetrofitHelper
import com.test.fitnessstudios.network.YelpApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    val loading = MutableLiveData(true)
    val currentTab = MutableLiveData(HomeTabs.MAP)

    fun changeTabToMaps() {
        currentTab.value = HomeTabs.MAP
    }

    fun changeTabToList() {
        currentTab.value = HomeTabs.LIST
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val apiService = RetrofitHelper.getInstance().create(YelpApi::class.java)
            val call = apiService.getBusinesses(
                mapOf(
                    "sort_by" to "best_match",
                    "limit" to "5",
                    "location" to "95618",
                    "term" to "Fitness"
                )
            )
            val businesses = call.body()?.businesses ?: return@launch
            businesses.forEach {
                Log.i("YelpDemo", "$it")
            }
            loading.postValue(false)
        }
    }

    enum class HomeTabs {
        MAP,
        LIST
    }

}