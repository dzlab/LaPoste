package com.lapost.staff;

import com.lapost.client.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

/**
 * Demonstrates how to embed a WebView in your activity. Also demonstrates how
 * to have javascript in the WebView call into the activity, and how the activity 
 * can invoke javascript.
 * <p>
 * In this example, clicking on the android in the WebView will result in a call into
 * the activities code in {@link DemoJavaScriptInterface#clickOnAndroid()}. This code
 * will turn around and invoke javascript using the {@link WebView#loadUrl(String)}
 * method.
 * <p>
 * Obviously all of this could have been accomplished without calling into the activity
 * and then back into javascript, but this code is intended to show how to set up the 
 * code paths for this sort of communication.
 *
 */
public class StaffActivity extends Activity {

    private static final String LOG_TAG = "WebViewDemo";

    private WebView mWebView;
    private ImageView imageView;


    private Handler mHandler = new Handler();
	SharedPreferences prefs ;
    SharedPreferences.Editor editor;
    

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
	    imageView= (ImageView) findViewById(R.id.splash);

        prefs = getSharedPreferences("preferences", 0);
	    editor = prefs.edit();
        
        mWebView = (WebView) findViewById(R.id.webview);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        
        mWebView.setWebViewClient(new WebViewClient() { 
            @Override 
            public void onPageFinished(WebView view, String url) 
            { 
                /* Call whatever, It's finished loading. */ 
            	
            
            	mWebView.setVisibility(View.VISIBLE); 
            	imageView.setVisibility(View.INVISIBLE);

                    } 
        }); 

        mWebView.setWebChromeClient(new MyWebChromeClient());

        mWebView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo");

        mWebView.loadUrl("file:///android_asset/index.html");
    }

    final class DemoJavaScriptInterface {

        DemoJavaScriptInterface() {
        }

        /**
         * This is not called on the UI thread. Post a runnable to invoke
         * loadUrl on the UI thread.
         */
        public void clickOnAndroid() {
            mHandler.post(new Runnable() {
                public void run() {
                    mWebView.loadUrl("javascript:addDataForEnvoye('Yang', '1er rue de la Defense', '- Le 12 Mars 2012: Recu par la poste')");
                }
            });

        }
    }

    /**
     * Provides a hook for calling "alert" from javascript. Useful for
     * debugging your javascript.
     */
    final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            Log.d(LOG_TAG, message);
            result.confirm();
            return true;
        }
    }
	
	   public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		    if (requestCode == 0) {
		        if (resultCode == RESULT_OK) {
		            String contents = intent.getStringExtra("SCAN_RESULT");
		            String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
		            
		            editor.putString("login", contents);
			       // editor.putString("pass", params.get("pass"));
			       // editor.putString("memo", params.get("memo"));
			        editor.commit();
			        
			        Log.d("The result is", prefs.getString("login",""));
			        
			        
                   
			        displayPinCodePopup();
			        
				   //finish();

			       // finish();
		            // Handle successful scan
		        } else if (resultCode == RESULT_CANCELED) {
		            // Handle cancel
		        }
		    }
		}
	   
	   public void scranBarCode() {
	        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	        intent.putExtra("SCAN_MODE", "ONE_D_MODE");
	        startActivityForResult(intent, 0);
	        
	    }
	   
	  void  displayPinCodePopup(){
		  
		  String name="Bob";
		  String address="9 Rue du Colonel Raynal, Paris";
	      String status="Envoye";
		  
	      
	      String url = "file:///android_asset/index.html";
			mWebView.setVerticalScrollbarOverlay(true);
			mWebView.getSettings().setJavaScriptEnabled(true);
			mWebView.loadUrl(url);

			//jsi.addDataForEnvoye(name, address, status);
			
			//finish();
		  
	  }

}