package com.geos.cinect.fragments;

import com.geos.cinect.R;
import com.geos.cinect.libs.Question;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FormFragment extends Fragment {

	private ListView list_questions;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_form, container,
				false);
		
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
//		list_questions = (ListView) getView().findViewById(R.id.form_fragment);
//		list_questions.setAdapter(new AdapterQuestions(this));
	}
	
//	public class AdapterQuestions extends ArrayAdapter<Question> {
//		Activity context;
//		
//		public AdapterQuestions(Fragment context) {
//			// TODO Auto-generated constructor stub
//			super(context.getActivity(), )
//		}
//	}
}
