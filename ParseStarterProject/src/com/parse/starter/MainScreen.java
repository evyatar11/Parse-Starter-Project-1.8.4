package com.parse.starter;

import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainScreen extends Activity {

    public static final String USER_ID_KEY = "userId";

    // GPSTracker class
    private Event currentEvent;
    private List<Tab> list;
    private List<Post> postList;
    private EditText etMessage;
    private Button btSend;
    private Button btt1, btt2;
    private ListView listView;
    private ArrayList<Post> listofposts;
    private ArrayAdapter<Post> adapter;
    private boolean tab1;
    private LocationRequest mLocationRequest;
    private Location currentLocation;
    private GoogleApiClient mGoogleApiClient;
    private boolean check;
    private ProgressDialog progress;

    public MainScreen() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        init();
        createDatabase();
        //google play request location update parameters setup
        AsyncTask task = new AsyncTask() {
            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);

            }

            @Override
            protected void onPreExecute (){
                progress = ProgressDialog.show(MainScreen.this, "Loading Data", "Please Wait",
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
                                if(check){
                                    currentLocation=location;
                                    Log.i("listennnnn", location.getLatitude()+"");
                                    Log.i("listennnnn", location.getLongitude()+"");
                                    check=false;
                                    setCurrentArea();
                                    setListOfTabs();
                                    mGoogleApiClient.disconnect();
                                    currentLocation.getLatitude();
                                    currentLocation.getLongitude();
                                    btt1.setText(list.get(0).getName().toString());
                                    btt2.setText(list.get(1).getName().toString());
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
        setContentView(R.layout.activity_list_view_android_example);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        check=true;
        btt1 = (Button) findViewById(R.id.tab1);
        btt2 = (Button) findViewById(R.id.tab2);
        etMessage = (EditText) findViewById(R.id.etMessage);
        btSend = (Button) findViewById(R.id.btSend);
        listView = (ListView) findViewById(R.id.list);
        btt1.setOnClickListener(new TabListener());
        btt2.setOnClickListener(new TabListener());
        btSend.setOnClickListener(new TabListener());
        listofposts = new ArrayList<Post>();
        listView.setAdapter(adapter);
        adapter = new ArrayAdapter<Post>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,
                listofposts);
    }


    private void getPostsInCurrentTab(Tab tab) {
        ParseQuery<Post> query = Post.getQuery();
        query.whereEqualTo(Post.PARENT_TAB, tab);
        try {
            postList = query.find();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void setListOfTabs() {
        ParseQuery<Tab> query = Tab.getQuery();
        query.whereEqualTo(Tab.PARENT_TAB, currentEvent);
        try {
            list = query.find();
            Log.d("cell[0]", list.get(0).getName().toString());
            Log.d("cell[1]", list.get(1).getName().toString());
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

    }

    private void setCurrentArea() {
        // check if GPS enabled
        double latitude = currentLocation.getLatitude();
        double longitude = currentLocation.getLongitude();
        ParseGeoPoint point = new ParseGeoPoint(latitude, longitude);

        Log.i("lat before",latitude+" ");
        Log.i("long before",longitude+" ");
        Log.i("lat after",point.getLatitude()+" ");
        Log.i("long after",point.getLongitude()+" ");
        ParseQuery<Event> query = Event.getQuery();
//        query.whereWithinKilometers(Event.LOCATION_KEY, point, 0.5);
        query.whereNear(Event.LOCATION_KEY, point);
        query.setLimit(1);
        try {
            currentEvent = query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }



    }

    private void createDatabase() {

        ParseGeoPoint laniado = new ParseGeoPoint(32.345063,34.857255);
        ParseGeoPoint gaash = new ParseGeoPoint(32.326161,34.847946);
        ParseGeoPoint sironit = new ParseGeoPoint(32.330199, 34.848404);
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
        tab1.saveInBackground(); tab2.saveInBackground();tab3.saveInBackground();tab4.saveInBackground();
        tab5.saveInBackground(); tab6.saveInBackground();tab7.saveInBackground();tab8.saveInBackground();
        tab9.saveInBackground();tab10.saveInBackground();
    }




    class TabListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tab1) {
                tab1 = true;
                adapter.clear();
                getPostsInCurrentTab(list.get(0));
                Log.d("tag", "postlist");
                for (int i = 0; i < postList.size(); i++) {
                    listofposts.add(postList.get(i));
                    adapter.notifyDataSetChanged();

                }
                listView.setAdapter(adapter);
            }
            if (v.getId() == R.id.tab2) {
                tab1 = false;
                adapter.clear();
                getPostsInCurrentTab(list.get(1));
                for (int i = 0; i < postList.size(); i++) {
                    listofposts.add(postList.get(i));
                    adapter.notifyDataSetChanged();

                }
                listView.setAdapter(adapter);
            }
            if (v.getId() == R.id.btSend) {
                String data = etMessage.getText().toString();
                Post message = new Post();
                message.setBody(data);
                message.setAuthor(ParseUser.getCurrentUser());
                if (tab1 == true)
                    message.setParent(list.get(0));
                else
                    message.setParent(list.get(1));
                try {
                    message.save();
                    listofposts.add(message);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                    Toast.makeText(MainScreen.this,
                            "Successfully created message on Parse",
                            Toast.LENGTH_SHORT).show();
                } catch (ParseException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                etMessage.setText("");
            }
        }
    }
}
