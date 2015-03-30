package com.parse.starter;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import java.util.List;

public class MainActivity extends SetupUiKeyboard implements TabHandler {




    public enum TabsIndex {TREMPS,BABYSITTER,SECONDHAND}
    private Event currentEvent;
    private List<Tab> tabList;
    private List<Post> postList;
    private EditText etMessage;
    private Button btSend;
    private ListView listView;
    private ArrayAdapter<Post> adapter;
    private boolean tab1;
    private LocationRequest mLocationRequest;
    private Location currentLocation;
    private GoogleApiClient mGoogleApiClient;
    private boolean check = true;
    private ProgressDialog progress;
    private String current_user;
    private Bundle bundle;
    private FragmentPagerAdapter adapterViewPager;
    private ViewPager vpPager;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
//        setupUI(findViewById(R.id.parent));
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void init() {
        setContentView(R.layout.mainscreen);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        AsyncTask task = new AsyncTask() {
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);


            }

            @Override
            protected void onPreExecute() {
                progress = ProgressDialog.show(MainActivity.this, "Loading Data", "Please Wait",
                        true);

            }

            @Override
            protected Object doInBackground(Object[] params) {
                buildGoogleApiClient();
                createLocationRequest();
                mGoogleApiClient.connect();
                mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                if (check) {
                                    currentLocation = location;
                                    mGoogleApiClient.disconnect();
                                    check = false;
                                    setCurrentArea();
                                    setListOfTabs();
                                    adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
                                    vpPager.setAdapter(adapterViewPager);
                                    progress.dismiss();
                                }
                            }
                        });
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                    }
                });
                return null;
            }
        };
        task.execute();

    }

    private void setCurrentArea() {
        double latitude = currentLocation.getLatitude();
        double longitude = currentLocation.getLongitude();
        ParseGeoPoint point = new ParseGeoPoint(latitude, longitude);
        ParseQuery<Event> query = Event.getQuery();
        query.whereNear(Event.LOCATION_KEY, point);
        query.setLimit(1);
        try {
            currentEvent = query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setListOfTabs() {
        ParseQuery<Tab> query = Tab.getQuery();
        query.whereEqualTo(Tab.PARENT_TAB, currentEvent);
        try {
            tabList = query.find();
        } catch (ParseException e1) {
            e1.printStackTrace();
        }

    }

    private void createDatabase() {

        ParseGeoPoint laniado = new ParseGeoPoint(32.345063, 34.857255);
        ParseGeoPoint gaash = new ParseGeoPoint(32.228921, 34.8246);
        ParseGeoPoint sironit = new ParseGeoPoint(32.330314, 34.847775);
        ParseGeoPoint zometNearHome = new ParseGeoPoint(32.329581, 34.862058);
        ParseGeoPoint home = new ParseGeoPoint(32.329041, 34.863829);
        Event event1 = new Event();
        Event event2 = new Event();
        Event event3 = new Event();
        Event event4 = new Event();
        Event event5 = new Event();
        Tab tab1 = new Tab();
        Tab tab2 = new Tab();
        Tab tab3 = new Tab();
        Tab tab4 = new Tab();
        Tab tab5 = new Tab();
        Tab tab6 = new Tab();
        Tab tab7 = new Tab();
        Tab tab8 = new Tab();
        Tab tab9 = new Tab();
        Tab tab10 = new Tab();
        event1.setLocation(laniado);
        event2.setLocation(sironit);
        event3.setLocation(zometNearHome);
        event4.setLocation(home);
        event5.setLocation(gaash);
        event1.setName("Laniado");
        event2.setName("sironit");
        event3.setName("zometNearHome");
        event4.setName("home");
        event5.setName("gaash");
        event1.saveInBackground();
        event2.saveInBackground();
        event3.saveInBackground();
        event4.saveInBackground();
        event5.saveInBackground();
        tab1.setParent(event1);
        tab2.setParent(event1);
        tab1.setName("Laniado News");
        tab2.setName("Laniado Tremps");
        tab3.setParent(event2);
        tab4.setParent(event2);
        tab3.setName("Sironit News");
        tab4.setName("Sironit Tremps");
        tab5.setParent(event3);
        tab6.setParent(event3);
        tab5.setName("zometNearHome News");
        tab6.setName("zometNearHome Tremps");
        tab7.setParent(event4);
        tab8.setParent(event4);
        tab7.setName("home News");
        tab8.setName("home Tremps");
        tab9.setParent(event5);
        tab10.setParent(event5);
        tab9.setName("gaash News");
        tab10.setName("gaash Tremps");
        tab1.saveInBackground();
        tab2.saveInBackground();
        tab3.saveInBackground();
        tab4.saveInBackground();
        tab5.saveInBackground();
        tab6.saveInBackground();
        tab7.saveInBackground();
        tab8.saveInBackground();
        tab9.saveInBackground();
        tab10.saveInBackground();
    }

    @Override
    public Tab getTab(int position) {
        return tabList.get(position);
    }




    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return FirstFragment.newInstance(TabsIndex.TREMPS.ordinal());
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return FirstFragment.newInstance(TabsIndex.BABYSITTER.ordinal());
//                case 2: // Fragment # 1 - This will show SecondFragment
//                    return FirstFragment.newInstance(TabsIndex.SECONDHAND.ordinal());
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Tab " + position;
        }

    }
}