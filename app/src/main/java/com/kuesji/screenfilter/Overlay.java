package com.kuesji.screenfilter;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.RelativeLayout;


public class Overlay extends AccessibilityService {

	RelativeLayout root;

	protected void onServiceConnected() {
		super.onServiceConnected();

		root = new RelativeLayout(this);
		root.setBackgroundColor(Color.argb(getSharedPreferences("settings",Context.MODE_PRIVATE).getInt("color",150),0,0,0));

		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

		int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
		flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		flags |= WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS;
		flags |= WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;

		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
		 WindowManager.LayoutParams.MATCH_PARENT,
		 WindowManager.LayoutParams.MATCH_PARENT,
		 WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
		 flags,
		 PixelFormat.TRANSLUCENT
		);

		wm.addView(root,lp);
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		if( root != null ) {
			root.setBackgroundColor(Color.argb(getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("color", 150), 0, 0, 0));
		}

		return super.onStartCommand(intent, flags, startId);
	}

	public void onAccessibilityEvent(AccessibilityEvent event) {
	}

	public void onInterrupt() {

	}
}
