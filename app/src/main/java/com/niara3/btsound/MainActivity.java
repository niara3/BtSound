package com.niara3.btsound;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;

public class MainActivity extends Activity 
{
	private static final String TAG = "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Log.d(TAG, "MainActivity#onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Intent intent = new Intent(this, EventCtrlService.class);
		intent.putExtra(
			EventCtrlService.EXTRA_NAME_MODE,
			EventCtrlService.EXTRA_MODE_INIT);
		try {
			this.startService(intent);
		} catch (SecurityException e) {
			Log.e(TAG, "MainActivity#onCreate startService", e);
		}
	}

	@Override
	protected void onDestroy()
	{
		Log.d(TAG, "MainActivity#onDestroy");
		super.onDestroy();
	}
}
