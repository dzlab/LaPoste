package com.lapost.client;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class C2DMReceiver extends BroadcastReceiver {
	
	public static final String TAG = C2DMReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "Receiving a C2DM message");
		if (intent.getAction().equals("com.google.android.c2dm.intent.REGISTRATION")) {
	        handleRegistration(context, intent);
	    } else if (intent.getAction().equals("com.google.android.c2dm.intent.RECEIVE")) {
	        handleMessage(context, intent);
	     }
	}
	
	private void handleRegistration(Context context, Intent intent) {
		Log.i(TAG, "Handling C2DM registration");
	    String registration = intent.getStringExtra("registration_id");
	    Log.i(TAG, "C2DM Registration ID= " + registration);
	    if (registration != null) {
	    	try {
	    		JSONObject json = new JSONObject();
	    		json.put("regid", registration);
	    		json.put("name", Constants.userName);
	    		HttpHelper.doPost("registration", json.toString());
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    	 	    	
	    } else if (intent.getStringExtra("error") != null) {
	    	Log.i(TAG, "C2DM Registration failed, should try again later.");
	    	
	    } else if (intent.getStringExtra("unregistered") != null) {
	        Log.i(TAG, "C2DM Unregistration done, new messages from the authorized sender will be rejected");
	    	
	    }
	}
	
	protected void handleMessage(Context context, Intent intent) {
		String message = intent.getStringExtra("message"); // data.message contains the notification text
		Log.i(TAG, "Received C2DM notification: " + message);
		String title = "OctoWorldCup notif";
		int iconId = R.drawable.ic_notification;

		// Creating the notification :
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(iconId, message, System.currentTimeMillis()); 

		// Creating the activity to be launched after a click :
		Intent notifIntent = new Intent(context.getApplicationContext(), MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notifIntent, 0);

		// Displaying the notification in a roll menu :
		notification.setLatestEventInfo(context, title, message, contentIntent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL; // la notification disparaitra une fois cliqu�e

		// Launching the notification :
		notificationManager.notify(1, notification);
	}

}
