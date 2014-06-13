package com.geos.cinect.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import com.geos.cinect.R;
import com.geos.cinect.interfaces.LoginListener;
import com.geos.cinect.libs.API;
import com.geos.cinect.libs.API.APIListener;
import com.geos.cinect.libs.API.APIParams;
import com.geos.cinect.libs.ParseJson;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginFragment extends Fragment {
	
	private Context context;
	private EditText username;
	private EditText password;
	private Button acceder;
	private LoginListener listener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_login,
				container, false);
		
		context = rootView.getContext();
		
		username = (EditText) rootView.findViewById(R.id.login_text_username);
		password = (EditText) rootView.findViewById(R.id.login_text_password);
		acceder = (Button) rootView.findViewById(R.id.login_button_acceder);
		
		acceder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if(listener != null)
//					listener.goMain();
				login();
			}
		});
		return rootView;
	}
	
	public void setListener(LoginListener listener){
		this.listener = listener;
	}
	
	public void login(){
		API api = API.getIntance();
		
		ArrayList<APIParams> params = api.createParams(
				new APIParams("email", "ricardo"),
				new APIParams("password", "casa")
		);
		
		api.sendByPost(context, "Iniciando sesion", params, API.API_LOGIN, new APIListener() {
			
			@Override
			public void success(String val) {
				// TODO Auto-generated method stub
				ParseJson serialize = new ParseJson();
				String[] vals = {"user"};
				HashMap<String, String> map = serialize.parse(val, vals);
//				Log.i("wey", map.get("Error"));
				if(map.get("user").equals("Error")){
					Toast.makeText(context, "Error en el usuario o contraseña", Toast.LENGTH_LONG).show();
					password.setText("");
				}else{
					SharedPreferences preferences = context.getSharedPreferences("userPref", Context.MODE_PRIVATE);
					Editor editor = preferences.edit();

					editor.putBoolean("savedUserData", true);
					editor.putString("email", "ricardo");
					editor.putString("password", "casa");
					editor.commit();

					if(listener != null){
						listener.goMain();
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
}
