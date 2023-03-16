package com.test.fitnessstudios.detail

import android.app.Application
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.squareup.picasso.Picasso
import com.test.fitnessstudios.model.NavigationResult
import com.test.fitnessstudios.network.DirectionsApi
import com.test.fitnessstudios.network.RetrofitHelper
import com.test.fitnessstudios.network.googleAPIKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(imageUrl: String, application: Application) : AndroidViewModel(application) {
    val navigationResult: MutableLiveData<NavigationResult> = MutableLiveData()
    val imageUrl = MutableLiveData(imageUrl)
    suspend fun getNavigationRoute(destination: LatLng, origin: LatLng) {
        val apiService = RetrofitHelper.getDirectionsInstance().create(DirectionsApi::class.java)
        val call = apiService.getRoute(
            mapOf(
                "origin" to "${origin.latitude},${origin.longitude}",
                "destination" to "${destination.latitude},${destination.longitude}",
                "key" to googleAPIKey
            )
        )

        val navigationResult = call.body() ?: return
        navigationResult.origin = origin
        navigationResult.destination = destination
        this.navigationResult.postValue(navigationResult)
    }
}

class DetailViewModelFactory(
    private val application: Application,
    private val imageUrl: String
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailViewModel(imageUrl, application) as T
    }
}

@BindingAdapter("imageUrl")
fun loadImage(view : View, url : String?){
    if (url.isNullOrEmpty()) return
    Picasso
        .get()
        .load(url)
        .into((view as ImageView))
}