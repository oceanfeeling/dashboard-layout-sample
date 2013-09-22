package com.example.dashboard;

public class HomeButtonInfo {
	private int mId;
	private int mText;
	private int mIcon;
	
	public HomeButtonInfo(int id, int text, int icon) {
		mId = id;
		mText = text;
		mIcon = icon;
	}
	
	public int getId() {
		return mId;
	}
	
	public int getText() {
		return mText;
	}
	
	public int getIcon() {
		return  mIcon;
	}
}
