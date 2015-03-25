package com.parse.starter;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("Tab")
public class Tab extends ParseObject {

	private static final String NAME_KEY = "Name";
	public static final String PARENT_TAB = "parent";

	public Tab() {

	}

	public Tab(Event event,String name){
		setName(name);
		setParent(event);
	}
	public static ParseQuery<Tab> getQuery() {
        return ParseQuery.getQuery(Tab.class);
    }
	public void setName(String name) {
		put(NAME_KEY, name);
	}

	public void setParent(Event parent) {
		put(PARENT_TAB, parent);
	}

	public String getName() {
		return getString(NAME_KEY);
	}

	public String getParent() {
		return getString(PARENT_TAB);
	}

}