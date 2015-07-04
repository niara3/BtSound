package com.niara3.btsound;

import android.bluetooth.*;
import android.content.*;
import android.util.*;

public class EventReceiver extends BroadcastReceiver
{
	private static final String TAG = "";

	@Override
	public void onReceive(Context inContext, Intent inIntent)
	{
		if (null == inContext)
		{
			Log.e(TAG, "EventReceiver#onReceive context null");
			return;
		}
		if (null == inIntent)
		{
			Log.e(TAG, "EventReceiver#onReceive intent null");
			return;
		}
		String act = inIntent.getAction();
		if (null == act)
		{
			Log.e(TAG, "EventReceiver#onReceive action null");
			return;
		}
		Log.d(TAG, "EventReceiver#onReceive " + act);
		if (0 == act.compareTo(BluetoothAdapter.ACTION_STATE_CHANGED))
		{
			BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
			if (null == bta)
			{
				Log.e(TAG, "EventReceiver#onReceive getDefaultAdapter null");
				return;
			}
			switch (bta.getState())
			{
			case BluetoothAdapter.STATE_TURNING_ON:
				Log.d(TAG, "EventReceiver#onReceive STATE_TURNING_ON");
				break;
			case BluetoothAdapter.STATE_ON:
				Log.d(TAG, "EventReceiver#onReceive STATE_ON");
				break;
			case BluetoothAdapter.STATE_TURNING_OFF:
				Log.d(TAG, "EventReceiver#onReceive STATE_TURNING_OFF");
				break;
			case BluetoothAdapter.STATE_OFF:
				Log.d(TAG, "EventReceiver#onReceive STATE_OFF");
				break;
			default:
				Log.d(TAG, "EventReceiver#onReceive STATE_unknown");
				break;
			}
		}
	}
}
