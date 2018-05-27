package ua.beloded.geotask.Controller;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Geocode extends AsyncTask<String, Integer, List<Address>> {

    private Context context;
    private List<Address> addressList;

    public Geocode(Context context) {
        this.context = context;
    }

    @Override
    protected List<Address> doInBackground(String... strings) {
        Geocoder geocoder = new Geocoder(context);
        try {
            return geocoder.getFromLocationName(strings[0], 10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Address> addresses) {
        super.onPostExecute(addresses);
        addressList = addresses;
    }
}
