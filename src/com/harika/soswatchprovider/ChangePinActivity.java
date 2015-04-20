package com.harika.soswatchprovider;


import com.harika.soswatchprovider.db.StealthAlertDAO;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class ChangePinActivity extends Activity {
	private Context context;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changepin);
		context = this;
		final EditText oldPin = (EditText) findViewById(R.id.editText1);
		oldPin.requestFocus();
		final EditText newPin = (EditText) findViewById(R.id.editText2);
		Button changeButon = (Button) findViewById(R.id.button1);
		final StealthAlertDAO dao = new StealthAlertDAO(this); 
		changeButon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				String oldPinText = oldPin.getText().toString();
				String newPinText = newPin.getText().toString();
				oldPin.setError(null);
				newPin.setError(null);
				View focusView = null;
				boolean cancel = false;
				// Check for a valid password.
				if (TextUtils.isEmpty(newPinText)) {
					newPin.setError(getString(R.string.error_field_required));
					focusView = newPin;
					cancel = true;
				} else if (newPinText.length() < 4) {
					newPin.setError(getString(R.string.error_invalid_password));
					focusView = newPin;
					cancel = true;
				}				
				
				//Old pin is checked last so that focus can go to it first if both have errors
				if (TextUtils.isEmpty(oldPinText)) {
					oldPin.setError(getString(R.string.error_field_required));
					focusView = oldPin;
					cancel = true;
				} else if (oldPinText.length() < 4) {
					oldPin.setError(getString(R.string.error_invalid_password));
					focusView = oldPin;
					cancel = true;
				}	
				
				if(cancel){
					focusView.requestFocus();
				} else {
					if(Integer.parseInt(oldPinText) == dao.getPin()){
						dao.updatePin(Integer.parseInt(newPinText));
						Toast.makeText(ChangePinActivity.this, "Pin changed", Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(context, MainActivity.class);
						startActivity(intent);
					} else {
						oldPin.setError(getString(R.string.error_incorrect_password));
						oldPin.requestFocus();
					}
				}	
			}
			
		});
	}
}