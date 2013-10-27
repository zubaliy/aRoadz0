package com.andrei.aroadz0.controller;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.os.AsyncTask;
import android.util.Log;

import com.andrei.aroadz0.model.User;
import com.andrei.aroadz0.utils.Config;
import com.andrei.aroadz0.utils.Toasts;


public class Uploader {
	private final static String LOG_TAG = "zUploader";

	private static Uploader uploader = null;
	private static int taskcounter = 0;
	private HttpClient httpclient = null;
				
	private Uploader() {
		httpclient = createHttpClient();
	}
	
	public static Uploader getInstance() {
		if (uploader == null) {
			uploader = new Uploader();
		}
		return uploader;
	}
	
	private HttpClient createHttpClient() {
	    HttpParams params = new BasicHttpParams();
	    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	    HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
	    HttpProtocolParams.setUseExpectContinue(params, true);

	    SchemeRegistry schReg = new SchemeRegistry();
	    schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	    schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
	    ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);

	    return new DefaultHttpClient(conMgr, params);
	}
	
	private HttpClient getHttpClient() {
		return httpclient;
	}
	
	public void httpPostFile(String filepath) {
		// POSTmethod via asynctask, mag niet in de hoofdthread. Anders een error.
		new UploadDataTask().execute(filepath);
	}
	
	public void httpPostAllFilesInFolder() {
		File folder = new File(Config.WORKFOLDER);
		File[] listOfFiles = folder.listFiles();
		
		Toasts.showGreenMessage(listOfFiles.length + " files need to be uploaded");
		if(listOfFiles.length >= 1 ) {
			for (File element : listOfFiles)
			    if (element.isFile()){
			    	addCounter();
			    	new UploadDataTask().execute(element.getAbsolutePath());
			    }
		} else {
			Toasts.showError("No collected data.");
		}
	}

	// if with TRUE option, parse the file
	public void httpPostFilesInFolder(String folderpath, boolean b) {
		File folder = new File(folderpath);
		File[] listOfFiles = folder.listFiles();

		for (File element : listOfFiles)
		    if (element.isFile()){
		    	addCounter();
		    	System.err.println("#############################\n\n");
				System.err.println(taskcounter);
				System.err.println("\n\n#############################");
		    	new UploadDataTask().execute(element.getAbsolutePath());
		    }
	}
	
	public void httpPostAnomalies() {
		//new UploadDataTaskString().execute(UserDB.getInstance().getAnomaliesToString());
	}
	
	public void addCounter(){
		taskcounter++;
	}
	
	public void subCounter(){
		taskcounter--;
	}
	
	public static void resetCounter(){
		taskcounter = 0;
	}
	
	public boolean register() {
		new RegisterTask().execute(User.getInstance());
		return true;
	}
	
	
	
	private class UploadDataTask extends AsyncTask<String, String, Integer >{

		private File file = null;
	
		
		@Override
		protected Integer doInBackground(String... arg0) {
			HttpResponse response = null;
			
			try {
				this.file = new File(arg0[0]);
				HttpClient httpclient = getHttpClient();
				HttpPost httppost = new HttpPost(Config.URL_UPLOAD);
				
				MultipartEntity entity = new MultipartEntity();
					
				entity.addPart("myfile", new FileBody(file));
				entity.addPart("email", new StringBody(User.getInstance().getEmail()));
				httppost.setEntity(entity);			
				response = httpclient.execute(httppost);
				
				System.err.println("#############################\n\n");
				System.err.println("##        " + response.getStatusLine());
				System.err.println("\n\n#############################");
				
				
				} catch (ClientProtocolException e) {
				} catch (IOException e) {
				}
			
			return response.getStatusLine().getStatusCode();
		}

		@Override
		protected void onPostExecute(Integer result) {

			super.onPostExecute(result);
			if(result >= 200 && result <= 206){
				// Delete file after uploading
				new File(this.file.getAbsolutePath()).delete();
				
				Uploader.getInstance().subCounter();
				
				Toasts.showGreenMessage("Upload succeeded, files left: " + new File(Config.WORKFOLDER).listFiles().length);
				Log.d(LOG_TAG, "Upload succeeded, file deleted: " + result);
			} else {
				Toasts.showError("Error: " + result);
				Log.d(LOG_TAG, "Error: " + result);
			}	
		}
	}
	
	private class RegisterTask extends AsyncTask<User, String, Integer >{
		
		@Override
		protected Integer doInBackground(User... arg0) {
			HttpResponse response = null;
			
			try {
				User user = arg0[0];
				//HttpClient httpclient = new DefaultHttpClient();
				HttpClient httpclient = getHttpClient();
				HttpPost httppost = new HttpPost(Config.URL_REGISTER_USER);
				
				MultipartEntity entity = new MultipartEntity();
					
				entity.addPart("email", new StringBody(user.getEmail()));
				entity.addPart("password", new StringBody(user.getPassword()));
				entity.addPart("name", new StringBody(user.getName()));
				entity.addPart("phone_model", new StringBody(user.getPhone_model()));
				entity.addPart("android_version", new StringBody(user.getAndroid_version()));
				entity.addPart("android_language", new StringBody(user.getAndroid_language()));
				entity.addPart("acc_maximum_range", new StringBody(user.getAcc_maximum_range()));
				entity.addPart("acc_resolution", new StringBody(user.getAcc_resolution()));
				entity.addPart("acc_min_delay", new StringBody(user.getAcc_min_delay()));
				entity.addPart("acc_power", new StringBody(user.getAcc_power()));
				entity.addPart("acc_vendor", new StringBody(user.getAcc_vendor()));
				entity.addPart("acc_version", new StringBody(user.getAcc_version()));
				entity.addPart("acc_type", new StringBody(user.getAcc_type()));
				
				httppost.setEntity(entity);			
				response = httpclient.execute(httppost);
				
				System.err.println("#############################\n\n");
				System.err.println("##    " + response.getStatusLine() + " " + response.getStatusLine().getStatusCode());
				System.err.println("\n\n#############################");
				
				
				} catch (ClientProtocolException e) {
				} catch (IOException e) {
				}
			
			return response.getStatusLine().getStatusCode();
		}

		@Override
		protected void onPostExecute(Integer result) {

			super.onPostExecute(result);
			if(result >= 200 && result <= 206){
				Toasts.showGreenMessage("User registered: " + result);
				
				User.getInstance().setRegistered(true);
				
				Log.d(LOG_TAG, "User registered: " + result);
			} else {
				Toasts.showError("Error: " + result);
				Log.d(LOG_TAG, "Error: " + result);
			}
		}
	}		
}
	
	
	

