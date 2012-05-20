package com.lapost.client;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
public class ClientActivity extends Activity {

    private static final String LOG_TAG = "WebViewDemo";

    private WebView mWebView;
    private ImageView imageView;


    private Handler mHandler = new Handler();
	SharedPreferences prefs ;
    SharedPreferences.Editor editor;
    String jsonResult;
    String jsonResultRec;

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
        
        
        DirectoryRequests.postDirectory("A123", "Alice", "9 Rue Saint Cloud, Paris");
        
        
        
        ReloadWebView checkTask=new ReloadWebView(this, 5, mWebView);
        
        
        
        
        
        
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
                	
                	scanBarCode();
                }
            });
        }
            
       
    }
    
    
    public void addOneSendElement(String numColis) {
     
             
              String idLetter=numColis;
          	  String nameSender="Bob";
    	      String nameReceiver="Alice";
    		  String addressReceiver="9 Rue du Colonel Raynal, Paris";
    	      
    		  Log.d("PostLetter commence...","");
    	      LetterClient.postLetter(idLetter,nameSender,nameReceiver,addressReceiver);
    		  Log.d("Apres PostLetter ...","");

    	     // LetterClient.getLetter();
    	    	  

            
            }
        
        
        
    



    
    public void loadListlements() {
        mHandler.post(new Runnable() {
            public void run() {
            	
      /*      	Log.d("LoadListElements", jsonResult.toString());
            	Log.d("LoadListElements", "hi1");
            	jsonResult=jsonResult+"Hi";
            	Log.d("LoadListElements", "hi2");

            	jsonResultRec=jsonResultRec+"Rec";
            	Log.d("LoadListElements", "hi3");
*/

    		
               mWebView.loadUrl("javascript: removeAll()");
                mWebView.loadUrl("javascript:addDataForEnvoye("+jsonResult+")");
                
                
                mWebView.loadUrl("javascript: removeAllRec()");
                mWebView.loadUrl("javascript:addDataForRec("+jsonResultRec+")");
              


            }
        });
    }
    
    public void reloadSendList(){
    	
   /* 	 WebSettings webSettings = mWebView.getSettings();
         webSettings.setSavePassword(false);
         webSettings.setSaveFormData(false);
         webSettings.setJavaScriptEnabled(true);
         webSettings.setSupportZoom(false);
    	//efface tout
    	  mWebView.setWebChromeClient(new MyWebChromeClient());

          mWebView.addJavascriptInterface(new DemoJavaScriptInterface(), "demo");

          mWebView.reload();
*/
          
    	
    	 
 		//getClientLetters("nameFactor", "factor");
		 jsonResult=DirectoryRequests.getClientLetters("nameSender", "Bob");
		 
		 jsonResultRec=DirectoryRequests.getClientLetters("nameReceiver", "Bob");


	    System.out.println("jsonResultForRecver="+jsonResultRec);
	    System.out.println("jsonResultForSender="+jsonResult);


	      //JSONObject json = new JSONObject(jsonResult);
	    
	    loadListlements();
	 


		//if(oldjson.toString());
	   // addOneListlement();


    }
    
    

    /**
     * Provides a hook for calling "alert" from javascript. Useful for
     * debugging your javascript.
     */
    class MyWebChromeClient extends WebChromeClient {
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
			        
			        
                   
			        addOneSendElement(prefs.getString("login",""));			        
				   //finish();

			       // finish();
		            // Handle successful scan
		        } else if (resultCode == RESULT_CANCELED) {
		            // Handle cancel
		        }
		    }
		}
	   
	   public void scanBarCode() {
	        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
	        intent.putExtra("SCAN_MODE", "ONE_D_MODE");
	        startActivityForResult(intent, 0);
	        
	    }
	   
	  void  displayPinCodePopup(){
		  
	;
		  
	      
	      String url = "file:///android_asset/index.html";
			mWebView.setVerticalScrollbarOverlay(true);
			mWebView.getSettings().setJavaScriptEnabled(true);
			mWebView.loadUrl(url);

			//jsi.addDataForEnvoye(name, address, status);
			
			//finish();
		  
	  }
	  
	  
	 
	  
	  
	  protected class ReloadWebView extends TimerTask {
		    Activity context;
		    Timer timer;
		    WebView wv;

		    public ReloadWebView(Activity context, int seconds, WebView wv) {
		        this.context = context;
		        this.wv = wv;

		        timer = new Timer();
		        /* execute the first task after seconds */
		        timer.schedule(this,
		                seconds * 1000,  // initial delay
		                seconds * 1000); // subsequent rate

		        /* if you want to execute the first task immediatly */
		        /*
		        timer.schedule(this,
		                0,               // initial delay null
		                seconds * 1000); // subsequent rate
		        */
		    }

		    @Override
		    public void run() {
		        if(context == null || context.isFinishing()) {
		            // Activity killed
		            this.cancel();
		            return;
		        }

		        context.runOnUiThread(new Runnable() {
		            public void run() {
		                //wv.reload();
		            	
		            	reloadSendList();
		            	

		            }
		        });
		    }
		}
	  
	  
	  

}