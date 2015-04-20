package com.harika.soswatchprovider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.harika.soswatchprovider.db.StealthAlertDAO;
import com.harika.soswatchprovider.logic.ProcessAlert;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		
		StealthAlertDAO dao = new StealthAlertDAO(this);
		if(dao.getPin() == 0){
			//No pin created by user. Divert to login page 
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();			
		}
		
		final ImageButton alertButton = (ImageButton) findViewById(R.id.imageButton1);
		alertButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alertButton.setImageResource(R.drawable.stop);
				ProcessAlert pa = new ProcessAlert(getBaseContext());
				pa.initiateAlert();	
				Toast.makeText(getBaseContext(), "Alert initiated", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			//Go through login screen
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void chooseContacts(MenuItem item){		
		Intent intent = new Intent(this, ContactsActivity.class);
		startActivity(intent);
	}	
}
