package com.harika.soswatchprovider.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.harika.soswatchprovider.R;
import com.harika.soswatchprovider.adapters.ContactsListAdapter;
import com.harika.soswatchprovider.beans.Contact;
import com.harika.soswatchprovider.logic.SharedPreference;

public class ContactListFragment extends Fragment implements OnItemClickListener, OnItemLongClickListener {

	public static final String ARG_ITEM_ID = "contact_list";

	private Activity activity;
	private ListView contactListView;
	private List<Contact> contactsList;
	private ContactsListAdapter contactListAdapter;
	private SharedPreference sharedPreference;
	private EditText inputSearch;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		sharedPreference = new SharedPreference();
	
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_contact_list, container,false);
		findViewsById(view);
		this.fetchContacts();
		contactListAdapter = new ContactsListAdapter(activity, contactsList);
		contactListView.setAdapter(contactListAdapter);
		contactListView.setOnItemClickListener(this);
		contactListView.setOnItemLongClickListener(this);
		/*inputSearch = (EditText) view.findViewById(R.id.inputsearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
            	contactListAdapter.getFilter().filter(cs);
            }
            
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void afterTextChanged(Editable arg0) {
            	
                // TODO Auto-generated method stub
            }
        });	*/
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		/*Contact product = (Contact) parent.getItemAtPosition(position);
		Toast.makeText(activity, product.toString(), Toast.LENGTH_LONG).show();*/
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view,int position, long arg3) {
		ImageView button = (ImageView) view.findViewById(R.id.imgbtn_favorite);
		String tag = button.getTag().toString();
		if (tag.equalsIgnoreCase("grey") ) {
			sharedPreference.addFavorite(activity, contactsList.get(position));
			Toast.makeText(activity,activity.getResources().getString(R.string.add_favr),Toast.LENGTH_SHORT).show();
			button.setTag("red");
			button.setImageResource(R.drawable.heart_red);
		} else {
			sharedPreference.removeFavorite(activity, contactsList.get(position));
			button.setTag("grey");
			button.setImageResource(R.drawable.heart_grey);
			Toast.makeText(activity,activity.getResources().getString(R.string.remove_favr),Toast.LENGTH_SHORT).show();
		}
		return true;
	}
	
	@Override
	public void onResume() {
		getActivity().setTitle(R.string.app_name);
		getActivity().getActionBar().setTitle(R.string.app_name);
		super.onResume();
	}
	
    private void fetchContacts(){
    	
    	Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
    	String ID = ContactsContract.Contacts._ID;
    	String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
    	String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;
    	
    	Uri phoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    	String phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
    	String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
    	
    	Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
    	String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
    	String DATA = ContactsContract.CommonDataKinds.Email.DATA;
    	
    	ContentResolver contentResolver = getActivity().getContentResolver();
    	Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);
		contactsList = new ArrayList<Contact>();

    	if(cursor.getCount()> 0){   		
    		while(cursor.moveToNext()) {  
    			
    			Contact newContact = new Contact();  
    			newContact.setId(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)));
    			//newContact.setId(cursor.getString(cursor.getColumnIndex(ID)));
    			newContact.setContactName(cursor.getString(cursor.getColumnIndex(DISPLAY_NAME)));
    			int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex( HAS_PHONE_NUMBER )));
    			if(hasPhoneNumber > 0){
    				Cursor phoneCursor = contentResolver.query(phoneCONTENT_URI, null, phone_CONTACT_ID + " = ?", new String[] {newContact.getId()}, null);
    				
    				while (phoneCursor.moveToNext()){   					
    					   newContact.setPhoneNo(phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER)));
    				}   				
    				phoneCursor.close();   				
    				Cursor emailCursor = contentResolver.query(EmailCONTENT_URI,    null, EmailCONTACT_ID+ " = ?", new String[] {newContact.getId()}, null);

                    while (emailCursor.moveToNext()) {
                        newContact.setEmail(emailCursor.getString(emailCursor.getColumnIndex(DATA)));
    			    }
    		        emailCursor.close();
    			}
    			contactsList.add(newContact);
    		}
    	}
    }

	private void findViewsById(View view) {
		contactListView = (ListView) view.findViewById(R.id.contactslistView);
	}
}
