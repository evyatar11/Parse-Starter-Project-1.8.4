package com.parse.starter;

import java.util.ArrayList;
import java.util.List;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.app.ProgressDialog;
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
//		createDatabase();
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
		gps = new GPSTracker(MainScreen.this);
		// check if GPS enabled
		if (gps.canGetLocation()) {
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			ParseGeoPoint point = new ParseGeoPoint(latitude, longitude);
			ParseQuery<Event> query = Event.getQuery();
//			query.whereWithinKilometers(Event.LOCATION_KEY, point, 3.0);
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

		ParseGeoPoint fikus = new ParseGeoPoint(32.113224,34.818085);
		Event event = new Event();
		event.setLocation(fikus);
		event.setName("Fikus");
		event.saveInBackground();
		ParseGeoPoint kirya = new ParseGeoPoint(32.114838, 34.817856);
		Event event2 = new Event();
		event2.setLocation(kirya);
		event2.setName("Kirya");
		event2.saveInBackground();
		Tab tab1 = new Tab();
		tab1.setParent(event);
		tab1.setName("Afeka Fikus News");
		Tab tab2 = new Tab();
		tab2.setParent(event);
		tab2.setName("Tremps");
		Tab tab3 = new Tab();
		tab3.setParent(event2);
		tab3.setName("Afeka Kirya News");
		Tab tab4 = new Tab();
		tab4.setParent(event2);
		tab4.setName("Tremps");
		tab1.saveInBackground();
		tab2.saveInBackground();
		tab3.saveInBackground();
		tab4.saveInBackground();
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
