package com.harika.soswatchprovider.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.harika.soswatchprovider.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/**
 * TODOs DAO object.
 * 
 * @author Harish G
 *
 */
public class StealthAlertDAO {

	private SQLiteDatabase db;
	private StealthAlertSQLiteHelper dbHelper;
	private Context context;
	
	public StealthAlertDAO(Context context) {
		this.context = context;
		dbHelper = new StealthAlertSQLiteHelper(context);
		db = dbHelper.getWritableDatabase();
	}
	
	// Close the db
	public void close() {
		db.close();
	}
	
	public void createPin(int pin) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("pin", pin);
		db.insert("login", null, contentValues);
		Log.d("DAO","Created a new pin");
	}
	
	public void updatePin(int value){
		ContentValues values = new ContentValues();
		values.put("pin", value);
		db.insertWithOnConflict("login", null, values, SQLiteDatabase.CONFLICT_REPLACE);
		Log.d("DAO","Updated pin");
	}
	
	public void updateSettings(ContentValues values) {
		db.update("settings", values, null, null);
		Log.d("DAO","Saved setting");
	}
	
	public List<String> getSettings() {
		String[] tableColumns = new String[] {context.getResources().getString(R.string.phoneColumn),
				context.getResources().getString(R.string.emailColumn),
				context.getResources().getString(R.string.emailmodeColumn),
				context.getResources().getString(R.string.smsmodeColumn),
				context.getResources().getString(R.string.frequencyColumn),
				context.getResources().getString(R.string.intervalColumn)};
		
		
		ArrayList<String> array = new ArrayList<String>();
		Cursor cursor = db.query("settings", tableColumns, null, null, null, null, null);
		cursor.moveToFirst();
		// Iterate the results
	    if (!cursor.isAfterLast()) {
	    	Log.d("DAO","Retrived setting");
	    	array.add(cursor.getString(0));
	    	array.add(cursor.getString(1));
	    	array.add(cursor.getString(2));
	    	array.add(cursor.getString(3));
	    	array.add(cursor.getString(4));
	    	array.add(cursor.getString(5));
	    	return array; 
	    } else 
	    	return null;
	}
		
	public int getPin() {
		// Name of the columns we want to select
		String[] tableColumns = new String[] {"pin"};
		
		// Query the database
		Cursor cursor = db.query("login", tableColumns, null, null, null, null, null);
		cursor.moveToFirst();
		
		// Iterate the results
	    if (!cursor.isAfterLast()) {	    	
	    	return cursor.getInt(0); 
	    } else 
	    	return 0;
	}
	
}
