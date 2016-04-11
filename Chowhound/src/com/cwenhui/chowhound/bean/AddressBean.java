package com.cwenhui.chowhound.bean;

public class AddressBean {
	private String poiName;
	private String poiAddress;
	private String poiDistance;

	public AddressBean(String poiName, String poiAddress) {
		super();
		this.poiName = poiName;
		this.poiAddress = poiAddress;
	}

	public AddressBean(String poiName, String poiAddress, String poiDistance) {
		super();
		this.poiName = poiName;
		this.poiAddress = poiAddress;
		this.poiDistance = poiDistance;
	}

	public String getPoiName() {
		return poiName;
	}

	public void setPoiName(String poiName) {
		this.poiName = poiName;
	}

	public String getPoiAddress() {
		return poiAddress;
	}

	public void setPoiAddress(String poiAddress) {
		this.poiAddress = poiAddress;
	}

	public String getPoiDistance() {
		return poiDistance;
	}

	public void setPoiDistance(String poiDistance) {
		this.poiDistance = poiDistance;
	}

}
