package ua.beloded.geotask.activity;

import android.content.Intent;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

import ua.beloded.geotask.Controller.MainController;
import ua.beloded.geotask.R;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, AdapterView.OnItemClickListener{


    private GoogleMap gMap1;
    private GoogleMap gMap2;
    private ListView cityList;
    private ListView destinationCityList;
    private List<Address> startAdresses;
    private List<Address> destAddresses;
    private Bundle bundle;
    private MainController controller;
    private SearchView searchCity;
    private SearchView searchCity2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main1);

        TabHost tabHost = findViewById(R.id.tab_host);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("Start point");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tab2");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator("Destination");
        tabHost.addTab(tabSpec);
        createMap();


        Button resultBtn = findViewById(R.id.resultBtn);
        resultBtn.setOnClickListener(this);
        cityList = findViewById(R.id.cityList);
        destinationCityList = findViewById(R.id.cityList2);
        searchCity = findViewById(R.id.searchCity);
        searchCity2 = findViewById(R.id.searchCity2);
        bundle = new Bundle();
        controller = new MainController(this);
        searchCity.setOnSearchClickListener(this);
        searchCity2.setOnSearchClickListener(this);


        searchCity.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                startAdresses = controller.citySearch(cityList, s);
                cityList.setOnItemClickListener(MainActivity.this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searchCity2.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                destAddresses = controller.citySearch(destinationCityList, s);
                destinationCityList.setOnItemClickListener(MainActivity.this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searchCity.setOnCloseListener(() -> {
            cityList.setVisibility(View.GONE);
            return false;
        });

        searchCity2.setOnCloseListener(() -> {
            destinationCityList.setVisibility(View.GONE);
            return true;
        });

    }

    private void createMap(){
        SupportMapFragment mapFragmentStart = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapTab1);
        SupportMapFragment mapFragmentEnd = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapTab2);
        mapFragmentStart.getMapAsync(this);
        mapFragmentEnd.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (gMap1 == null){
            gMap1 = googleMap;
        }else {
            gMap2 = googleMap;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.resultBtn :
                if (!bundle.isEmpty() && bundle != null) {
                    Intent intent = new Intent(this, ResultActivity.class);
                    intent.putExtra("pointBundle", bundle);
                    startActivity(intent);
                }
                break;
            case R.id.searchCity :
                cityList.setVisibility(View.VISIBLE);
                break;

            case R.id.searchCity2 :
                cityList.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()) {
            case R.id.cityList :
                controller.setMapPoint(gMap1, startAdresses.get(i));
                bundle.putParcelable("start", startAdresses.get(i));
                break;
            case R.id.cityList2 :
                controller.setMapPoint(gMap2, destAddresses.get(i));
                bundle.putParcelable("destination", destAddresses.get(i));
                break;
        }
    }

}
