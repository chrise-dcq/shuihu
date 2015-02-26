package com.peter.shuihu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SplashActivity extends Activity {

	int count = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		findViewById(R.id.splash).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TextView tv = (TextView) v;
				count++;
				String str = "";
				if(count >= 15){
					str = "Peter love Lily!";
				}else if(count >= 10) {
					str = "Peter want Lily!";
				}else if(count >= 5) {
					str = "Peter miss Lily!";
				}
				tv.setText(str);
			}
		});
	}
	
}
