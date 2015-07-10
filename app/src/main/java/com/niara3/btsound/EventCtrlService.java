package com.niara3.btsound;

import android.bluetooth.*;
import android.app.*;
import android.content.*;
import android.util.*;
import java.io.*;
import java.util.*;

public class EventCtrlService extends IntentService
{
	private static final String TAG = "";
	private static final String EMPTY_STRING = "";

// Advanced Audio Distribution Profile (A2DP) AudioSource 	0000110A-0000-1000-8000-00805F9B34FB
// Advanced Audio Distribution Profile (A2DP) AudioSink 	0000110B-0000-1000-8000-00805F9B34FB
	private static final String BTSERVER_NAME = "BtSoud a2dp";
	private static final UUID BTSERVER_UUID = UUID.fromString("0000110A-0000-1000-8000-00805F9B34FB");

	private static final String EXTRA_NAME_MODE = "EXTRA_MODE";
	private static final int EXTRA_MODE_UNKNOWN = -1;
	private static final int EXTRA_MODE_INIT = 1;				// "MODE_INIT";
	private static final int EXTRA_MODE_RECEIVER = 2;	// "MODE_RECEIVER";
	private static final int EXTRA_MODE_TERM = 3;			// "MODE_TERM";
	private static final String PREF_NAME = "pref";
	private static final String PREF_KEY_BTADR = "BTADR";

	private static final enum STATE {
		UNKNOWN,
		INIT,
		START,
		STOP,
		END,
	};

	private String mBtAdr;
	private BtAcceptThread mBtAcceptThread;

