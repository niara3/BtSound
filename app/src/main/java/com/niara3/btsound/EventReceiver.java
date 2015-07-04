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

		if (0 == act.compareTo(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED))
		{
			actionBtaConnectionStateChagned(inContext, inIntent);
		}
		else if (0 == act.compareTo(BluetoothAdapter.ACTION_STATE_CHANGED))
		{
			actionBtaStateChagned(inContext);
		}
	}

	private void actionBtaConnectionStateChagned(Context inContext, Intent inIntent)
	{
		BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
		if (null == bta)
		{
			Log.e(TAG, "EventReceiver#actionBtaConnectionStateChagned getDefaultAdapter null");
			return;
		}
		BluetoothDevice btd = inIntent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		if (null == btd)
		{
			Log.e(TAG, "EventReceiver#actionBtaConnectionStateChagned EXTRA_DEVICE null");
		}
		else
		{
			Log.d(TAG, "EventReceiver#actionBtaConnectionStateChagned"
									+ " adr=" + btd.getAddress()
									+ " nm=" + btd.getName());
		}
		int state = inIntent.getIntExtra(
							BluetoothAdapter.EXTRA_CONNECTION_STATE,
							BluetoothAdapter.ERROR);
		switch (state)
		{
		case BluetoothAdapter.STATE_DISCONNECTED:
			Log.d(TAG, "EventReceiver#actionBtaConnectionStateChagned DISCONNECTED");
			EventCtrlService.kickReceiver(inContext);
			break;
		case BluetoothAdapter.STATE_CONNECTING:
			Log.d(TAG, "EventReceiver#actionBtaConnectionStateChagned CONNECTING");
			break;
		case BluetoothAdapter.STATE_CONNECTED:
			Log.d(TAG, "EventReceiver#actionBtaConnectionStateChagned CONNECTED");
			EventCtrlService.kickReceiver(inContext);
			break;
		case BluetoothAdapter.STATE_DISCONNECTING:
			Log.d(TAG, "EventReceiver#actionBtaConnectionStateChagned DISCONNECTING");
			break;
		default:	// BluetoothAdapter.ERROR
			Log.d(TAG, "EventReceiver#actionBtaConnectionStateChagned STATE_unknown");
			break;
		}
	}

	private void actionBtaStateChagned(Context inContext)
	{
		BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
		if (null == bta)
		{
			Log.e(TAG, "EventReceiver#actionBtaStateChagned getDefaultAdapter null");
			return;
		}
		switch (bta.getState())
		{
		case BluetoothAdapter.STATE_OFF:
			Log.d(TAG, "EventReceiver#actionBtaStateChagned OFF");
			EventCtrlService.kickReceiver(inContext);
			break;
		case BluetoothAdapter.STATE_TURNING_ON:
			Log.d(TAG, "EventReceiver#actionBtaStateChagned TURNING_ON");
			break;
		case BluetoothAdapter.STATE_ON:
			Log.d(TAG, "EventReceiver#actionBtaStateChagned ON");
			EventCtrlService.kickReceiver(inContext);
			break;
		case BluetoothAdapter.STATE_TURNING_OFF:
			Log.d(TAG, "EventReceiver#actionBtaStateChagned TURNING_OFF");
			break;
		default:
			Log.d(TAG, "EventReceiver#actionBtaStateChagned STATE_unknown");
			break;
		}
	}
}
