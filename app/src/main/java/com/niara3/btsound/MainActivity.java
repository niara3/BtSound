package com.niara3.btsound;

import android.app.*;
import android.bluetooth.*;
import android.content.*;
import android.os.*;
import android.util.*;
import java.util.*;

public class MainActivity extends Activity 
{
	private static final String TAG = "";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Log.d(TAG, "MainActivity#onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		showBt();

		EventCtrlService.kickInit(this);
	}

	@Override
	protected void onDestroy()
	{
		Log.d(TAG, "MainActivity#onDestroy");
		super.onDestroy();
	}

	private void showBt()
	{
		BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
		if (null == bta)
		{
			Log.e(TAG, "MainActivity#showBt getDefaultAdapter null");
			return;
		}
		Set devset = bta.getBondedDevices();
		if (null == devset)
		{
			Log.e(TAG, "MainActivity#showBt getBondedDevices null");
			return;
		}
		if (devset.isEmpty())
		{
			Log.d(TAG, "MainActivity#showBt BondedDevices empty");
			return;
		}
		Iterator<BluetoothDevice> devite = devset.iterator();
		if (null == devite)
		{
			Log.e(TAG, "MainActivity#showBt BondedDevices iterator null");
			return;
		}

		while (devite.hasNext())
		{
			BluetoothDevice btd = devite.next();
			if (null == btd) continue;
			Log.d(TAG, "MainActivity#showBt "
									+ " adr=" + btd.getAddress()
									+ " nm=" + btd.getName());
		}
	}
}
