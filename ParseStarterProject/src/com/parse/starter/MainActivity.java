package com.parse.starter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MainActivity extends FragmentActivity implements TabHandler {




    public enum TabsIndex {NEWS,TREMPS,SECONDHAND}
    private Event currentEvent;
    private static List<Tab> tabList;
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(),
                "User Successfully Logged out",
                Toast.LENGTH_LONG).show();
        ParseUser.logOut();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100);
        mLocationRequest.setFastestInterval(1000);
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
//                createDatabase();

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

        ParseGeoPoint fikus = new ParseGeoPoint(32.11334, 34.818058);
        ParseGeoPoint kirya  = new ParseGeoPoint(32.114766, 34.818233);
        ParseGeoPoint home = new ParseGeoPoint(32.329041, 34.863829);
        Event event1 = new Event();
        Event event2 = new Event();
        Event event3 = new Event();
        Tab tab1 = new Tab();
        Tab tab2 = new Tab();
        Tab tab3 = new Tab();
        Tab tab4 = new Tab();
        Tab tab5 = new Tab();
        Tab tab6 = new Tab();
        Tab tab7 = new Tab();
        Tab tab8 = new Tab();
        Tab tab9 = new Tab();
        event1.setLocation(fikus);
        event2.setLocation(kirya);
        event3.setLocation(home);
        event1.setName("Fikus");
        event2.setName("Kirya");
        event3.setName("Home");
        event1.saveInBackground();
        event2.saveInBackground();
        event3.saveInBackground();
        tab1.setParent(event1);
        tab2.setParent(event1);
        tab3.setParent(event1);
        tab1.setName("Fikus News");
        tab2.setName("Fikus Tremps");
        tab3.setName("Fikus SecondHand");
        tab4.setParent(event2);
        tab5.setParent(event2);
        tab6.setParent(event2);
        tab4.setName("Kirya News");
        tab5.setName("Kirya Tremps");
        tab6.setName("Kirya SecondHand");
        tab7.setParent(event3);
        tab8.setParent(event3);
        tab9.setParent(event3);
        tab7.setName("Home News");
        tab8.setName("Home Tremps");
        tab9.setName("Home SecondHand");
        tab1.saveInBackground();
        tab2.saveInBackground();
        tab3.saveInBackground();
        tab4.saveInBackground();
        tab5.saveInBackground();
        tab6.saveInBackground();
        tab7.saveInBackground();
        tab8.saveInBackground();
        tab9.saveInBackground();
    }

    @Override
    public Tab getTab(int position) {
        return tabList.get(position);
    }




    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

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
                    return FirstFragment.newInstance(TabsIndex.NEWS.ordinal());
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return FirstFragment.newInstance(TabsIndex.TREMPS.ordinal());
                case 2: // Fragment # 1 - This will show SecondFragment
                    return FirstFragment.newInstance(TabsIndex.SECONDHAND.ordinal());
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
//            return tabList.get(position).getName();
            return tabList.get(position).getName().toString();
        }


    }
}