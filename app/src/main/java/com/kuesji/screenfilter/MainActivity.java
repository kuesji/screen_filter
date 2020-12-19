package com.kuesji.screenfilter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

public class MainActivity extends Activity {

	SharedPreferences.Editor editor;
	LinearLayout root;
	SeekBar seekbar;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		editor = getSharedPreferences("settings", Context.MODE_PRIVATE).edit();

		root = new LinearLayout(this);
		root.setOrientation(LinearLayout.VERTICAL);
		root.setGravity(Gravity.CENTER);

		ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.leftMargin = convertDpToPx(32);
		lp.rightMargin = convertDpToPx(32);

		seekbar = new SeekBar(this);
		seekbar.setLayoutParams(lp);
		seekbar.setMax(230);
		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				editor.putInt("color",seekBar.getMax()-progress);
				editor.commit();
				startService(new Intent(MainActivity.this,Overlay.class));
			}

			public void onStartTrackingTouch(SeekBar seekBar) { }
			public void onStopTrackingTouch(SeekBar seekBar) { }
		});
		root.addView(seekbar);

		setContentView(root);
	}

	@Override
	protected void onStart() {
		super.onStart();

		seekbar.setEnabled(false);
		root.setOnClickListener((View v)->{
			try {
				Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
				startActivity(intent);

				Toast.makeText(getBaseContext(),"please enable \"screen filter\" service first",Toast.LENGTH_SHORT).show();
			} catch (Exception e){
				Toast.makeText(MainActivity.this,"weird. no activity found to handle accessibility service", Toast.LENGTH_SHORT);
			}
		});

		try {
			ComponentName componentName = new ComponentName(this, Overlay.class);
			String services_list = Settings.Secure.getString(getContentResolver(), "enabled_accessibility_services");
			String[] services = services_list.split(":");
			for (String service : services) {
				ComponentName fromString = ComponentName.unflattenFromString(service);
				if (fromString != null && fromString.equals(componentName)) {
					seekbar.setEnabled(true);
					root.setOnClickListener((v)->{});
					break;
				}
			}
		} catch (Exception e){}

		SharedPreferences preferences = getSharedPreferences("settings",Context.MODE_PRIVATE);
		seekbar.setProgress( seekbar.getMax() - preferences.getInt("color",50));
	}

	public int convertDpToPx(float dp) {
		return (int)(dp * getResources().getDisplayMetrics().density);
	}
}
