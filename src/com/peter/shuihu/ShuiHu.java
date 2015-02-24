package com.peter.shuihu;

import android.app.Application;
import android.util.SparseArray;

public class ShuiHu extends Application{

	private boolean mHashInit = false;
	SparseArray<Item> mItems = new SparseArray<Item>(108);

	public void init() {
		if(mHashInit) {
			return;
		}
		
		for (int i = 0; i < 108; i++) {
			String frontName = "";
			String backName = "";
			int index = i + 1;
			if (index < 10) {
				frontName = "s00" + index + "_1";
				backName = "s00" + index + "_2";
			} else if (index < 100) {
				frontName = "s0" + index + "_1";
				backName = "s0" + index + "_2";
			} else if (index < 110) {
				frontName = "s" + index + "_1";
				backName = "s" + index + "_2";
			}
			
			Item item = new Item();
			item.mFront = getResourceId(frontName);
			item.mBack = getResourceId(backName);
			item.isFront = true;
			mItems.put(i, item);
		}
		
		mHashInit = true;
	}

	public SparseArray<Item> getItems() {
		if(!mHashInit) {
			init();
		}
		return mItems;
	}
	
	int getResourceId(String name) {
		return getResources().getIdentifier(name, "drawable", "com.peter.shuihu");
	}
	
	public static class Item {
		Integer mFront;
		Integer mBack;
		boolean isFront;
	}
	
}
