package com.lapost.client;

import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TabHost;

public class MainActivity extends TabActivity{
	
	public static final String TAG = MainActivity.class.getSimpleName();
	
    private static String nameKey;
	private String addressKey;
	private String emailKey;
	private String phoneKey;
	private static SharedPreferences prefs;
	private String packageName;
	
	 TabHost tabHost;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        packageName = this.getPackageName();
        nameKey = packageName + "name_key";
		addressKey = packageName + "address_key";
		emailKey=packageName+ "email_key";
		phoneKey=packageName+ "phone_key";

		prefs = this.getSharedPreferences(this.getPackageName(),
				Context.MODE_PRIVATE);
		
		Constants.userName= prefs.getString(nameKey, "");
		
        if(Constants.userName.length()<1){
    	Intent info = new Intent(this, UserInfoActivity.class);
        this.startActivityForResult(info, 555);
        }
        
        	
		
        
        //System.setProperty("http.proxyHost", "my.proxyhost.com");
        //System.setProperty("http.proxyPort", "1234");
        
        // Initialize constants
		
	
        
        
        
        // Send an intent to ask Google for C2DM registration id
        registerC2DM();

        // initializing ListActivity
        Resources res = getResources(); // Resource object to get Drawables
         tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab


        // Do the same for the other tabs
        intent = new Intent().setClass(this, SentListActivity.class);
        spec = tabHost.newTabSpec("sent").setIndicator(getString(R.string.my_sendings),
                          res.getDrawable(R.drawable.arrow_up))
                      .setContent(intent);
        tabHost.addTab(spec);
        
        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, ReceivedListActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("received").setIndicator(getString(R.string.my_receptions),
                          res.getDrawable(R.drawable.arrow2))
                      .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
        
        
      //  tabHost.getTabWidget().getChildAt(1).setBackgroundColor(Color.parseColor("#003D8F"));


        
        
        
    }
    
    

    
    protected void registerC2DM() {
    	Log.i(TAG, "Registering for C2DM");
    	try {
        	Intent registrationIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
        	registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
        	registrationIntent.putExtra("sender", "droidxmann@gmail.com");
        	startService(registrationIntent);
	
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}