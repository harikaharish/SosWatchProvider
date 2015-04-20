package com.harika.soswatchprovider.logic;

import java.util.Calendar;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;

public class ProcessAlert {
	private long startTime = 0;
	private LocationManager lm;
	private LocationListener ll;
	private final int maxCount = 1;
	private static final String TAG = "ProcessAlert";
	private Context context;
	private SharedPreference sharedPreference;
	public ProcessAlert(Context context) {
		this.context = context;
	}

	public void initiateAlert() {
		// TODO Auto-generated method stub
		Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(500);
		lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		ll = new Mylocationlistener();
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
	}
	
	public enum Day
	{
	  SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY 
	}
	
	public enum Months
	{
	  JAN, FEB, MAR, APR, MAY, JUN,
	  JULY, AUG, SEPT, OCT, NOV, DEC
	}
			
	private class Mylocationlistener implements LocationListener {
		int count = 0;
		StringBuilder locationUpdate = new StringBuilder();

		@Override
		public void onLocationChanged(Location location) {
			Log.d("onLocationChanged", "Enter");
			long timer = System.currentTimeMillis() - startTime;
			if (timer >= 60000) {
				startTime = System.currentTimeMillis();
				Log.d("Main", "Trying to send mail for every minute:"
						+ locationUpdate.toString());				
				Log.d("LOCATION CHANGED", location.getLatitude() + "");
				Log.d("LOCATION CHANGED", location.getLongitude() + "");
				count++;
				/*
				 * Toast.makeText(MainActivity.this, location.getLatitude()
				 * + "" + location.getLongitude(),
				 * Toast.LENGTH_LONG).show();
				 */
				Calendar c = Calendar.getInstance();
				int seconds = c.get(Calendar.SECOND);
				int minutes = c.get(Calendar.MINUTE);
				int hours = c.get(Calendar.HOUR_OF_DAY);
				int day = c.get(Calendar.DATE);
				int month = c.get(Calendar.MONTH);
				locationUpdate.append(hours + ":" + minutes + ":" + seconds+" " 
						+ Months.values()[month] + " " + day +":"
						+ "http://maps.google.com/?q="
						+ location.getLatitude() + ","
						+ location.getLongitude());
				Log.d("Main", "Trying to send sms:" + locationUpdate.toString());
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage("+18123617599", null,
				locationUpdate.toString(), null, null);
				JavaEmail email = new JavaEmail();
				String[] toEmails = { "mail.iamharish@gmail.com" };
				String emailSubject = "IMP: Alert APP Email";
				try {
					email.createEmailMessage(toEmails, emailSubject,
							locationUpdate.toString());
					email.execute();
				} catch (AddressException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (count == maxCount) {
					Log.d("Main", "Ending location update activity");
					lm.removeUpdates(ll);
				}
			}
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
	}			
}
