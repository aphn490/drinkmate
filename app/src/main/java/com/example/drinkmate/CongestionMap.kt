package com.example.drinkmate

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.setFragmentResultListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import java.sql.Types.NULL


class CongestionMap : Fragment(), OnMapReadyCallback {
    // Initializing the GoogleMap and SupportMapFragment objects
    private lateinit var trafMap : GoogleMap
    private lateinit var mapFrag : SupportMapFragment
    // Initializing arr which will hold the bars objects from Congestion fragment and
    // latlong which will hold values of the bars from strings
    private var arr: MutableList<HashMap<String, *>> = ArrayList()
    private var latlong: MutableList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialization of a job and coroutine in order to make sure a job is completed first before
        // going into code that needs the result
        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + job)
        // The setFragmentResultListener will retrieve data that was sent to it with the attached
        // key, it will retrieve the arraylist from Congestion
        setFragmentResultListener("requestKey") { key, bundle ->
            uiScope.launch(Dispatchers.IO) {
                // Assigns arr variable to the ArrayList that is retrieved
                arr = bundle.get("list") as MutableList<HashMap<String, *>>
                for (x in arr) {
                    // Assigns variables to the data in the HashMap and then adds the values to the latlong array list
                    val name : String = x!!["name"] as String
                    val imageurl : String  = x!!["image"] as String
                    val longi : Double = x!!["long"] as Double
                    val lati : Double = x!!["lat"] as Double
                    latlong.add(name)
                    latlong.add(imageurl)
                    latlong.add(lati.toString())
                    latlong.add(longi.toString())
                }
                withContext(Dispatchers.Main) {
                    // Sets values that reference the ImageViews in the xml that will hold all the images of the bars
                    val bar1 = view?.findViewById<ImageView>(R.id.mapBar1)
                    bar1?.visibility = GONE
                    val bar2 = view?.findViewById<ImageView>(R.id.mapBar2)
                    bar2?.visibility = GONE
                    val bar3 = view?.findViewById<ImageView>(R.id.mapBar3)
                    bar3?.visibility = GONE
                    val bar4 = view?.findViewById<ImageView>(R.id.mapBar4)
                    bar4?.visibility = GONE
                    val bar5 = view?.findViewById<ImageView>(R.id.mapBar5)
                    bar5?.visibility = GONE

                    // If latlong is bigger than or equal to 4, then it will add the first bar's information.
                    if (latlong.size >= 4) {
                        // Picasso will load the imageview with the image url that was saved
                        Picasso.get().load(latlong[1]).into(bar1)
                        bar1?.visibility = VISIBLE
                        val bar1latlong = LatLng(latlong[2].toDouble(), latlong[3].toDouble())
                        // The google maps will add a marker to the bar's position, based on its latitude and longitude
                        trafMap.addMarker(
                            MarkerOptions()
                                .position(bar1latlong)
                                .title(latlong[0])
                        )
                        // If the image is clicked, then the GoogleMaps will move the camera to where the bar's location.
                        bar1?.setOnClickListener() {
                            trafMap.moveCamera(CameraUpdateFactory.newLatLng(bar1latlong))
                        }
                    }
                    // If latlong is bigger than or equal to 8, then it will add the second bar's information.
                    if (latlong.size >= 8) {
                        // Picasso will load the imageview with the image url that was saved
                        Picasso.get().load(latlong[5]).into(bar2)
                        bar2?.visibility = VISIBLE
                        val bar2latlong = LatLng(latlong[6].toDouble(), latlong[7].toDouble())
                        // The google maps will add a marker to the bar's position, based on its latitude and longitude
                        trafMap.addMarker(
                            MarkerOptions()
                                .position(bar2latlong)
                                .title(latlong[4])
                        )
                        // If the image is clicked, then the GoogleMaps will move the camera to where the bar's location.
                        bar2?.setOnClickListener() {
                            trafMap.moveCamera(CameraUpdateFactory.newLatLng(bar2latlong))
                        }
                    }
                    // If latlong is bigger than or equal to 12, then it will add the third bar's information.
                    if (latlong.size >= 12) {
                        // Picasso will load the imageview with the image url that was saved
                        Picasso.get().load(latlong[9]).into(bar3)
                        bar3?.visibility = VISIBLE
                        val bar3latlong = LatLng(latlong[10].toDouble(), latlong[11].toDouble())
                        // The google maps will add a marker to the bar's position, based on its latitude and longitude
                        trafMap.addMarker(
                            MarkerOptions()
                                .position(bar3latlong)
                                .title(latlong[8])
                        )
                        // If the image is clicked, then the GoogleMaps will move the camera to where the bar's location.
                        bar3?.setOnClickListener() {
                            trafMap.moveCamera(CameraUpdateFactory.newLatLng(bar3latlong))
                        }
                    }
                    // If latlong is bigger than or equal to 16, then it will add the fourth bar's information.
                    if (latlong.size >= 16) {
                        // Picasso will load the imageview with the image url that was saved
                        Picasso.get().load(latlong[13]).into(bar4)
                        bar4?.visibility = VISIBLE
                        val bar4latlong = LatLng(latlong[14].toDouble(), latlong[15].toDouble())
                        // The google maps will add a marker to the bar's position, based on its latitude and longitude
                        trafMap.addMarker(
                            MarkerOptions()
                                .position(bar4latlong)
                                .title(latlong[12])
                        )
                        // If the image is clicked, then the GoogleMaps will move the camera to where the bar's location.
                        bar4?.setOnClickListener() {
                            trafMap.moveCamera(CameraUpdateFactory.newLatLng(bar4latlong))
                        }
                    }
                    // If latlong is bigger than or equal to 20, then it will add the last bar's information.
                    if (latlong.size >= 20) {
                        // Picasso will load the imageview with the image url that was saved
                        Picasso.get().load(latlong[17]).into(bar5)
                        bar5?.visibility = VISIBLE
                        val bar5latlong = LatLng(latlong[18].toDouble(), latlong[19].toDouble())
                        // The google maps will add a marker to the bar's position, based on its latitude and longitude
                        trafMap.addMarker(
                            MarkerOptions()
                                .position(bar5latlong)
                                .title(latlong[16])
                        )
                        // If the image is clicked, then the GoogleMaps will move the camera to where the bar's location.
                        bar5?.setOnClickListener() {
                            trafMap.moveCamera(CameraUpdateFactory.newLatLng(bar5latlong))
                        }
                    }
                }
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_congestion_map, container, false)
        mapFrag = childFragmentManager.findFragmentById(R.id.trafficMap) as SupportMapFragment
        mapFrag.getMapAsync(this)
        return view
    }


    override fun onMapReady(map : GoogleMap) {
        // Enables showing traffic on the map
        trafMap = map
        trafMap.isTrafficEnabled = true
        trafMap.moveCamera(CameraUpdateFactory.zoomTo(17f))
    }

}
