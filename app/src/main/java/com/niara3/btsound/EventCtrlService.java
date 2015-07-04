package com.niara3.btsound;

import android.app.*;
import android.content.*;
import android.util.*;

public class EventCtrlService extends IntentService
{
	private static final String TAG = "";
	public static final String EXTRA_NAME_MODE = "EXTRA_MODE";
	public static final String EXTRA_MODE_INIT = "MODE_INIT";
	
	public EventCtrlService() // public & no arg
	{
		super("EventCtrlService");
	}

	@Override
	public void onCreate()
	{
		Log.d(TAG, "EventCtrlService#onCreate");
		super.onCreate();
	}

	@Override
	public void onDestroy()
	{
		Log.d(TAG, "EventCtrlService#onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onHandleIntent(Intent inIntent)
	{
		if (null == inIntent)
		{
			Log.e(TAG, "EventCtrlService#onHandleIntent intent null");
			return;
		}
		String mode = inIntent.getStringExtra(EXTRA_NAME_MODE);
		if (null == mode)
		{
			Log.e(TAG, "EventCtrlService#onHandleIntent extra mode null");
			return;
		}
		if (0 == mode.compareTo(EXTRA_MODE_INIT))
		{
			Log.d(TAG, "EventCtrlService#onHandleIntent " + mode);
		}
		else
		{
			Log.e(TAG, "EventCtrlService#onHandleIntent unknown mode");
			return;
		}
	}
}

