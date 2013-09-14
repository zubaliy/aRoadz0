package com.example.aroadz0;

import java.util.concurrent.TimeUnit;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyService extends Service {
	
	final String LOG_TAG = "zService";
	
	private boolean run = true;
	
	private NotificationManager notificationmanager;
	
	public void onCreate() {
		super.onCreate();
		Log.d(LOG_TAG, "onCreate()");
		run = true;
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOG_TAG, "onStartCommand()");
		Notification();
		someTask();
		return Service.START_NOT_STICKY;
	}
	
	public void onDestroy(){
		super.onDestroy();
		run = false;
		notificationmanager.cancel(0);
		
		Log.d(LOG_TAG, "onDestroy()");
		//System.exit(0);
	}
	
	
	
	@Override
	public IBinder onBind(Intent intent) {
		Log.d(LOG_TAG, "onBind()");
		return null;
	}
	
	void someTask() {
		new Thread(new Runnable() {
			int i = 0;
		      public void run() {
		        while(run) {
		          Log.d(LOG_TAG, "i = " + i);
		          try {
		            TimeUnit.SECONDS.sleep(1);
		          } catch (InterruptedException e) {
		            e.printStackTrace();
		          }
		          i++;
		        }
		        stopSelf();
		      }
		    }).start();
	}
	
	
	public void Notification() {

        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(this, MainActivity.class);
        // 2 next lines are need to start or to bring to the front the MainActivity. 
        // Without there would create one extra MainActivity
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
 
        //Create Notification using NotificationCompat.Builder 
        
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.aroadz_icon)
                // Set Ticker Message
                .setTicker(getString(R.string.notificationticker))
                // Set Title
                .setContentTitle(getString(R.string.notificationtitle))
                // Set Text
                .setContentText(getString(R.string.notificationtext))
                // Add an Action Button below Notification
              //  .addAction(R.drawable.ic_launcher, "Action Button", pIntent)
                // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                .setProgress(0, 0, true)
                .setOngoing(true)
                // Dismiss Notification
                .setAutoCancel(false);
 
        // Create Notification Manager
        notificationmanager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());
 
    }
}
