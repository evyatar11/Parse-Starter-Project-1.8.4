package com.parse.starter;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

public class MainScreen extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener,LocationListener {

    public static final String USER_ID_KEY = "userId";

    // GPSTracker class
    private static final String TAG = MainScreen.class.getName();
    private static String sUserId;
    private Event currentEvent;
    private List<Tab> list;
    private List<Post> postList;
    private ProgressDialog progress;
    private EditText etMessage;
    private Button btSend;
    private Button btt1, btt2;
    private ListView listView;
    private ArrayList<Post> listofposts;
    private ArrayAdapter<Post> adapter;
    private boolean tab1;
    private Location mCurrentLocation,mLastLocation;
    private Context context;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;
    private boolean mRequestingLocationUpdates=true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_android_example);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        progress = ProgressDialog.show(this, "Loading Data", "Please Wait",
                true);
        new Thread(new Runnable() {
            @Override
            public void run() {
//                createDatabase();
                buildGoogleApiClient();
//        createLocationRequest();
                mGoogleApiClient.connect();


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.dismiss();
                        getCurrentArea();
                        getListOfTabs();
                        btt1.setText(list.get(0).getName().toString());
                        btt2.setText(list.get(1).getName().toString());
                    }
                });
            }
        }).start();

    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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

    private void getListOfTabs() {
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

    private void getCurrentArea() {
        // check if GPS enabled
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
//            Toast.makeText(MainScreen.this,"lat "+ latitude+ "long "+ longitude,Toast.LENGTH_LONG ).show();
            ParseGeoPoint point = new ParseGeoPoint(latitude, longitude);
//            Toast.makeText(context,"lat "+ point.getLatitude()+ "long "+ point.getLongitude(),Toast.LENGTH_LONG ).show();

            Log.i("lat before",latitude+" ");
            Log.i("long before",longitude+" ");
            Log.i("lat after",point.getLatitude()+" ");
            Log.i("long after",point.getLongitude()+" ");
            ParseQuery<Event> query = Event.getQuery();
//           query.whereWithinKilometers(Event.LOCATION_KEY, point, 0.5);
            query.whereNear(Event.LOCATION_KEY, point);
//            query.setLimit(1);
            try {
                currentEvent = query.getFirst();
                getApplicationContext();
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

//    @Override
//    public void onConnected(Bundle bundle) {
////        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
////                mGoogleApiClient);
//        boolean mRequestingLocationUpdates=true;
//        if (mRequestingLocationUpdates) {
//            startLocationUpdates();
//        }
////        if (mLastLocation != null) {
////            Log.i("latitude",mLastLocation.getLatitude()+"");
////            Log.i("longitude",mLastLocation.getLongitude()+"");
////            }
//    }


    @Override
    public void onConnected(Bundle connectionHint) {

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        mLastLocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.i("latitude",mLastLocation.getLatitude()+"");
        Log.i("longitude",mLastLocation.getLongitude()+"");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            // Show dialog using GooglePlayServicesUtil.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    // The rest of this code is all about building the error dialog

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
//        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!mGoogleApiClient.isConnecting() &&
                        !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
//        mCurrentLocation = location;
//        Log.i("latitude",mCurrentLocation.getLatitude()+"");
//        Log.i("longitude",mCurrentLocation.getLongitude()+"");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GooglePlayServicesUtil.getErrorDialog(errorCode,
                    this.getActivity(), REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((MainScreen)getActivity()).onDialogDismissed();
        }
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
