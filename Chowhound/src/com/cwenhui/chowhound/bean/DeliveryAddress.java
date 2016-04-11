package com.cwenhui.chowhound.bean;

public class DeliveryAddress {
	private int addressId;
	private String adrReceiver;
	private String adrPhone;
	private String adrAddress;

	public DeliveryAddress() {
		super();
	}

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String getAdrReceiver() {
		return adrReceiver;
	}

	public void setAdrReceiver(String adrReceiver) {
		this.adrReceiver = adrReceiver;
	}

	public String getAdrPhone() {
		return adrPhone;
	}

	public void setAdrPhone(String adrPhone) {
		this.adrPhone = adrPhone;
	}

	public String getAdrAddress() {
		return adrAddress;
	}

	public void setAdrAddress(String adrAddress) {
		this.adrAddress = adrAddress;
	}

}
