package tk.onlinesilkstore.androidmapsapplication;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearByPlaces extends AsyncTask<Object, String ,String> {
    private String googleplacedata, url;
    private GoogleMap mMap;

    @Override
    protected String doInBackground(Object... objects) {
        mMap=(GoogleMap)objects[0];
        url= (String)objects[1];
         Download_URL download_url=new Download_URL();
        try {
            googleplacedata=download_url.ReadURL(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleplacedata;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>> nearBYPlacesList=null;
        DataParser dataParser= new DataParser();
        nearBYPlacesList=dataParser.parse(s);
        DisplayNearPlaces(nearBYPlacesList);
    }

    private void DisplayNearPlaces(List<HashMap<String,String>> nearBYPlacesList)
    {
        for(int i=0;i<nearBYPlacesList.size();i++)
        {
            MarkerOptions markerOptions= new MarkerOptions();
            HashMap<String,String> google_near_places=nearBYPlacesList.get(i);
            String NAmeOfPlace=google_near_places.get("place_name");
            String vicinity=google_near_places.get("vicinity");
            double lat=Double.parseDouble(google_near_places.get("lat"));
            double lng=Double.parseDouble(google_near_places.get("long"));

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(NAmeOfPlace+":"+vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }
}
/*
package tk.onlinesilkstore.androidmapsapplication;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;




public class GetNearByPlaces extends AsyncTask<Object, String, String>
{
    private String googleplaceData, url;
    private GoogleMap mMap;

    @Override
    protected String doInBackground(Object... objects)
    {
        mMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        Download_URL downloadUrl = new Download_URL();
        try
        {
            googleplaceData = downloadUrl.ReadURL(url);//ReadTheURL
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return googleplaceData;
    }


    @Override
    protected void onPostExecute(String s)
    {
        List<HashMap<String, String>> nearByPlacesList = null;
        DataParser dataParser = new DataParser();
        nearByPlacesList = dataParser.parse(s);

        DisplayNearbyPlaces(nearByPlacesList);
    }


    private void DisplayNearbyPlaces(List<HashMap<String, String>> nearByPlacesList)
    {
        for (int i=0; i<nearByPlacesList.size(); i++)
        {
            MarkerOptions markerOptions = new MarkerOptions();

            HashMap<String, String> googleNearbyPlace = nearByPlacesList.get(i);
            String nameOfPlace = googleNearbyPlace.get("place_name");
            String vicinity = googleNearbyPlace.get("vicinity");
            double lat = Double.parseDouble(googleNearbyPlace.get("lat"));
            double lng = Double.parseDouble(googleNearbyPlace.get("lng"));


            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(nameOfPlace + " : " + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }
}
*/
