package com.example.googlemapapplication

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback
    , GoogleApiClient.ConnectionCallbacks
    , GoogleApiClient.OnConnectionFailedListener {


    private lateinit var mMap: GoogleMap
    private lateinit var locationClient: GoogleApiClient

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        btn_location.setOnClickListener {
            if (checkSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == PackageManager.PERMISSION_GRANTED) {
                val location = LocationServices.getFusedLocationProviderClient(this)
                location.lastLocation.addOnSuccessListener {
                    it.let {
                        val latLng = LatLng(it.latitude, it.longitude)
                        val cameraUpdateFactory = CameraUpdateFactory.newLatLngZoom(latLng, 15f)
                        mMap.animateCamera(cameraUpdateFactory)
                    }
                }
            } else {
                ActivityCompat.requestPermissions(this, arrayOf("android.permission.ACCESS_FINE_LOCATION"), 654)
            }

        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        locationClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()

        locationClient.connect()

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M &&
            checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED
        )
            mMap.isMyLocationEnabled = true
        else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf("android.permission.ACCESS_FINE_LOCATION"),
                123
            )
            mMap.isMyLocationEnabled = true
        }

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(31.228358, 29.951477)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15f))
    }

    override fun onConnected(p0: Bundle?) {
        //pass
    }

    override fun onConnectionSuspended(p0: Int) {
        //pass
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        //pass
    }


}
