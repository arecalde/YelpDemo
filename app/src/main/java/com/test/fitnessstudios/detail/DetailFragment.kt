package com.test.fitnessstudios.detail

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.gms.common.api.internal.ApiKey
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.google.maps.android.PolyUtil
import com.test.fitnessstudios.R
import com.test.fitnessstudios.databinding.DetailFragmentBinding
import com.test.fitnessstudios.helpers.LocationHelper
import com.test.fitnessstudios.helpers.PERMISSION_REQUEST
import com.test.fitnessstudios.home.toLatLng
import com.test.fitnessstudios.model.Businesses
import com.test.fitnessstudios.model.NavigationResult
import com.test.fitnessstudios.model.Steps
import com.test.fitnessstudios.network.DirectionsApi
import com.test.fitnessstudios.network.RetrofitHelper
import com.test.fitnessstudios.network.googleAPIKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailFragment : Fragment() {
    private val args: DetailFragmentArgs by navArgs()

    private val viewModel: DetailViewModel by viewModels {
        DetailViewModelFactory(requireActivity().application, args.imageUrl)
    }

    private var fusedLocationClient: FusedLocationProviderClient? = null

    private var permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DetailFragmentBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (LocationHelper.checkPermission(context)) {
            LocationHelper.getLocation(context) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val businessLatLng = args.location ?: return@launch
                    viewModel.getNavigationRoute(businessLatLng, it.toLatLng())
                }
            }
        } else {
            requestPermissions(permissions, PERMISSION_REQUEST)
        }

        viewModel.navigationResult.observe(viewLifecycleOwner) {
            lifecycleScope.launch(Dispatchers.IO) {
                processGoogleMap(it)
            }

        }

        return binding.root
    }

    private fun processGoogleMap(navigationResult: NavigationResult) {
        val route = navigationResult.routes.first()
        val steps = navigationResult.routes.first().legs.first().steps

        val origin = navigationResult.origin ?: return
        val destination = navigationResult.destination ?: return

        lifecycleScope.launch(Dispatchers.Main) {
            val path = steps.map {
                val points = it.polyline?.points
                PolyUtil.decode(points)
            }

            val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment?
            mapFragment?.getMapAsync { googleMap ->
                val zoomLevel = 12f //This goes up to 21

                googleMap.addMarker(MarkerOptions().position(origin).title("Origin"))
                googleMap.addMarker(MarkerOptions().position(destination).title("Destination"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(origin, zoomLevel))
                path.forEach {
                    googleMap.addPolyline(PolylineOptions().addAll(it).color(Color.RED))
                }
            }

        }
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