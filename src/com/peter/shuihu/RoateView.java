package com.peter.shuihu;

import com.peter.shuihu.RotateAnimation.InterpolatedTimeListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class RoateView implements InterpolatedTimeListener,OnClickListener{
	
	private boolean enableRefresh;
	private ImageView front, back;
	private RelativeLayout card;
	private int click_id;
	
	public RoateView(Context context, ViewGroup root) {
		View.inflate(context, R.layout.cardview, root);
		LayoutInflater factory = LayoutInflater.from(context);
		card = (RelativeLayout) factory.inflate(R.layout.cardview, root, false);
		front = (ImageView) card.findViewById(R.id.front);
		back = (ImageView) card.findViewById(R.id.back);
		front.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void interpolatedTime(float interpolatedTime) {
		if (enableRefresh && interpolatedTime > 0.5f) {
			setHint();
			enableRefresh = false;
		}
	}
	
	public void setFrontResurect(Bitmap frontBm) {
		front.setBackground(new BitmapDrawable(front.getResources(), frontBm));
	}
	
	public void setBackResource(Bitmap backBm) {
		back.setBackground(new BitmapDrawable(back.getResources(), backBm));
	}
	
	public View getCardView() {
		return card;
	}
	
	public void setHint() {
		front.setVisibility(View.GONE);
		back.setVisibility(View.GONE);

		if (click_id == R.id.front) {
			back.setVisibility(View.VISIBLE);
		} else if (click_id == R.id.back) {
			front.setVisibility(View.VISIBLE);
		}

	}
	
	public static float cX = 0.0f;
	public static float cY = 0.0f;
	
	@Override
	public void onClick(View v) {
		enableRefresh = true;
		RotateAnimation rotateAnim = null;
		if(cX == 0) {
			cX = card.getWidth() / 2.0f;
		}
		if(cY == 0) {
			cY = card.getHeight() / 2.0f;
		}

		click_id = v.getId();
		if (click_id == R.id.front) {
			rotateAnim = new RotateAnimation(cX, cY,
					RotateAnimation.ROTATE_DECREASE);
		} else if (click_id == R.id.back) {
			rotateAnim = new RotateAnimation(cX, cY,
					RotateAnimation.ROTATE_INCREASE);
		}

		if (rotateAnim != null) {
			rotateAnim.setInterpolatedTimeListener(this);
			rotateAnim.setFillAfter(true);
			rotateAnim.setFillBefore(false);
			card.startAnimation(rotateAnim);
		}
	}

}
