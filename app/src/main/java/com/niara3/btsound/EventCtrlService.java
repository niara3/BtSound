package com.niara3.btsound;

import android.app.*;
import android.content.*;
import android.util.*;

public class EventCtrlService extends IntentService
{
	private static final String TAG = "";
	private static final String EXTRA_NAME_MODE = "EXTRA_MODE";
	private static final String EXTRA_MODE_INIT = "MODE_INIT";
	private static final String EXTRA_MODE_RECEIVER = "MODE_RECEIVER";

	public EventCtrlService() // public & no arg
	{
		super("EventCtrlService");
	}

	public static void kickInit(Context inContext)
	{
		if (null == inContext)
		{
			Log.e(TAG, "EventCtrlService#kickInit inContext null");
			return;
		}
		Intent intent = new Intent(inContext, EventCtrlService.class);
		intent.putExtra(EXTRA_NAME_MODE, EXTRA_MODE_INIT);
		try {
			inContext.startService(intent);
		} catch (SecurityException e) {
			Log.e(TAG, "EventCtrlService#kickInit startService", e);
		}
	}

	public static void kickReceiver(Context inContext)
	{
		if (null == inContext)
		{
			Log.e(TAG, "EventCtrlService#kickReceiver inContext null");
			return;
		}
		Intent intent = new Intent(inContext, EventCtrlService.class);
		intent.putExtra(EXTRA_NAME_MODE, EXTRA_MODE_RECEIVER);
		try {
			inContext.startService(intent);
		} catch (SecurityException e) {
			Log.e(TAG, "EventCtrlService#kickReceiver startService", e);
		}
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
		if ((0 == mode.compareTo(EXTRA_MODE_INIT)) ||
			(0 == mode.compareTo(EXTRA_MODE_RECEIVER)))
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

