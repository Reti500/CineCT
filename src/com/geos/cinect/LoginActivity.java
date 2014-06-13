package com.geos.cinect;

import com.geos.cinect.fragments.LoginFragment;
import com.geos.cinect.interfaces.LoginListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;

public class LoginActivity extends FragmentActivity implements LoginListener {

	private LoginFragment fragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		fragment = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.login_fragment);
		fragment.setListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void goMain() {
		// TODO Auto-generated method stub
		Intent i = new Intent().setClass(LoginActivity.this, MainActivity.class);
		
		startActivity(i);
		finish();
	}

}
