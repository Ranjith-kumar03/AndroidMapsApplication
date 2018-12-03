package tk.onlinesilkstore.androidmapsapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
//import android.location.LocationListener;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private Marker currentuserLOcationMArker;
    private static final int Request_User_LOcation_Code=99;
    private double latitude, longitude;
    private String url="";
    private int ProximityRadius=10000;
    private static final String TAG = "MapsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
        {
            checkUserLocationPermission();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onClick(View v)
    {
        String hospital="hospital",school="school", resturant="resturant";
        Object transfer_data[]=new Object[2];
        GetNearByPlaces getNearByPlaces=new GetNearByPlaces();

        switch (v.getId())
        {
            case R.id.search_btn:
                mMap.clear();
                EditText search=(EditText)findViewById(R.id.location_search);
                String  search_item=search.getText().toString();
                List<Address> address_list = null;
                MarkerOptions search_markeroptions=new MarkerOptions();
                if(!TextUtils.isEmpty(search_item))
                {
                    Geocoder geocoder= new Geocoder(this);
                    try {
                        address_list=geocoder.getFromLocationName(search_item,6);
                        if(address_list!=null) {

                            for (int i = 0; i < address_list.size(); i++) {
                                Address listing = address_list.get(i);
                                LatLng latLng = new LatLng(listing.getLatitude(), listing.getLongitude());
                                search_markeroptions.position(latLng);
                                search_markeroptions.title(search_item);
                                search_markeroptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                mMap.addMarker(search_markeroptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(1));
                            }

                        }
                        else
                        {
                            Toast.makeText(this,"No such Location exists",Toast.LENGTH_LONG).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    Toast.makeText(this,"Please enter a search item before clicking search item",Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.hospital:
                mMap.clear();
                url=getURL(latitude,longitude,hospital);
                transfer_data[0]=mMap;
                transfer_data[1]=url;
                getNearByPlaces.execute(transfer_data);
                Toast.makeText(this,"Searching for Hospitals......",Toast.LENGTH_LONG).show();
                Toast.makeText(this,"Showing Nearby Hospitals.........",Toast.LENGTH_LONG).show();
                break;

            case R.id.school:
                mMap.clear();
                url=getURL(latitude,longitude,school);
                transfer_data[0]=mMap;
                transfer_data[1]=url;
                getNearByPlaces.execute(transfer_data);
                Toast.makeText(this,"Searching for schools......",Toast.LENGTH_LONG).show();
                Toast.makeText(this,"Showing Nearby schools.........",Toast.LENGTH_LONG).show();
                break;

            case R.id.resturant:
                mMap.clear();
                url=getURL(latitude,longitude,resturant);
                transfer_data[0]=mMap;
                transfer_data[1]=url;
                getNearByPlaces.execute(transfer_data);
                Toast.makeText(this,"Searching for resturants......",Toast.LENGTH_LONG).show();
                Toast.makeText(this,"Showing Nearby resturants.........",Toast.LENGTH_LONG).show();
                break;
        }
    }

    private String getURL(double latitude,double longitude,String nearByPlace)
    {
        StringBuilder google_url= new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        google_url.append("location="+latitude+","+longitude);
        google_url.append("&radius="+ProximityRadius);
        google_url.append("&type="+nearByPlace);
        google_url.append("&sensor=true");
        google_url.append("&key="+"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        Log.d(TAG , "getURL: "+google_url.toString());
        return  google_url.toString();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
            buildgoogleAPIclient();
            mMap.setMyLocationEnabled(true);
        }
     }

     public boolean checkUserLocationPermission()
     {
         if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
         {
             if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
             {
                 ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.ACCESS_FINE_LOCATION},Request_User_LOcation_Code );
             }
             else
             {
                 ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.ACCESS_FINE_LOCATION},Request_User_LOcation_Code );
             }
             return false;
         }
         else
         {
             return true;
         }
     }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode)
        {
            case Request_User_LOcation_Code:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED)
                    {
                        if(googleApiClient==null)
                        {
                            buildgoogleAPIclient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(this, "Permission Not Grated",Toast.LENGTH_LONG).show();
                }
                return;
        }

    }

    protected synchronized void buildgoogleAPIclient()
    {
        googleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        lastlocation=location;
        if(currentuserLOcationMArker!=null)
        {
            currentuserLOcationMArker.remove();
        }

        LatLng latLng= new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("User Current LOcation");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        currentuserLOcationMArker=mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(1));
        if(googleApiClient!=null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }


    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest= new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
