package tk.onlinesilkstore.androidmapsapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {
    private HashMap<String, String> getSinglePlace(JSONObject googlePlaceJSON)
    {
        HashMap<String,String> google_Place = new HashMap<>();
        String NameofPlace="-na-";
        String Vicinity="-na-";
        String latitude="";
        String longitude="";
        String reference="";
        try {
            if(!googlePlaceJSON.isNull("name")) {
                NameofPlace = googlePlaceJSON.getString("name");
            }
            if(!googlePlaceJSON.isNull("vicinity")) {
                Vicinity = googlePlaceJSON.getString("vicinity");
            }
            latitude=googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude=googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference=googlePlaceJSON.getString("reference");

            google_Place.put("place_name",NameofPlace);
            google_Place.put("vicinity",Vicinity);
            google_Place.put("lat",latitude);
            google_Place.put("long",longitude);
            google_Place.put("reference",reference);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return google_Place;

    }

    private List<HashMap<String, String>> getAllNearByPlace(JSONArray jsonArray){

        int counter=jsonArray.length();
        List<HashMap<String, String>> NearBYPlacesList= new ArrayList<>();
        HashMap<String,String> NearbyPlace=null;
        for(int i=0;i<counter;i++)
        {
            try {
                NearbyPlace=getSinglePlace((JSONObject)jsonArray.getJSONObject(i));
                NearBYPlacesList.add(NearbyPlace);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return  NearBYPlacesList;
    }

    public List<HashMap<String, String>> parse(String jsondata)
    {
        JSONArray jsonArray=null;
        JSONObject jsonObject;

        try {
            jsonObject=new JSONObject(jsondata);
            jsonArray=jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return  getAllNearByPlace(jsonArray);
    }
}
/*
package tk.onlinesilkstore.androidmapsapplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

*/
/**
 * Created by Coding Cafe on 7/18/2018.
 *//*


public class DataParser
{
    private HashMap<String, String> getSingleNearbyPlace(JSONObject googlePlaceJSON)
    {
        HashMap<String, String> googlePlaceMap = new HashMap<>();
        String NameOfPlace = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";

        try
        {
            if (!googlePlaceJSON.isNull("name"))
            {
                NameOfPlace = googlePlaceJSON.getString("name");
            }
            if (!googlePlaceJSON.isNull("vicinity"))
            {
                vicinity = googlePlaceJSON.getString("vicinity");
            }
            latitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googlePlaceJSON.getString("reference");

            googlePlaceMap.put("place_name", NameOfPlace);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return googlePlaceMap;
    }



    private List<HashMap<String, String>> getAllNearbyPlaces(JSONArray jsonArray)
    {
        int counter = jsonArray.length();

        List<HashMap<String, String>> NearbyPlacesList = new ArrayList<>();

        HashMap<String, String> NearbyPlaceMap = null;

        for (int i=0; i<counter; i++)
        {
            try
            {
                NearbyPlaceMap = getSingleNearbyPlace( (JSONObject) jsonArray.get(i) );
                NearbyPlacesList.add(NearbyPlaceMap);

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        return NearbyPlacesList;
    }



    public List<HashMap<String, String>> parse(String jSONdata)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try
        {
            jsonObject = new JSONObject(jSONdata);
            jsonArray = jsonObject.getJSONArray("results");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return getAllNearbyPlaces(jsonArray);
    }
}*/
