package com.geos.cinect.fragments;

import java.util.ArrayList;

import com.geos.cinect.R;
import com.geos.cinect.libs.Question;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FormFragment extends Fragment {

	private Question[] datos = new Question[]{
		new Question("Hola")
	};
	
	private ListView list_questions;
	
	private ArrayList<String> forms_json;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_form, container,
				false);
		
		list_questions = (ListView) rootView.findViewById(R.id.form_list_questions);
		list_questions.setAdapter(new AdapterQuestions(this));
		
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}
	
	public void setFormsJson(ArrayList<String> forms_json){
		this.forms_json = forms_json;
	}
	
	public class AdapterQuestions extends ArrayAdapter<Question> {
		Activity context;
		
		public AdapterQuestions(Fragment context) {
			// TODO Auto-generated constructor stub
			super(context.getActivity(), R.layout.list_item_question, datos);
			this.context = context.getActivity();
		}
		
		public View getView(int position, View convertView, ViewGroup parent){
			View row = convertView;
			
			if(row == null){
				
			}
			
			LayoutInflater inflater = context.getLayoutInflater();
			LinearLayout item = (LinearLayout) inflater.inflate(R.layout.list_item_question, null);

			
			
			return item;
		}
	}
}