	public EventCtrlService() // public & no arg
	{
		super("EventCtrlService");
		mBtAdr = EMPTY_STRING;
		mBtAcceptThread = null;
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

	public static void kickTerm(Context inContext)
	{
		if (null == inContext)
		{
			Log.e(TAG, "EventCtrlService#kickTerm inContext null");
			return;
		}
		Intent intent = new Intent(inContext, EventCtrlService.class);
		intent.putExtra(EXTRA_NAME_MODE, EXTRA_MODE_TERM);
		try {
			inContext.startService(intent);
		} catch (SecurityException e) {
			Log.e(TAG, "EventCtrlService#kickTerm startService", e);
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
		int mode = inIntent.getIntExtra(EXTRA_NAME_MODE, EXTRA_MODE_UNKNOWN);
		switch (mode)
		{
		case EXTRA_MODE_INIT:
			Log.d(TAG, "EventCtrlService#onHandleIntent MODE_INIT");
			doInit();
			break;
		case EXTRA_MODE_RECEIVER:
			Log.d(TAG, "EventCtrlService#onHandleIntent MODE_RECEIVER");
			doReceiver();
			break;
		case EXTRA_MODE_TERM:
			Log.d(TAG, "EventCtrlService#onHandleIntent MODE_TERM");
			doTerm();
			break;
		default:	// EXTRA_MODE_UNKNOWN
			Log.e(TAG, "EventCtrlService#onHandleIntent unknown mode");
			break;
		}
	}

	private void doInit()
	{
		mBtAdr = EMPTY_STRING;

		SharedPreferences pref = getSharedPreferences(
														PREF_NAME,
														MODE_PRIVATE | MODE_MULTI_PROCESS);
		if (null == pref)
		{
			Log.w(TAG, "EventCtrlService#doInit pref null");
		}
		else
		{
			mBtAdr  = pref.getString(PREF_KEY_BTADR, null);
			if (null == mBtAdr)
			{
				Log.d(TAG, "EventCtrlService#doInit no BTADR");
				mBtAdr = EMPTY_STRING;
			}
			else
			{
				Log.d(TAG, "EventCtrlService#doInit BTADR=" + mBtAdr);
			}
		}

		startBtListen();
	}

	private void doReceiver()
	{
	}

	private void doTerm()
	{
		stopBtListen();
		
		SharedPreferences pref = getSharedPreferences(
														PREF_NAME,
														MODE_PRIVATE | MODE_MULTI_PROCESS);
		if (null == pref)
		{
			Log.w(TAG, "EventCtrlService#doTerm pref null");
		}
		else
		{
			SharedPreferences.Editor editor = pref.edit();
			if (null == editor)
			{
				Log.w(TAG, "EventCtrlService#doTerm pref editor null");
			}
			else
			{
				editor.putString(PREF_KEY_BTADR, "A0:DD:E5:E5:1E:33");
				editor.commit();
				Log.d(TAG, "EventCtrlService#doInit dummy BTADR write");
			}
		}
	}

	private void startBtListen()
	{
		stopBtListen();

		if (mBtAdr.isEmpty())
		{
			return;
		}

		mBtAcceptThread = new BtAcceptThread(mBtAdr);
		if (null == mBtAcceptThread)
		{
			return;
		}
		mBtAcceptThread.start();
	}

	private void stopBtListen()
	{
		if (null == mBtAcceptThread)
		{
			return;
		}

		mBtAcceptThread.reqStop();
		mBtAcceptThread = null;
	}

	private class BtAcceptThread extends Thread
	{
		private STATE mState = STATE.UNKNOWN;
		private BluetoothServerSocket mBtss;
		private String mBtAdr;	// 方針不明・・・

		public BtAcceptThread(String btadr)
		{
			mState = STATE.INIT;
			mBtss = null;
			mBtAdr = btadr;
		}

		public void run()
		{
			Log.d(TAG, "BtAcceptThread#run start");
			mState = STATE.START;

			doRun();

			mState = STATE.END;
			mBtss = null;
			Log.d(TAG, "BtAcceptThread#run end");
		}

		private void doRun()
		{
			mBtss = listen();
			if (null == mBtss)
			{
				Log.e(TAG, "BtAcceptThread#doRun mBtss null");
				return;
			}
			while (STATE.START == mState)
			{
				Log.d(TAG, "BtAcceptThread#doRun accept start");
				try
				{
					BluetoothSocket bts = mBtss.accept();
					BtInputThread btin = new BtInputThread(bts);
					btin.start();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				Log.d(TAG, "BtAcceptThread#doRun accept end");
			}

			Log.d(TAG, "BtAcceptThread#doRun close");
			try
			{
				mBtss.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		private BluetoothServerSocket listen()
		{
			BluetoothServerSocket btss = null;
			BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
			if (null == bta)
			{
				Log.e(TAG, "BtAcceptThread#listen getDefaultAdapter null");
				return null;
			}

			try
			{
				btss = bta.listenUsingRfcommWithServiceRecord(
								BTSERVER_NAME,
								BTSERVER_UUID);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

			return btss;
		}

		public void reqStop()
		{
			Log.d(TAG, "BtAcceptThread#reqStop");
			STATE state = mState;
			BluetoothServerSocket btss = mBtss;
			if (STATE.START != state)
			{
				Log.w(TAG, "BtAcceptThread#reqStop state=" + state);
				return;
			}
			else
			{
				mState = STATE.STOP;
			}
			if (null != btss)
			{
				Log.d(TAG, "BtAcceptThread#reqStop close");
				try
				{
					btss.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private class BtInputThread extends Thread
	{
		private STATE mState = STATE.UNKNOWN;
		private BluetoothSocket mBts;

		public BtInputThread(BluetoothSocket bts)
		{
			mState = STATE.INIT;
			mBts = bts;
		}

		public void run()
		{
			Log.d(TAG, "BtInputThread#run start");
			mState = STATE.START;

			doRun();

			mState = STATE.END;
			mBts = null;
			Log.d(TAG, "BtInputThread#run end");
		}

		private void doRun()
		{
			BluetoothSocket bts = mBts;
			if (null == bts)
			{
				Log.e(TAG, "BtInputThread#doRun BluetoothSocket null");
				return;
			}

			try
			{
				Thread.sleep(5000); //5000ミリ秒Sleepする
			}
			catch (InterruptedException e)
			{
			}

			try
			{
				bts.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
