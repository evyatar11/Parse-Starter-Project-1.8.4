package com.parse.starter;

import java.util.ArrayList;
import java.util.List;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
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
	private GPSTracker gps;
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
    private Context context;


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progress != null) {
            progress.dismiss();
            progress = null;
        }
    }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_view_android_example);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        gps = new GPSTracker(getApplicationContext());
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
//              createDatabase();
                getCurrentArea();
                getListOfTabs();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						progress.dismiss();
						btt1.setText(list.get(0).getName().toString());
						btt2.setText(list.get(1).getName().toString());
					}
				});
			}
		}).start();


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
		if (gps.canGetLocation()) {
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
//            Toast.makeText(MainScreen.this,"lat "+ latitude+ "long "+ longitude,Toast.LENGTH_LONG ).show();
			ParseGeoPoint point = new ParseGeoPoint(latitude, longitude);
//            Toast.makeText(context,"lat "+ point.getLatitude()+ "long "+ point.getLongitude(),Toast.LENGTH_LONG ).show();

            Log.i("lat before",latitude+" ");
            Log.i("long before",longitude+" ");
            Log.i("lat after",point.getLatitude()+" ");
            Log.i("long after",point.getLongitude()+" ");
            ParseQuery<Event> query = Event.getQuery();
//            query.whereWithinKilometers(Event.LOCATION_KEY, point, 1.0);
			query.whereNear(Event.LOCATION_KEY, point);
			query.setLimit(1);
			try {
				currentEvent = query.getFirst();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
	}

	private void createDatabase() {

		ParseGeoPoint laniado = new ParseGeoPoint(32.345063,34.857255);
        ParseGeoPoint sironit = new ParseGeoPoint(32.330199, 34.848404);
        ParseGeoPoint zometNearHome = new ParseGeoPoint(32.329581, 34.862058);
        ParseGeoPoint home = new ParseGeoPoint(32.329041, 34.863829);
		Event event1 = new Event();
        Event event2 = new Event();
        Event event3 = new Event();
        Event event4 = new Event();
        Tab tab1 = new Tab();
        Tab tab2 = new Tab();
        Tab tab3 = new Tab();
        Tab tab4 = new Tab();
        Tab tab5 = new Tab();
        Tab tab6 = new Tab();
        Tab tab7 = new Tab();
        Tab tab8 = new Tab();
		event1.setLocation(laniado);
        event2.setLocation(sironit);
        event3.setLocation(zometNearHome);
        event4.setLocation(home);
		event1.setName("Laniado");
		event2.setName("sironit");
        event3.setName("zometNearHome");
        event4.setName("home");
        event1.saveInBackground();
		event2.saveInBackground();
        event3.saveInBackground();
        event4.saveInBackground();
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
		tab1.saveInBackground(); tab2.saveInBackground();tab3.saveInBackground();tab4.saveInBackground();
        tab5.saveInBackground(); tab6.saveInBackground();tab7.saveInBackground();tab8.saveInBackground();
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
