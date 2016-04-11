package com.cwenhui.chowhound.bean;


public class CommonBean {
	private String text;
	private int resource;
	private final String Tag = "Bean";

	
	public CommonBean(String text) {
		super();
		this.text = text;
	}

	public CommonBean(String text,int resource) {
		super();
		this.text = text;
		this.resource = resource;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getResource() {
		return resource;
	}

	public void setResource(int resource) {
		this.resource = resource;
	}

	@Override
	public String toString() {
		return "CommonBean [text=" + text + "]";
	}
	
	
	

}
