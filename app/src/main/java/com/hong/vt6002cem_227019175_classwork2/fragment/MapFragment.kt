package com.hong.vt6002cem_227019175_classwork2.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.hong.vt6002cem_227019175_classwork2.R
import java.lang.Exception
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.util.Properties

class MapFragment : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var locationManager: LocationManager
    private val PERMISSIONS_REQUEST_LOCATION = 1
    private lateinit var locationListener: MyLocationListener
    private lateinit var googleMap: GoogleMap
    private var userLocationMarker: Marker? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootView = inflater.inflate(R.layout.fragment_maps, container, false)
        mapView = rootView.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)


        val task = ConnectToDatabaseTask()
        val hauntedHouseList = task.execute().get();

        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager


        mapView.getMapAsync { googleMap ->

            this.googleMap = googleMap
            for(ele in hauntedHouseList){
                googleMap.addMarker(MarkerOptions().position(LatLng(ele.lat,ele.long)).title(ele.name));
            }
            showUserLocation()
        }
        return rootView
    }




    private fun startLocationUpdates() {
        locationListener = MyLocationListener()
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {}

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_LOCATION && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates()
        }
    }

    private fun showUserLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request location permissions
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_LOCATION)
            return
        }
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location != null) {
            val latLng = LatLng(location.latitude, location.longitude)
            if (userLocationMarker == null) {
                userLocationMarker = googleMap.addMarker(
                    MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.current_location))
                        .position(latLng).title("My Location")
                )
            } else {
                userLocationMarker?.position = latLng
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
    }



    private inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            val latLng = LatLng(location.latitude, location.longitude)
            googleMap.addMarker(MarkerOptions().position(latLng).title("Your current location"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    }

    class ConnectToDatabaseTask : AsyncTask<Void, Void, List<HauntedHouse>>() {
        override fun doInBackground(vararg params: Void?): List<HauntedHouse>? {
            val connectionProps = Properties()
            connectionProps.put("user", "hong")
            connectionProps.put("password", "HongC_12345")
            var conn :Connection?  =null ;
            try {
                Class.forName("org.mariadb.jdbc.Driver").newInstance()
                conn = DriverManager.getConnection(
                    "jdbc:" + "mariadb" + "://" +
                            "61.239.141.245" +
                            ":" + "3306" + "/android_assignment" +
                            "",
                    connectionProps)


                val stmt = conn.createStatement()
                var houseList = mutableListOf<HauntedHouse>();

                val rs = stmt.executeQuery("SELECT * FROM haunted_house")
                while (rs.next()) {

                    var house = HauntedHouse(rs.getString("name"),rs.getDouble("lat"),rs.getDouble("long"))
                    houseList.add(house);

                }

                return houseList;
            } catch (ex: SQLException) {
                // handle any errors
                ex.printStackTrace()
            } catch (ex: Exception) {
                // handle any errors
                ex.printStackTrace()
            }


            return null;

        }


    }

    class HauntedHouse (val name: String,val lat: Double,val long: Double){

    }
}
