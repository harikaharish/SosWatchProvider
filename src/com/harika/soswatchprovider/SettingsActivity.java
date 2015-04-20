package com.harika.soswatchprovider;

import java.util.List;

import com.harika.soswatchprovider.db.StealthAlertDAO;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	private Context context;
	Button phoneContact;
	EditText email;
	EditText timeInterval;
	EditText frequency;
	CheckBox emailChecked;
	CheckBox smsChecked;
	TextView phoneNumber;
	StealthAlertDAO dao;
	final int CONTACT_PICKER_RESULT = 1;
	private final String TAG = "SettingActivity";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		context = this;
		
		//Up button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		email = (EditText) findViewById(R.id.editText2);
		frequency = (EditText) findViewById(R.id.editText3);
		timeInterval = (EditText) findViewById(R.id.editText5);
		smsChecked = (CheckBox) findViewById(R.id.checkBox1);
		emailChecked = (CheckBox) findViewById(R.id.checkBox2);
		Button changePin = (Button) findViewById(R.id.button1);
		Button save = (Button) findViewById(R.id.button2);
		phoneContact = (Button) findViewById(R.id.button3);
		phoneNumber = (TextView) findViewById(R.id.phoneNumber);
		
		dao = new StealthAlertDAO(context);
		List<String> values = dao.getSettings();
		if(values != null){			
			phoneNumber.setText(values.get(0));
			email.setText(values.get(1));
			if(TextUtils.equals("Yes", values.get(2)))
				emailChecked.setChecked(true);
			if(TextUtils.equals("Yes", values.get(3)))
				smsChecked.setChecked(true);
			frequency.setText(values.get(4));
			timeInterval.setText(values.get(5));
		}
		phoneContact.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);  
				startActivityForResult(intent, 1);
			}
		});			
		
		save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Reset errors.
				timeInterval.setError(null);
				frequency.setError(null);
				email.setError(null);
				phoneNumber.setError(null);
				
				boolean cancel = false;
				View focusView = null;

				if (TextUtils.isEmpty(timeInterval.getText())) {
					timeInterval.setError(getString(R.string.invalidColumn));
					focusView = timeInterval;
					cancel = true;
					Log.d(TAG, "timeInterval");
				}
				
				if (TextUtils.isEmpty(frequency.getText())) {
					frequency.setError(getString(R.string.invalidColumn));
					focusView = frequency;
					cancel = true;
					Log.d(TAG, "frequency");
				}
				
				if (TextUtils.isEmpty(email.getText())) {
					email.setError(getString(R.string.invalidColumn));
					focusView = email;
					cancel = true;
					Log.d(TAG, "email");
				} 
				
				if (TextUtils.isEmpty(phoneNumber.getText())) {
					phoneNumber.setError(getString(R.string.invalidColumn));
					focusView = phoneNumber;
					cancel = true;
					Log.d(TAG, "phoneNumber");
				}				
				
				if(cancel){
					focusView.requestFocus();
				} else {
					ContentValues values = new ContentValues();
					values.put(getResources().getString(R.string.phoneColumn), phoneNumber.getText().toString());
					values.put(getResources().getString(R.string.emailColumn), email.getText().toString());
					values.put(getResources().getString(R.string.emailmodeColumn), emailChecked.isChecked()?"Yes":"No");
					values.put(getResources().getString(R.string.smsmodeColumn), smsChecked.isChecked()?"Yes":"No");
					values.put(getResources().getString(R.string.frequencyColumn), frequency.getText().toString());
					values.put(getResources().getString(R.string.intervalColumn), timeInterval.getText().toString());				
					dao.updateSettings(values);
					Toast.makeText(SettingsActivity.this, "Settings saved",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(context, MainActivity.class);
					startActivity(intent);
					finish();
				}
			}

		});		
		
		changePin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(context, ChangePinActivity.class);
				startActivity(intent);
			}
		});
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
            case CONTACT_PICKER_RESULT:
                // handle contact results
				String phoneNo = null ;
				Uri uri = data.getData();
				Cursor cursor = getContentResolver().query(uri, null, null, null, null);
				cursor.moveToFirst();
				int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
				phoneNo = cursor.getString(phoneIndex);
				Log.d(TAG, "phoneno: "+phoneNo);
				phoneNumber.setText(phoneNo);
                break;
            }

        } else {
            // gracefully handle failure
            Log.d(TAG, "Warning: activity result not ok");
        }
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "up button clicked");
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	    	Log.d(TAG, "mainactivity started");
			Intent intent = new Intent(context, MainActivity.class);
			startActivity(intent);
			finish();
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(context, MainActivity.class);
		startActivity(intent);
		finish();
	}
	
}
