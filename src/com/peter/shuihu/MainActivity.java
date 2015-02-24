package com.peter.shuihu;

import com.peter.shuihu.MyMenu.ItemViewCreater;
import com.peter.shuihu.MyMenu.ItemViewOnClickListener;
import com.peter.shuihu.ShuiHu.Item;

import android.support.v4.util.LruCache;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue.IdleHandler;

public class MainActivity extends Activity {

	ViewPager mPager;
	private MyMenu mMenu;
    private int[] menuTitleRes = { 
    		R.string.action_dianjiang,
    		R.string.action_help, 
    		R.string.action_about
            };
	private static LruCache<Integer, Bitmap> mMemoryCache;
	private TextView mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showSplash();
	}

	private void showMainView() {
		setContentView(R.layout.main);
		initMenu();
	}

	private void showPager() {
		mPager = (ViewPager)findViewById(R.id.vp);
		mTitle = (TextView) findViewById(R.id.card_name);
		final String[] items = getResources().getStringArray(R.array.select_jiang);
		mTitle.setText(items[0]);
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory());
		final int cacheSize = maxMemory / 2;
		mMemoryCache = new LruCache<Integer, Bitmap>(cacheSize) {

			@Override
			protected int sizeOf(Integer key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}

			@Override
			protected void entryRemoved(boolean evicted, Integer key,
					Bitmap oldValue, Bitmap newValue) {
				if (evicted) {
					oldValue.recycle();
					System.gc();
				}
			}
		};
		ShuiHu sh = (ShuiHu) getApplication();
		ImageAdapter mAdapter = new ImageAdapter(sh.getItems());
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				mTitle.setText(items[position]);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		mPager.setAdapter(mAdapter);
	}
	
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.menu:
            mMenu.show();
            break;
        }
    }
	
	private void initMenu() {
		mMenu = new MyMenu(MainActivity.this);
        View anchor = findViewById(R.id.menu);
        mMenu.setAnchor(anchor);
        for (int i = 0; i < menuTitleRes.length; i++) {
            mMenu.addMenuItem(i, menuTitleRes[i], menuTitleRes[i]);
        }
        mMenu.setMenuItemCreater(new ItemViewCreater() {

            @Override
            public View createView(int position, ViewGroup parent) {
                LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                View menu = factory.inflate(R.layout.menu_item, parent, false);
                TextView tv = (TextView) menu.findViewById(R.id.text);
                tv.setText(menuTitleRes[position]);
                return menu;
            }
        });
        mMenu.setMenuItemOnClickListener(new ItemViewOnClickListener() {

            @Override
            public void OnItemClick(int order) {

                switch (order) {
                case R.string.action_dianjiang:
                	showList();
                    break;
                case R.string.action_help:
                	showToast(R.string.action_help_msg);
                    break;
                case R.string.action_about:
                	showToast(R.string.action_about_msg);
                    break;
                }
                mMenu.dismiss();
            }
        });
	}
	
    private void showToast(int StringId) {
        Toast toast = Toast.makeText(getApplicationContext(), StringId, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

	
	private void showSplash() {
		setContentView(new SplashView(MainActivity.this));
		IdleHandler handler = new IdleHandler() {

			@Override
			public boolean queueIdle() {
				ShuiHu sh = (ShuiHu) getApplication();
				sh.init();
				showMainView();
				showPager();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return false;
			}

		};
		Looper.myQueue().addIdleHandler(handler);
	}
	
	private void showList() {
		new AlertDialog.Builder(MainActivity.this)
        .setTitle(R.string.action_dianjiang)
        .setItems(R.array.select_jiang, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                /* User clicked so do some stuff */
            	mPager.setCurrentItem(which);
            }
        })
        .create().show();
	}
	
	public class SplashView extends TextView {

		public SplashView(Context context) {
			super(context);
			init(context);
		}

		private void init(Context context) {
			setText("Loading...");
			setTextColor(Color.WHITE);
			setTextSize(50);
			setBackgroundColor(Color.WHITE);
			setBackgroundResource(R.drawable.bg);
			setGravity(Gravity.CENTER);
		}
	}
	
	private class ImageAdapter extends PagerAdapter {

		SparseArray<Item> mItems;
		
		public ImageAdapter(SparseArray<Item> items) {
			mItems = items;
		}
		
		@Override
		public int getCount() {
			return mItems.size();
		}

		@Override
		public boolean isViewFromObject(final View arg0, final Object arg1) {
			return arg0 == ((RoateView) arg1).getCardView();
		}

		@Override
		public Object instantiateItem(final ViewGroup container,
				final int position) {
			
			Item item = mItems.get(position);
			RoateView view = new RoateView(MainActivity.this, container);
			
			Bitmap frontBm = mMemoryCache.get(item.mFront);
			if (frontBm == null) {
				frontBm = BitmapFactory.decodeResource(getResources(), item.mFront);
				mMemoryCache.put(item.mFront, frontBm);
			}
			view.setFrontResurect(frontBm);
			
			
			Bitmap backBm = mMemoryCache.get(item.mBack);
			if(backBm == null) {
				backBm = BitmapFactory.decodeResource(getResources(), item.mBack);
				mMemoryCache.put(item.mBack, backBm);
			}
			view.setBackResource(backBm);
			
			container.addView(view.getCardView());
			return view;
		}

		@Override
		public void destroyItem(final ViewGroup container, final int position,
				final Object object) {
			container.removeView(((RoateView) object).getCardView());
		}

		@Override
		public int getItemPosition(final Object object) {
			return POSITION_NONE;
		}
	}


}
