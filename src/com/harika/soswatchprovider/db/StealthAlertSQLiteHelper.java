package com.harika.soswatchprovider.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Creates application database.
 * 
 * @author Harish G
 *
 */
public class StealthAlertSQLiteHelper extends SQLiteOpenHelper {

	public StealthAlertSQLiteHelper(Context context) {
		// Databse: stealthalert_db, Version: 1
		super(context, "stealthalert_db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Execute create table SQL
		db.execSQL("CREATE TABLE login (pin INTEGER PRIMARY KEY AUTOINCREMENT);");
		db.execSQL("CREATE TABLE settings (phone text, email text, "
				+ "emailmode text, smsmode text, frquency integer, interval integer);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}	
}
