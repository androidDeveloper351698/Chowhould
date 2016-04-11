package com.cwenhui.chowhound.utils;

import java.util.ArrayList;
import java.util.List;

import com.cwenhui.chowhound.bean.DeliveryAddress;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
	private DBHelper helper;
	private SQLiteDatabase db;

	public DBManager(Context context) {
		helper = new DBHelper(context);
		// 因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
		// 所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
		db = helper.getWritableDatabase();
	}

	public void addDeliveryAddress(List<DeliveryAddress> address) {
		db.beginTransaction();  //开始事务  
		for (DeliveryAddress deliveryAddress : address) {
			try{
				db.execSQL("INSERT INTO delivery_address VALUES(null, ?, ?, ?)", new Object[]{deliveryAddress.getAdrReceiver(), 
						deliveryAddress.getAdrPhone(), deliveryAddress.getAdrAddress()});  
				db.setTransactionSuccessful();  //设置事务成功完成  
			}catch(Exception e){
			}finally{
				db.endTransaction();    //结束事务 
			}
		}
	}
	
	public void addDeleveryAddress(DeliveryAddress deliveryAddress){
		db.beginTransaction();  //开始事务
		try{
			db.execSQL("INSERT INTO delivery_address VALUES(null, ?, ?, ?)", new Object[]{deliveryAddress.getAdrReceiver(), 
					deliveryAddress.getAdrPhone(), deliveryAddress.getAdrAddress()});  
			db.setTransactionSuccessful();  //设置事务成功完成  
		}catch(Exception e){
		}finally{
			db.endTransaction();    //结束事务 
		}
	}
	
	public void updateDeliveryAddress(DeliveryAddress address){
		ContentValues cv = new ContentValues();  
        cv.put("adrReceiver", address.getAdrReceiver());  
        cv.put("adrPhone", address.getAdrPhone());
        cv.put("adrAddress", address.getAdrAddress());
        db.update("delivery_address", cv, "addressId = ?", new String[]{String.valueOf(address.getAddressId())});  
	}
	
	public void deleteDeliveryAddress(int addressId){
		db.delete("delivery_address", "addressId = ?", new String[]{String.valueOf(addressId)});  
	}
	
	public List<DeliveryAddress> queryDeliveryAddresses(){
		ArrayList<DeliveryAddress> deliveryAddresses = new ArrayList<DeliveryAddress>();  
		Cursor c = db.rawQuery("SELECT * FROM delivery_address", null);  
		DeliveryAddress address = null;
		while (c.moveToNext()) {  
			address = new DeliveryAddress();
			address.setAddressId(c.getInt(c.getColumnIndex("addressId")));
			address.setAdrReceiver(c.getString(c.getColumnIndex("adrReceiver")));
			address.setAdrPhone(c.getString(c.getColumnIndex("adrPhone")));
			address.setAdrAddress(c.getString(c.getColumnIndex("adrAddress")));
			deliveryAddresses.add(address);
		}
		c.close(); 
		return deliveryAddresses;
	}
	
	public DeliveryAddress queryDeliveryAddress(int addressId){
		Cursor c = db.rawQuery("SELECT * FROM delivery_address where addressId = ?", new String[]{String.valueOf(addressId)});  
		DeliveryAddress address = null;
		if(c.moveToNext()){
			address = new DeliveryAddress();
			address.setAddressId(c.getInt(c.getColumnIndex("addressId")));
			address.setAdrReceiver(c.getString(c.getColumnIndex("adrReceiver")));
			address.setAdrPhone(c.getString(c.getColumnIndex("adrPhone")));
			address.setAdrAddress(c.getString(c.getColumnIndex("adrAddress")));
		}
		c.close(); 
		return address;
	}
	
	public void closeDB() {  
        db.close();  
    }  
}
