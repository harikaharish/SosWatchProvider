package com.harika.soswatchprovider.beans;

import java.util.Random;

public class Contact {
	

	private String id;
	private String contactName;
	private String phoneNo;
	private String contactUri;
	private String email;
	
	public Contact(){
		super();
	}
	
	public Contact(String id, String contactName, String phoneNo,
			String contactUri, String email) {
		super();
		this.id = id;
		this.contactName = contactName;
		this.phoneNo = phoneNo;
		this.contactUri = contactUri;
		this.email = email;
	}
	
	public String getContactUri() {
		return contactUri;
	}
	public void setContactUri(String contactUri) {
		this.contactUri = contactUri;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Integer.parseInt(id);;
		return result;
	}

	/*Need to edit this equals method basing on the id, email and other values in future.*/
	@Override
	public boolean equals(Object obj) {
		boolean isEqual = false;
		if(this.getClass() == obj.getClass()){
			Contact contactObject = (Contact)obj;
			if(contactObject.getId().equalsIgnoreCase(this.getId()))
			//if((contactObject.contactName).equalsIgnoreCase(this.contactName) && (contactObject.getPhoneNo()).equalsIgnoreCase(this.getPhoneNo()))
					isEqual = true;
		}
		return isEqual;
	}

	@Override
	public String toString() {
		return "Contact [Name=" + contactName + ", Number="
				+ phoneNo + ", Email=" + email + "]";
	}

}

