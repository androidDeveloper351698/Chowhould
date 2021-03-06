package com.cwenhui.chowhound.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class GoodsBean implements Parcelable{
	private int goodsId;
	private String goodsName;
	private int salesVolume;
	private int price;
	private String goodsImg;
	private int selectedNum;
	
	public GoodsBean() {
		super();
	}

	public GoodsBean(int goodsId, String goodsName, int salesVolume,
			int price, String goodsImg) {
		super();
		this.goodsId = goodsId;
		this.goodsName = goodsName;
		this.salesVolume = salesVolume;
		this.price = price;
		this.goodsImg = goodsImg;
		
		selectedNum = 0;
	}

	public int getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getSalesVolume() {
		return salesVolume;
	}

	public void setSalesVolume(int salesVolume) {
		this.salesVolume = salesVolume;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getGoodsImg() {
		return goodsImg;
	}

	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}

	public int getSelectedNum() {
		return selectedNum;
	}

	public void setSelectedNum(int selectedNum) {
		this.selectedNum = selectedNum;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(goodsId);
		out.writeInt(price);
		out.writeInt(salesVolume);
		out.writeInt(selectedNum);
		out.writeString(goodsName);
		out.writeString(goodsImg);
	}
	
	public static final Parcelable.Creator<GoodsBean> CREATOR = new Parcelable.Creator<GoodsBean>(){

		@Override
		public GoodsBean createFromParcel(Parcel in) {
			return new GoodsBean(in);				//从Parcel中读取数据的顺序要和写入Parcel中的顺序一样(即和writeToParcel()方法中的写入保持一样的顺序),否则会报错
		}

		@Override
		public GoodsBean[] newArray(int size) {
			return new GoodsBean[size];
		}
		
	};
	
	private GoodsBean(Parcel in){
		goodsId = in.readInt();
		price = in.readInt();
		salesVolume = in.readInt();
		selectedNum = in.readInt();
		goodsName = in.readString();
		goodsImg = in.readString();
	}

}
