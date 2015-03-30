package com.parse.starter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evia on 3/30/2015.
 */
public class FirstFragment extends Fragment implements View.OnClickListener {
    // Store instance variables
    private static final String TAG = "FirstFragment";
    private String title;
    private int page;
    private ArrayAdapter<Post> adapter;
    private List<Post> postList;
    private Tab tab;
    private TabHandler tabHandler;
    private TextView etMessage;
    private Button btMessage;
    private List<Tab> list;

    // newInstance constructor for creating fragment with arguments
    public static FirstFragment newInstance(int page) {
        FirstFragment fragmentFirst = new FirstFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("TabPosition", page);
        fragmentFirst.setArguments(bundle);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("TabPosition", 0);

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.firstpage, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list);
        btMessage = (Button)view.findViewById(R.id.btSend);
        etMessage = (TextView)view.findViewById(R.id.etMessage);
        btMessage.setOnClickListener(this);
        tab = tabHandler.getTab(page);

        postList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, postList);
        listView.setAdapter(adapter);
        getListOfPosts(tab);
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            tabHandler = (TabHandler) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("containing activity must implements tab handler");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        tabHandler = null;
    }

    private void getListOfPosts(Tab tab) {
        ParseQuery<Post> query = Post.getQuery();
        query.whereEqualTo(Post.PARENT_TAB, tab);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e == null) {
                    postList.addAll(posts);
                    adapter.notifyDataSetChanged();
                }else{
                    Log.e(TAG,"CANNOT RETRIEVE POST FROM SERVER"+e.getMessage());
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btSend) {
            String data = etMessage.getText().toString();
            Post message = new Post();
            message.setBody(ParseUser.getCurrentUser().getUsername() + ":\n" + data);
            postList.add(message);
            adapter.notifyDataSetChanged();
            message.saveInBackground();
        }
    }
}