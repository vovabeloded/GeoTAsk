package ua.beloded.geotask.Controller;

import android.content.Context;
import android.location.Address;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ua.beloded.geotask.Adapter.CityListAdapteer;

public class MainController {

    private final Context context;
    private CityListAdapteer adapter;

    public MainController(Context context) {
        this.context = context;
    }

    public List<Address> citySearch (ListView listView, String cityName) {
        try {
            List<Address> addresses = new Geocode(context).execute(cityName).get();

            if (addresses != null && !addresses.isEmpty()) {
                if(adapter == null){
                    adapter = new CityListAdapteer(addresses, context);
                }else {
                    adapter.setAddresses(addresses);
                }
                    listView.setAdapter(adapter);
            }
            return addresses;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setMapPoint (GoogleMap map, Address address){
        map.clear();
        LatLng marker = new LatLng(address.getLatitude(), address.getLongitude());
        map.addMarker(new MarkerOptions().position(marker));
        map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(marker, 5, 0, 0)));
    }

}
