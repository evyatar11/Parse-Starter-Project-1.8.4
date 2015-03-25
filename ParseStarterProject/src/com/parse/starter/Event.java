package com.parse.starter;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@ParseClassName("Event")
public class Event extends ParseObject {
    public static final String NAME_KEY = "name";
    public static final String LOCATION_KEY = "location";

    public Event() {
    }
    public Event(String name,ParseGeoPoint location){
    	setName(name);
    	setLocation(location);
    }

    public String getName() {
        return getString(NAME_KEY);
    }

    public void setName(String name) {
        put(NAME_KEY, name);
    }

    public ParseGeoPoint getLocation() {
        return getParseGeoPoint(LOCATION_KEY);
    }

    public LatLng getLatLngLocation() {
        ParseGeoPoint geoPoint = getLocation();
        return new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
    }

    public void setLocation(ParseGeoPoint geoPoint) {
        put(LOCATION_KEY, geoPoint);
    }
    public void setLocation(LatLng latLng) {
        put(LOCATION_KEY, new ParseGeoPoint(latLng.latitude, latLng.longitude));
    }


    public static ParseQuery<Event> getQuery() {
        return ParseQuery.getQuery(Event.class);
    }

}