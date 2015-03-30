package com.parse.starter;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("Post")
public class Post extends ParseObject {
	private static final String TITLE_KEY = "title";
	private static final String BODY_KEY = "body";
	public static final String PARENT_TAB = "parent";
	private static final String AUTHOR_KEY= "author";

	public Post() {

	}
	
	public Post(Tab tab){
		setParent(tab);
	}

	public static ParseQuery<Post> getQuery() {
		return ParseQuery.getQuery(Post.class);
	}

	public void setAuthor(String user){
		put(AUTHOR_KEY,user);
	}
	public void setTitle(String title) {
		put(TITLE_KEY, title);
	}

	public void setBody(String body) {
		put(BODY_KEY, body);
	}

	public void setParent(Tab parent) {
		put(PARENT_TAB, parent);
	}

	public String getTitle() {
		return getString(TITLE_KEY);
	}

	public String getBody() {
		return getString(BODY_KEY);
	}

	public String getParent() {
		return getString(PARENT_TAB);
	}
	public String toString(){
		return getBody();
	}

}