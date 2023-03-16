package com.test.fitnessstudios.detail

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.test.fitnessstudios.R
import com.test.fitnessstudios.helpers.LocationHelper
import com.test.fitnessstudios.helpers.PERMISSION_REQUEST

class DetailFragment : Fragment() {

    private val viewModel: DetailViewModel by viewModels()
    private var fusedLocationClient: FusedLocationProviderClient? = null

    private var permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?

        mapFragment?.getMapAsync { googleMap ->
            val zoomLevel = 8f //This goes up to 21
            val latLngOrigin = LatLng(38.0, -121.0) // Ayala
            val latLngDestination = LatLng(38.01, -121.01) // SM City
            googleMap?.addMarker(MarkerOptions().position(latLngOrigin).title("Ayala"))
            googleMap?.addMarker(MarkerOptions().position(latLngDestination).title("SM City"))
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(38.0, -121.0), zoomLevel))
        }

        if (LocationHelper.checkPermission(context)) {
            LocationHelper.getLocation(context) {

            }
        }else{
            requestPermissions(permissions, PERMISSION_REQUEST)
        }

        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

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
                    LocationHelper.getLocation(context) {

                    }
                }
            }
        }
    }

}