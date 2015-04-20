package com.harika.soswatchprovider.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.harika.soswatchprovider.beans.Contact;

public class SharedPreference {

	public static final String PREFS_NAME = "CONTACTS_APP";
	public static final String FAVORITES = "Contacts_Favorite";
	
	public SharedPreference() {
		super();
	}

	// This four methods are used for maintaining favorites.
	public void saveFavorites(Context context, List<Contact> favorites) {
		SharedPreferences settings;
		Editor editor;
		settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
		editor = settings.edit();
		Gson gson = new Gson();
		String jsonFavorites = gson.toJson(favorites);
		editor.putString(FAVORITES, jsonFavorites);
		editor.commit();
	}

	public void addFavorite(Context context, Contact product) {
		List<Contact> favorites = getFavorites(context);
		if (favorites == null)
			favorites = new ArrayList<Contact>();
		favorites.add(product);
		saveFavorites(context, favorites);
	}

	public void removeFavorite(Context context, Contact product) {
		ArrayList<Contact> favorites = getFavorites(context);
		if (favorites != null) {
			favorites.remove(product);
			saveFavorites(context, favorites);
		}
	}

	public ArrayList<Contact> getFavorites(Context context) {
		SharedPreferences settings;
		List<Contact> favorites;

		settings = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);

		if (settings.contains(FAVORITES)) {
			String jsonFavorites = settings.getString(FAVORITES, null);
			Gson gson = new Gson();
			Contact[] favoriteItems = gson.fromJson(jsonFavorites,Contact[].class);
			favorites = Arrays.asList(favoriteItems);
			favorites = new ArrayList<Contact>(favorites);
		} else
			return null;

		return (ArrayList<Contact>) favorites;
	}
}
