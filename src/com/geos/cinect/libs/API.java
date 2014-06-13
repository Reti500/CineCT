package com.geos.cinect.libs;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class API {
	// Public constants
	public static final String METHOD_GET = "GET";
	public static final String METHOD_POST = "POST";
	
	// Routes
	public static final String API_URL_BASE = "http://geosforms.herokuapp.com/api/";
	public static final String SERVER_URL_BASE = "";
	
	public static final String API_LOGIN = API_URL_BASE + "sessions";
	public static final String API_PROJECTS = API_URL_BASE + "projects";
	
	// Private constants
	private static final String LOG_TAG = "API";
	private static final String LOG_ERROR_TAG = "ERROR API";
	
	// Instance
	private static API instance;
	
	// Client HTTP
	private HttpClient client;
	
	public API(){
		super();
		
		client = new DefaultHttpClient();
	}
	
	public static final API getIntance(){
		if(instance == null)
			instance = new API();
		return instance;
	}
	
	public void sendByPost(Context context, String msg, ArrayList<APIParams> params, String url, APIListener listener){
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		for(int i=0; i<params.size(); i++){
			APIParams p = params.get(i);

			if(p.type == 0){
				builder.addTextBody(p.name_param, p.param);
			}else if(p.type == 1){
				builder.addPart(p.name_param,  new FileBody(p.file));
			}
		}

        HttpEntity entity = builder.build();
        
        Log.i("SERVER", url);
        new SendPostTask(context, msg, listener, new HttpPost(url)).execute(entity);
	}
	
	public void sendByGet(Context context, String msg, ArrayList<APIParams> params, String url, APIListener listener){
		String new_url = createGetUrl(url, params);
		HttpGet get = new HttpGet(new_url);
		Log.i("URLBASE", new_url);
        new SendGetTask(context, listener, get).execute();
	}
	
	public ArrayList<APIParams> createParams(APIParams... params){
		ArrayList<APIParams> listParams = new ArrayList<APIParams>();

		if(params != null){
			for(int i=0; i<params.length; i++){
				listParams.add(params[i]);
			}
		}

		return listParams;
	}
	
	public String createGetUrl(String url, ArrayList<APIParams> params){
		if(!url.endsWith("?"))
	        url += "?";

	    List<NameValuePair> new_params = new LinkedList<NameValuePair>();

	    if(params != null){
			for(int i=0; i<params.size(); i++){
				APIParams p = params.get(i);
				new_params.add(new BasicNameValuePair(p.name_param, p.param));
			}
		}

	    String paramString = URLEncodedUtils.format(new_params, "utf-8");

	    return url += paramString;
	}
	
	public class SendPostTask extends AsyncTask<HttpEntity, Void, String>{

		private APIListener listener;
		private HttpPost httppost;
		private Context context;
		private ProgressDialog dialog;
		private String message;
		
		public SendPostTask(Context ctx, String msg, APIListener listener, HttpPost post){
			this.context = ctx;
			this.message = msg;
			this.listener = listener;
			this.httppost = post;
		}
		
		public SendPostTask(Context ctx, APIListener listener, HttpPost post){
			this.context = ctx;
			this.message = "Enviando datos ...";
			this.listener = listener;
			this.httppost = post;
		}
		
		public SendPostTask(APIListener listener, HttpPost post){
			this.context = null;
			this.message = "Enviando datos ...";
			this.listener = listener;
			this.httppost = post;
		}
		
		@Override
		protected void onPreExecute(){
			if(context != null){
				dialog = ProgressDialog.show(context, "Please wait ...", message, true);
			}
		}
		
		@Override
		protected String doInBackground(HttpEntity... params) {
			// TODO Auto-generated method stub
			
			String result = null;
			HttpResponse response = null;

			try{
				httppost.setEntity(params[0]);
		        response = client.execute(httppost);
		        result = parseResponce(response);

		        Log.i(LOG_TAG,"Post -> State -> "+response.getStatusLine().getStatusCode());
		        Log.i(LOG_TAG,"Post -> "+result);
			}catch(ClientProtocolException e){
				Log.d(LOG_ERROR_TAG, e.getMessage(),e);
			}catch (IOException e) {
	            Log.d(LOG_ERROR_TAG, e.getMessage(),e);
			}
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result){
			if(result != null){
				listener.success(result);
			}else{
				listener.error(result);
			}

			if(dialog != null) dialog.dismiss();
		}
		
	}
	
	public class SendGetTask extends AsyncTask<Void, Void, String>{

		private APIListener listener;
		private HttpGet httpget;
		private Context context;
		private ProgressDialog dialog;
		private String message;
		
		public SendGetTask(Context ctx, String msg, APIListener listener, HttpGet get){
			this.listener = listener;
			this.httpget = get;
			this.context = ctx;
			this.message = msg;
		}
		
		public SendGetTask(Context ctx, APIListener listener, HttpGet get){
			this.listener = listener;
			this.httpget = get;
			this.context = ctx;
			this.message = "Enviando datos ...";
		}
		
		public SendGetTask(APIListener listener, HttpGet get){
			this.listener = listener;
			this.httpget = get;
			this.context = null;
			this.message = "Enviando datos ...";
		}
		
		@Override
		protected void onPreExecute(){
			if(context != null)
				dialog = ProgressDialog.show(context, "Please wait ...", message, true);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String result = null;
			HttpResponse response = null;

			try{
		        response = client.execute(httpget);
		        result = parseResponce(response);

		        Log.i(LOG_TAG,"GET -> Sate -> "+response.getStatusLine().getStatusCode());
		        Log.i(LOG_TAG,""+result);
			}catch(ClientProtocolException e){
				Log.d(LOG_ERROR_TAG, e.getMessage(),e);
			}catch (IOException e) {
	            Log.d(LOG_ERROR_TAG, e.getMessage(),e);
	        }
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result){
			if(result != null){
				listener.success(result);
			}else{
				listener.error(result);
			}

			if(dialog != null) dialog.dismiss();
		}
		
	}
	
	public static class APIParams {
		public int type;
		public String name_param;
		public String param;
		public File file;

		public APIParams(String name_param, String param){
			this.name_param = name_param;
			this.param = param;
			this.type = 0;
		}

		public APIParams(String name_param, File param){
			this.name_param = name_param;
			this.file = param;
			this.type = 1;
		}
	}
		
	public String parseResponce(HttpResponse response){
		StringBuffer sb = new StringBuffer();

		try{
			BufferedReader in = new BufferedReader(
		        		new InputStreamReader(response.getEntity().getContent()));
	        String line = "";
	        String NL = System.getProperty("line.separator");
	        while ((line = in.readLine()) != null) {
	            sb.append(line);
	            sb.append(NL);
	        }
	        in.close();
		}catch(ClientProtocolException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

        return sb.toString();
	}
	
	public static abstract class APIListener {
		public abstract void success(String val);
		public abstract void error(String msg);
		public abstract void onFinal();
	}
}
