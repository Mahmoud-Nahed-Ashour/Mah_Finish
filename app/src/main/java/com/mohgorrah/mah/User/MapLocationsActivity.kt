package com.mohgorrah.mah.User

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mohgorrah.mah.R


class MapLocationsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_locations)
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.orange
                )
            )
        )

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)



        getCurrentLocationUser()

    }

    private fun getCurrentLocationUser() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                permissionCode
            )
            return
        }

      val getlocation =  fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->

            if (location != null) {

                currentLocation = location
                Toast.makeText(
                    applicationContext,
                    currentLocation.latitude.toString() + "" + currentLocation.longitude.toString(),
                    Toast.LENGTH_SHORT
                ).show()

                val mapFragment =
                    supportFragmentManager.findFragmentById(R.id.frame_layout) as SupportMapFragment
                mapFragment.getMapAsync(this@MapLocationsActivity)

            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationUser()
            }

        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val Location = LatLng(31.502454, 34.436597)
        val markerOptions = MarkerOptions().position(Location).title("جمعية واعد للأسرى والمحررين")
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(Location))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Location, 100f))
        googleMap.addMarker(markerOptions)

    }


}