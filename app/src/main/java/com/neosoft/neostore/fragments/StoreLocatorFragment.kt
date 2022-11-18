@file:Suppress("DEPRECATION")
package com.neosoft.neostore.fragments
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.neosoft.neostore.R
import com.neosoft.neostore.activities.MainActivity
import com.neosoft.neostore.constants.Constants
import kotlinx.android.synthetic.main.fragment_store_locator.*
import kotlinx.android.synthetic.main.fragment_store_locator.view.*
import java.io.IOException
import kotlin.collections.ArrayList
@Suppress("DEPRECATION", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class StoreLocatorFragment : Fragment() ,OnMapReadyCallback{
    private lateinit var googleMap: GoogleMap
    private val list = ArrayList<LatLng>()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //for showing map
        if (map_layout != null) {
            map_layout.onCreate(null)
            map_layout.onResume()
            map_layout.getMapAsync(this)
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_store_locator, container, false)
     //for getting address
      view.btn_get_store.setOnClickListener {
          val builder = PlacePicker.IntentBuilder()
          try {
              startActivityForResult(builder.build(requireActivity()), Constants.PLACE_PICKER_REQUEST_CODE)
          } catch (e: GooglePlayServicesRepairableException)
          {
              e.printStackTrace()
          } catch (e: GooglePlayServicesNotAvailableException)
          {
              e.printStackTrace()
          }
      }
        return view
    }
    override fun onMapReady(map: GoogleMap?)
    {
        MapsInitializer.initialize(activity)
        if (map != null)
        {
            googleMap = map
        }
        val zoomLevel = 15f
        val homeLatLng = LatLng(17.88252355892892,75.01984086125351)
        val myLang = LatLng(17.894196005836072, 75.02310673299861)
        val solapur = LatLng(17.88321438200638, 75.01801728143161)
        val satara = LatLng(17.883236213407233, 75.02217165259627)
        val akluj = LatLng(17.879841517023987, 75.01547595006508)
        list.add(homeLatLng)
        list.add(myLang)
        list.add(solapur)
        list.add(satara)
        list.add(akluj)
        for (i in 0 until list.size)
        {
            map?.addMarker(MarkerOptions().position(list[i]).title("My Location"))
        }
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
         map?.addMarker(MarkerOptions().position(homeLatLng))
    }
    @SuppressLint("SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.PLACE_PICKER_REQUEST_CODE)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                val place: Place = PlacePicker.getPlace(data, activity)
                //for getting locale address
                val geocoder = Geocoder(activity)
             try
             {
                 val address1:List<Address> = geocoder.getFromLocation(place.latLng.latitude,place.latLng.longitude,1)
                 val name = address1[0].getAddressLine(0)
                 val city = address1[0].getAddressLine(1)
                 txt_place.text = name+city
             }catch (e:IOException)
             {
                 e.printStackTrace()
             }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val actionBar: androidx.appcompat.app.ActionBar? = (activity as MainActivity?)?.supportActionBar
        actionBar?.title = getString(R.string.store_locator)
    }
}
