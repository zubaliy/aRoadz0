package com.andrei.aroadz0.controller;
	
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
	
import com.andrei.aroadz0.model.Data;
import com.andrei.aroadz0.model.User;
import com.andrei.aroadz0.utils.Config;

	
	
import android.os.AsyncTask;
import android.util.Log;

	
public class LogManager extends AsyncTask<String, String, String > {
		
	private final String TAG = "zLog"; 
	
	private File logfile = null;
	public String filelist = new String();

	private final String format = new String("yyyy.MM.dd-kk.mm.ss");
	private SimpleDateFormat sdf = null;
	private String currentDateTimeString = null;
	
	
	public LogManager() {
		
	}

	public void createNewLogFile() {
		sdf = new SimpleDateFormat(format, Locale.UK);	
		currentDateTimeString = sdf.format(System.currentTimeMillis());
		
		logfile = new File(
				  Config.WORKFOLDER
				+ User.getInstance().getEmail() 
				+ "-" + currentDateTimeString
				+ ".csv");
		
		createNewFile(logfile);
	}
	
	public void createNewFile(File file){
		logfile = file;
		if (!file.exists()) {
		      try
		      {
		         file.createNewFile();
		         // First line used as header.
		         // Need to parse .csv on the server correctly.
		         // Look in DataCombiner.java for the right order
		         
		         appendLine(	 "timestamp" + ",\t" +
				        		 "longitude" + ",\t" + 
				        		 "latitude" + ",\t" + 	
				        		 "speed" + ",\t" + 
				        		 "accuracy" + ",\t" + 
				        		 "altitude" + ",\t" + 
				        		 "xaR" + ",\t" + 
				        		 "yaR" + ",\t" + 
				        		 "zaR"
				        		 );

		         Log.d(TAG, "New logfile was created:" + file.getAbsolutePath() );
		      } 
		      catch (IOException e)
		      {
		         // TODO Auto-generated catch block
		         e.printStackTrace();
		      }
		   }
		}
	
	public void listFilesForFolder(final File folder) {
		File[] listOfFiles = folder.listFiles();

		for (File element : listOfFiles)
		    if (element.isFile()){
		        filelist += element.getName() + "\n";
		    }

	}

	public void saveListOfFiles(final File file){
		if (!file.exists())
		   {
		      try
		      {
		         file.createNewFile();
		         Log.d(TAG,"New listfile was created");
		      } 
		      catch (IOException e)
		      {
		         // TODO Auto-generated catch block
		         e.printStackTrace();
		      }
		   }
		 try
		   {
		      //BufferedWriter for performance, true to set append to file flag
		      BufferedWriter buf = new BufferedWriter(new FileWriter(file, true)); 
		      buf.write(filelist);
		      buf.newLine();
		      buf.close();
		   }
		   catch (IOException e)
		   {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		   }
		}
	
	
	public void appendLine(String dataline) {
		if (logfile == null) {
			createNewLogFile();
		    Log.d(TAG, "Logfile doesn't exist");
		} else {
	   
			   try
			   {
			      //BufferedWriter for performance, true to set append to file flag
			      BufferedWriter buf = new BufferedWriter(new FileWriter(logfile, true)); 
			      buf.append(dataline);
			      buf.newLine();
			      buf.close();
			   }
			   catch (IOException e)
			   {
			      // TODO Auto-generated catch block
			      e.printStackTrace();
			   }
		}
	}
	
	public void deleteFile(String filepath){
		File file = new File(filepath);
		file.delete();
	}
	
	public void writeLine(String line) {
		try {
			// TODO crash, when too many tasks. How to solve?
			execute(line);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
	protected void onPreExecute() {
		// UI
		super.onPreExecute();
	}

	protected String doInBackground(String... params) {
		// Background. Use publishProgress(something) to send something to onProgressUpdate()
		appendLine(params[0]); 
		Log.d(TAG, "Writing");
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
