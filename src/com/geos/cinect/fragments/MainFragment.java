package com.geos.cinect.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.geos.cinect.FormActivity;
import com.geos.cinect.R;
import com.geos.cinect.libs.API;
import com.geos.cinect.libs.API.APIListener;
import com.geos.cinect.libs.ParseJson;

public class MainFragment extends Fragment implements OnItemSelectedListener {

	private Context context;
	private Spinner projects;
	private Spinner forms;
	private Button ver;
	private ArrayList<String> projects_json;
	private ArrayList<String> forms_json;
	
	private int current_project_id;
	private int current_form_id;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		
		context = rootView.getContext();
		
		projects = (Spinner) rootView.findViewById(R.id.main_spinner_project);
		forms = (Spinner) rootView.findViewById(R.id.main_spinner_form);
		ver = (Button) rootView.findViewById(R.id.main_button_ver);
		
		projects_json = new ArrayList<String>();
		forms_json = new ArrayList<String>();
		
		projects.setOnItemSelectedListener(this);
		forms.setOnItemSelectedListener(this);
		
		ver.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goForm();
			}
		});
		
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
				String[] vals = {"name", "forms"};
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
		projects_json.add(table.get("name"));
		forms_json.add(table.get("forms"));
//		addItemsOnSpinnerForms(table.get("forms"));
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
			android.R.layout.simple_spinner_item, projects_json);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	}
	
	public void addItemsOnSpinnerForms(String json_forms){
		List<String> list = new ArrayList<String>();
		ParseJson serialize = new ParseJson();
		String[] vals = {"name"};
		
		try {
			JSONArray a = new JSONArray(json_forms);
			for(int i=0; i<a.length(); i++){
				HashMap<String, String> list_forms = serialize.parse(a.getString(i), vals);
				list.add(list_forms.get("name"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		forms.setAdapter(dataAdapter);
	}
	
	public void goForm() {
		Intent i = new Intent().setClass(context, FormActivity.class);
		
		i.putExtra("projects", projects_json);
		i.putExtra("forms", forms_json);
		i.putExtra("id_project", current_project_id);
		i.putExtra("id_form", current_form_id);
		
		startActivity(i);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if(parent.getId() == R.id.main_spinner_project){
			current_project_id = position;
			current_form_id = 0;
			addItemsOnSpinnerForms(forms_json.get(position));
		}else if(parent.getId() == R.id.main_spinner_form){
			current_form_id = position;
		}
		
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
}
