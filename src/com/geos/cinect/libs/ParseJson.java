package com.geos.cinect.libs;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.array;
import android.util.Log;

public class ParseJson {
	
	// Private Constants
	private static final String LOG_TAG = "JsonParse";
	// --------------------------------------------------------
	
	public ParseJson(){
		super();
	}
	
	public HashMap<String, String> parse(String string_json, String[] vals){
		HashMap<String, String> json_parsed = new HashMap<String, String>();
		
		try {
			JSONObject json = new JSONObject(string_json);
			
			for(int i=0; i<vals.length; i++){
				String obj = json.isNull(vals[i]) != true ? json.getString(vals[i]) : "Error";
				json_parsed.put(vals[i], obj);
			}
		} catch(JSONException e) {
			Log.e(LOG_TAG, e.getMessage());
		}
		
		return json_parsed;
	}
	
	public JSONArray parse(String string_json, String val){
		JSONArray array = null;
		
		try {
			JSONObject json = new JSONObject(string_json);
			array = json.getJSONArray(val);
		} catch(JSONException e) {
			Log.e(LOG_TAG, e.getMessage());
		}
		
		return array;
	}
	
	public ArrayList<JSONObject> parseArray(JSONObject json, String head){
		ArrayList<JSONObject> json_parsed = new ArrayList<JSONObject>();
		
		try {
			JSONArray array = json.getJSONArray(head);
			
			for(int i=0; i<array.length(); i++){
				JSONObject item = array.getJSONObject(i);
				json_parsed.add(item);
			}
		} catch (JSONException e){
			Log.e(LOG_TAG, e.getMessage());
		}
		
		return json_parsed;
	}
}
