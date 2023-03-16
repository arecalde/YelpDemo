package com.test.fitnessstudios

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.test.fitnessstudios.network.RetrofitHelper
import com.test.fitnessstudios.network.YelpApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch(Dispatchers.IO) {
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
        }
    }
}