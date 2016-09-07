package com.game.tsoglanakos.ac.vresToPesTo;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;
private static boolean hasUpdate=false;
	// Database Name
	private static final String DATABASE_NAME = "Vres_to_Pes_to_DB";

	// Contacts table name
	private static final String TABLE_CONTACTS = "Vres_Pes_Table";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String LEVELS = "levels";
	private static final String ADS="ads";
	private static final String TIME="time";
	
	private static ArrayList<Integer> listId = new ArrayList<Integer>();

	/**
	 * 
	 * @param context
	 */
	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		try{
		onCreate(getWritableDatabase());
		}catch(Exception e){}
	//	onCreate(getWritableDatabase());
		
		
	}

	/**
 * 
 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try{
	
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + LEVELS + " INTEGER,"+TIME + " TEXT,"
				+ADS + " TEXT"+ ")";
		db.execSQL(CREATE_CONTACTS_TABLE);
	}
		catch (Exception e){
			e.printStackTrace();
		}}

	/**
 * 
 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		for (int i = 0; i < 10; i++) {
			Log.e("Database ", "on Upgrade ");
		}
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

		// Create tables again
		onCreate(db);
	}

	/**
	 * 
	 * @param contact
	 */
	public void addContact(int levels, int time,boolean addAdsNumber) {
		int hint=0;
		try{
			
			
		
		if(addAdsNumber){
			hint=1;
		}
		
	    onUpgrade(this.getWritableDatabase(), 1, 1);
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, 0);
		
		values.put(LEVELS, levels); // Contact button id
		values.put(TIME, Integer.toString(time)); // time
		values.put(ADS, Integer.toString(hint)); // hints
	
		// Number

		// Inserting Row
		db.insert(TABLE_CONTACTS, null, values);
		db.close(); // Closing database connection
		}catch(Exception e){
			try{
				boolean b=false;
				if(hint==1){
					b=true;
				}
			addContact( levels,time, b);
		}catch(Exception ex){
			
		}}
	}

	/**
	 * 
	 * @return
	 */
	// Getting All Contacts
	public ArrayList<Integer> getAllContacts() {
		ArrayList<Integer> contactList = new ArrayList<Integer>();
		// Select All Query
		listId.removeAll(listId);
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor!=null&&cursor.moveToFirst()) {
			do {
				int totalLevels = 0;
				int rounds = 0;
				int adsNumber = 0;
				int id=cursor.getInt(0);
				rounds = Integer.parseInt(cursor.getString(1));
				totalLevels = cursor.getInt(2);
				adsNumber= Integer.parseInt(cursor.getString(3));
               // int hint= cursor.getInt(2);
				// contact.setName(cursor.getString(1));
				// contact.setPhoneNumber(cursor.getString(2));
				// Adding contact to list
				contactList.add(rounds);
				contactList.add(totalLevels);
				contactList.add(adsNumber);
				listId.add(id);
				
			} while (cursor.moveToNext());
		}
db.close();
		// return contact list
		return contactList;
	}
	
	
	

	/**
		  * 
		  */
	// Deleting all contacts
	public void removeAll() {
		if (listId.isEmpty()) {
			return;
		}
		SQLiteDatabase db = this.getWritableDatabase();
		//ArrayList<Integer> array = getAllContacts();
		int counter = getCorrectCount();
		while (!listId.isEmpty()) {
			Log.e("removeAll", "function");
			Log.e("number ", Integer.toString(counter));
			db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
					new String[] { String.valueOf(listId.remove(0)) });
			
		}
		db.close();

	}

	/*
	 * public void removeAll(){ SQLiteDatabase db = this.getWritableDatabase();
	 * db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS); db.close(); }
	 */

	public int getCorrectCount() {
		return getAllContacts().size();
	}

}
