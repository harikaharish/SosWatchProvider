package com.harika.soswatchprovider.logic;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.harika.soswatchprovider.R;
import com.samsung.android.sdk.accessory.SAAgent;
import com.samsung.android.sdk.accessory.SAPeerAgent;
import com.samsung.android.sdk.accessory.SASocket;

public class SASafeWatchAgentImpl extends SAAgent {

	private static final String TAG = "SafeWatchAgentService";
	private static final String ALT_INI_RQST = "alert-initiate-req";
	HashMap<Integer, SASafeWatchConnection> mConnectionsMap = null;

	public interface JsonSerializable {
		public Object toJSON() throws JSONException;

		public void fromJSON(Object json) throws JSONException;
	}

	public SASafeWatchAgentImpl() {
		super(TAG, SASafeWatchConnection.class);
	}

	protected SASafeWatchAgentImpl(String arg0, Class<? extends SASocket> arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub

	}

	@Override
	protected void onFindPeerAgentResponse(SAPeerAgent arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	public boolean closeConnection() {
		if (mConnectionsMap != null) {
			final List<Integer> listConnections = new ArrayList<Integer>(
					mConnectionsMap.keySet());
			for (final Integer s : listConnections) {
				Log.i(TAG, "KEYS found are" + s);
				mConnectionsMap.get(s).close();
				mConnectionsMap.remove(s);
			}
		}
		return true;
	}

	@Override
	protected void onServiceConnectionResponse(SASocket uThisConnection,
			int result) {
		// TODO Auto-generated method stub
		Log.d(TAG, "Inside onServiceConnectionResponse");
		if (result == CONNECTION_SUCCESS) {
			if (uThisConnection != null) {
				final SASafeWatchConnection myConnection = (SASafeWatchConnection) uThisConnection;
				if (mConnectionsMap == null) {
					mConnectionsMap = new HashMap<Integer, SASafeWatchConnection>();
				}
				myConnection.mConnectionId = (int) (System.currentTimeMillis() & 255);
				Log.d(TAG, "onServiceConnection connectionID = "
						+ myConnection.mConnectionId);
				mConnectionsMap.put(myConnection.mConnectionId, myConnection);
				// String toastString = R.string.ConnectionEstablishedMsg + ":"
				// + uThisConnection.getRemotePeerId();
				Toast.makeText(getBaseContext(),
						R.string.ConnectionEstablishedMsg, Toast.LENGTH_LONG)
						.show();
			} else {
				Log.e(TAG, "SASocket object is null");
			}
		} else {
			Log.e(TAG, "onServiceConnectionResponse result error =" + result);
		}

	}

	private void onDataAvailableonChannel(String connectedPeerId,
			long channelId, String data) {
		Log.d(TAG, "Inside onDataAvailableonChannel");
		Log.d(TAG, data);
		Log.i(TAG, "incoming data on channel = " + channelId + ": from peer ="
				+ connectedPeerId);
		if (data.contains(ALT_INI_RQST)) {
			ProcessAlert pa = new ProcessAlert(this);
			pa.initiateAlert();
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public class LocalBinder extends Binder {
		public SASafeWatchAgentImpl getService() {
			return SASafeWatchAgentImpl.this;
		}
	}

	public class SASafeWatchConnection extends SASocket {

		public static final String TAG = "SASafeWatchConnection";
		private int mConnectionId;

		public SASafeWatchConnection() {
			super(SASafeWatchConnection.class.getName());
		}

		@Override
		public void onError(int channelId, String errorString, int error) {
			Log.e(TAG, "Connection is not alive ERROR: " + errorString + "  "
					+ error);
		}

		@Override
		public void onReceive(int channelId, byte[] data) {
			Log.i(TAG, "onReceive ENTER channel = " + channelId);
			final String strToUpdateUI = new String(data);
			onDataAvailableonChannel(String.valueOf(mConnectionId), channelId, // getRemotePeerId()
					strToUpdateUI);

		}

		@Override
		public void onServiceConnectionLost(int errorCode) {

			Log.e(TAG, "onServiceConectionLost  for peer = " + mConnectionId
					+ "error code =" + errorCode);
			if (mConnectionsMap != null) {
				mConnectionsMap.remove(mConnectionId);

			}
		}

	}
}
