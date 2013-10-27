package com.andrei.aroadz0.service;



import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;



import com.andrei.aroadz0.R.drawable;
import com.andrei.aroadz0.R.string;
import com.andrei.aroadz0.controller.DataCombiner;
import com.andrei.aroadz0.controller.LogManager;
import com.andrei.aroadz0.controller.WriteDataTask;
import com.andrei.aroadz0.filter.Filter;
import com.andrei.aroadz0.filter.FilterImpl;
import com.andrei.aroadz0.model.Data;
import com.andrei.aroadz0.utils.Config;
import com.andrei.aroadz0.MainActivity;
import com.andrei.aroadz0.R;

import android.animation.AnimatorSet.Builder;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MyService extends Service implements Observer {
	
	private final String LOG_TAG = "zService";

	private DataCombiner datacomb = null;
	private LogManager log = null;
	private Filter filter;

	private NotificationManager notificationmanager;

	private BroadcastReceiver br = null;
	public final static String BROADCAST_ACTION = "com.andrei.aroadz0.service.MyService";

	public final static String PARAM_TASK = "task";
	public final static String PARAM_DATA = "data";

	private final int TASK1_CODE = 1;

	public void onCreate() {
		super.onCreate();

		Config.init(this);

		Notification();

		datacomb = new DataCombiner(getApplication().getApplicationContext());
		datacomb.addObserver(this);
		datacomb.addListeners();

		log = new LogManager();
		filter = new FilterImpl();

		someTask();

		createBR();

		isGpsEnabled();

		Log.d(LOG_TAG, "onCreate()");
	}

	
	private void isGpsEnabled() {
		if (!datacomb.isGpsEnabled()) {
			sendBR();
		}
	}

	private void sendBR() {
		Intent intent = new Intent(MainActivity.BROADCAST_ACTION);
		intent.putExtra(MyService.PARAM_TASK, 1);
		intent.putExtra(MyService.PARAM_DATA, "Gps");
		sendBroadcast(intent);
	}

	private void enableWrite(boolean bool) {
		Config.write = bool;
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(LOG_TAG, "onStartCommand(), startId = " + startId);
		// + "\n\n" + intent.getStringExtra("data"));

		if (flags == Service.START_FLAG_REDELIVERY) {
			Log.d(LOG_TAG, "START_FLAG_REDELIVERY");
		}
		if (flags == Service.START_FLAG_RETRY)
			Log.d(LOG_TAG, "START_FLAG_RETRY");
		if (flags == Service.START_STICKY_COMPATIBILITY)
			Log.d(LOG_TAG, "START_STICKY_COMPATIBILITY ");

		return Service.START_NOT_STICKY;
	}
	
	private void createBR() {
		// создаем BroadcastReceiver
		br = new BroadcastReceiver() {
			// действия при получении сообщений
			public void onReceive(Context context, Intent intent) {
				int task = intent.getIntExtra(PARAM_TASK, 0);
				boolean data = intent.getBooleanExtra(PARAM_DATA, false);
				Log.d(LOG_TAG, "onReceive: task = " + task + ", data = " + data);

				// Ловим сообщения о старте задач
				switch (task) {
				case TASK1_CODE:
					// enable write
					enableWrite(data);
					Notification();
					break;
				default:
					break;
				}
			}
		};

		// создаем фильтр для BroadcastReceiver
		IntentFilter intFilt = new IntentFilter(BROADCAST_ACTION);
		// регистрируем (включаем) BroadcastReceiver
		registerReceiver(br, intFilt);

	}
	
	private void someTask() {

	}

	public void onDestroy() {
		super.onDestroy();

		notificationmanager.cancelAll(); // or cancel specific ID
		datacomb.removeListeners();

		unregisterReceiver(br);

		Log.d(LOG_TAG, "onDestroy()");
		// System.exit(0);
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(LOG_TAG, "onBind()");
		return null;
	}
	
	public void Notification() {

		// Open MainActivity Class on Notification Click
		Intent intent = new Intent(this, MainActivity.class);
		// 2 next lines are need to start or to bring to the front the
		// MainActivity.
		// Without them one extra MainActivity would be created.
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);

		// Open Activity
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		// Create Notification using NotificationCompat.Builder

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this)
				// Set Icon
				.setSmallIcon(R.drawable.aroadz_icon)
				// Set Ticker Message
				.setTicker(getString(R.string.notificationticker))
				// Set Title
				.setContentTitle(getString(R.string.notificationtitle))
				// Set Text
				.setContentText(getString(R.string.notificationtext))
				// Add an Action Button below Notification
				// .addAction(R.drawable.ic_launcher, "Action Button", pIntent)
				// Set PendingIntent into Notification
				.setContentIntent(pIntent).setProgress(0, 0, Config.write)
				.setOngoing(true)
				// .setDefaults(android.app.Notification.DEFAULT_ALL) //sound,
				// vibrate, light
				// Dismiss Notification
				.setAutoCancel(false);

		// Create Notification Manager
		notificationmanager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Build Notification with Notification Manager
		// ID = 0, if same ID, then refresh notification
		notificationmanager.notify(0, builder.build());

	}
/*	
	// interesting code, not yet used
	private boolean isMyServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (MyService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	*/

	@Override
	public void update(Observable arg0, Object data) {
		// Update comes from DataCombiner
		if(Config.isWrite()){
			log.appendLine(filter.applyFilter(((Data) data)).toString());		
		}
		//Log.d(LOG_TAG, ""+Config.isWrite());
	}
	

}
