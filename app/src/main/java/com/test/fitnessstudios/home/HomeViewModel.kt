package com.test.fitnessstudios.home

import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.test.fitnessstudios.model.Businesses
import com.test.fitnessstudios.network.RetrofitHelper
import com.test.fitnessstudios.network.YelpApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    val loading = MutableLiveData(true)
    val currentTab = MutableLiveData(HomeTabs.MAP)
    val mapIsVisible = currentTab.map { 
        it.equals(HomeTabs.MAP)
    }

    val locationLiveData: MutableLiveData<Location> = MutableLiveData()
    val listOfBusinesses: MutableLiveData<List<Businesses>> = MutableLiveData()

    fun changeTabToMaps() {
        if (currentTab.value == HomeTabs.LIST) {
            currentTab.value = HomeTabs.MAP
        }
    }

    fun updateLocation(location: Location) {
        locationLiveData.value = location
    }

    fun changeTabToList() {
        if (currentTab.value == HomeTabs.MAP) {
            currentTab.value = HomeTabs.LIST
        }
    }

    init {

    }

    fun callBusinesses(location: LatLng) {
        viewModelScope.launch(Dispatchers.IO) {
            val apiService = RetrofitHelper.getInstance().create(YelpApi::class.java)
            val call = apiService.getBusinesses(
                mapOf(
                    "sort_by" to "best_match",
                    "limit" to "20",
                    "latitude" to "${location.latitude}",
                    "longitude" to "${location.longitude}",
                    "term" to "Fitness"
                )
            )
            val businesses = call.body()?.businesses ?: return@launch
            businesses.forEach {
                Log.i("YelpDemo", "$it")
            }

            listOfBusinesses.postValue(businesses)
            loading.postValue(false)
        }
    }

    enum class HomeTabs {
        MAP,
        LIST
    }

}