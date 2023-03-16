package com.test.fitnessstudios.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.test.fitnessstudios.R
import com.test.fitnessstudios.databinding.BusinessItemBinding
import com.test.fitnessstudios.databinding.HomeFragmentBinding
import com.test.fitnessstudios.helpers.ItemAdapter
import com.test.fitnessstudios.model.Businesses

private const val PERMISSION_REQUEST = 10
class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()
    private var map : GoogleMap? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null

    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = HomeFragmentBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (checkPermission(permissions)) {
            getLocation()
        }else{
            requestPermissions(permissions, PERMISSION_REQUEST)
        }

        viewModel.locationLiveData.observe(viewLifecycleOwner) {
            val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
            mapFragment?.getMapAsync { googleMap ->
                viewModel.callBusinesses(it.toLatLng())
                map = googleMap
                val zoomLevel = 8f //This goes up to 21
                map?.moveCamera(CameraUpdateFactory.newLatLngZoom(it.toLatLng(), zoomLevel))
            }
        }

        viewModel.listOfBusinesses.observe(viewLifecycleOwner) {
            binding.businessList.adapter = ItemAdapter<Businesses, BusinessItemBinding>(
                it,
                viewLifecycleOwner,
                R.layout.business_item
            ) { business, businessItemBinding ->
                businessItemBinding.business = business
            }

            val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
            mapFragment?.getMapAsync { googleMap ->
                it.forEach { business ->
                    val latLng = business.coordinates?.toLatLng() ?: return@forEach
                    googleMap.addMarker(
                        MarkerOptions()
                            .title(business.name)
                            .position(latLng)
                    )
                }
            }
        }

        val mapTab = binding.tabLayout.newTab().setText(R.string.map)
        val listTab = binding.tabLayout.newTab().setText(R.string.list)

        binding.tabLayout.addTab(mapTab)
        binding.tabLayout.addTab(listTab)

        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.text == "List") {
                    viewModel.changeTabToList()
                } else {
                    viewModel.changeTabToMaps()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        return binding.root
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return
        val hasGps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (hasGps || hasNetwork) {
            if (hasGps) {
                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                localGpsLocation?.let {
                    viewModel.updateLocation(it)
                }
            } else if (hasNetwork) {
                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                localNetworkLocation?.let {
                    viewModel.updateLocation(it)
                }
            }
        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }

    private fun checkPermission(permissionArray: Array<String>): Boolean {
        var allSuccess = true
        for (i in permissionArray.indices) {
            if (context?.checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED)
                allSuccess = false
        }
        return allSuccess
    }
    //checks if the user has given permission to have their location recorder in the db

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    if (shouldShowRequestPermissionRationale(permissions[i])) {
                        Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Go to settings and enable the permission", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    getLocation()
                }
            }


        }
    }
}

fun Location.toLatLng(): LatLng {
    return LatLng(latitude, longitude)
}