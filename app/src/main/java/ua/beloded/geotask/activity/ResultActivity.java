package ua.beloded.geotask.activity;

import android.graphics.Color;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.android.PolyUtil;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import ua.beloded.geotask.Controller.RetrofiQuery;
import ua.beloded.geotask.R;
import ua.beloded.geotask.model.DirectionResults;

public class ResultActivity extends AppCompatActivity implements OnMapReadyCallback, Callback<DirectionResults> {

    private DirectionResults results;
    private GoogleMap resultMap;
    private Address startAdress;
    private Address endAdress;
    private String startCordinate;
    private String endCordinate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        getExtraData();
        initiateMap();

        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl("https://maps.googleapis.com/")
                .build();
        RetrofiQuery query = retrofit.create(RetrofiQuery.class);
        Call<DirectionResults> call = query.getWay(startCordinate, endCordinate,"driving", getString(R.string.directionsApi));
        call.enqueue(this);
    }

    private void initiateMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.resultMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        resultMap = googleMap;
    }

    public void getExtraData(){
        Bundle bundle = getIntent().getBundleExtra("pointBundle");
        startAdress = bundle.getParcelable("start");
        endAdress = bundle.getParcelable("destination");
        startCordinate = startAdress.getLatitude() + "," + startAdress.getLongitude();
        endCordinate = endAdress.getLatitude() + "," + endAdress.getLongitude();
    }

    @Override
    public void onResponse(Response<DirectionResults> response, Retrofit retrofit) {
        results = response.body();
        LatLng startPosition = new LatLng(startAdress.getLatitude(), startAdress.getLongitude());
        LatLng endPosition = new LatLng(endAdress.getLatitude(), endAdress.getLongitude());
        List<LatLng> way = PolyUtil.decode(results.getRoutes().get(0).getOverviewPolyLine().getPoints());
        PolylineOptions polyline = new PolylineOptions().color(Color.BLACK).visible(true).width(10);
        for (LatLng point: way){
            polyline.add(point);
        }

        resultMap.addMarker(new MarkerOptions().position(startPosition));
        resultMap.addMarker(new MarkerOptions().position(endPosition));
        resultMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(startPosition, 5, 0, 0)));
        resultMap.addPolyline(polyline);
    }

    @Override
    public void onFailure(Throwable t) {

    }
}

