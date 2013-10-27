package com.andrei.aroadz0.controller;

import android.os.AsyncTask;

public class WriteDataTask extends AsyncTask<String, String, String > {
	
	protected void onPreExecute() {
		// UI
		super.onPreExecute();
		
		
	}

	protected String doInBackground(String... params) {
		// Background. Use publishProgress(something) to send something to onProgressUpdate()
		//Log.appendLogLine(params[0]); 
		return null;
	}

	protected void onProgressUpdate(String... values) {
		// UI
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}

	
}
