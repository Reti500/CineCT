package com.geos.cinect.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.geos.cinect.R;
import com.geos.cinect.libs.API;
import com.geos.cinect.libs.API.APIListener;
import com.geos.cinect.libs.API.APIParams;
import com.geos.cinect.libs.ParseJson;

public class MainFragment extends Fragment {

	private Context context;
	private Spinner projects;
	private Spinner forms;
	private Button ver;
	private ArrayList<String> projects_vals;
	private ArrayList<String> forms_vals;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		
		context = rootView.getContext();
		
		projects = (Spinner) rootView.findViewById(R.id.main_spinner_project);
		forms = (Spinner) rootView.findViewById(R.id.main_spinner_form);
		ver = (Button) rootView.findViewById(R.id.main_button_ver);
		
		getInfo();
		return rootView;
	}
	
	public void getInfo(){
		API api = API.getIntance();
		
		api.sendByGet(context, "Obteniendo proyectos ...", null, API.API_PROJECTS, new APIListener() {
			
			@Override
			public void success(String val) {
				// TODO Auto-generated method stub
				ParseJson serialize = new ParseJson();
				String[] vals = {"name"};
				JSONArray projects_json = serialize.parse(val, "projects");
				
				for(int i=0; i<projects_json.length(); i++){
					try {
						HashMap<String, String> json = serialize.parse(projects_json.get(i).toString(), vals);
						addItemsOnSpinner(projects, json);
						Log.i("JSON", json.values().toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
			
			@Override
			public void onFinal() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void error(String msg) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public void setListener(){
		
	}
	
	// add items into spinner dynamically
	public void addItemsOnSpinner(Spinner spinner, HashMap<String, String> table) {
		List<String> list = new ArrayList<String>();
		for(int i=0; i<table.size(); i++){
			list.add(table.get("name"));
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	  }
	
}
