package com.lapost.client;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class SentListActivity extends ListActivity {
	
	public static final String TAG = SentListActivity.class.getSimpleName();	
	ListAdapter listAdapter;
	Context context;

	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        overridePendingTransition(R.anim.hold, R.anim.push_left_in);
        this.getListView().setCacheColorHint(0);
        this.getListView().setDivider(this.getResources().getDrawable(R.drawable.transparent_color));
        this.getListView().setDividerHeight(2);
        new ProgressTask(SentListActivity.this).execute();      
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
    	if (scanResult != null) {    		
    	    // handle scan result
    		Toast.makeText(this, "Bordereau: " + scanResult.getContents(), Toast.LENGTH_SHORT).show();
    		Intent wake = new Intent(this, LetterActivity.class);
    		wake.putExtra("letter", scanResult.getContents());
            startActivity(wake);
    	}
    	// else continue with any other code you need in the method
    	
    }
     
    protected void loadListContent() {
    	List<Map<String, String>> data = new ArrayList<Map<String,String>>(); 
    	try {
    		String sender = URLEncoder.encode(Constants.userName, "UTF-8");
			JSONArray json = new JSONArray(HttpHelper.doGet("letter?sender='" + sender + "'"));
			
			
			for(int i=0; i < json.length(); i++) {
				JSONObject obj = (JSONObject) json.get(i); 
				Log.i(TAG, obj.toString());
				Map<String, String> map = new HashMap<String, String>();

				map.put("identifier", getString(R.string.num)+obj.getString("identifier"));
				map.put("nameReceiver", "A: " + obj.getString("nameReceiver"));
				map.put("addrReceiver", obj.getString("addrReceiver"));
				map.put("type", obj.getString("type"));

				
				//map.put("state", "Statut: " + obj.getString("state"));
				
				String identifier = URLEncoder.encode((String)obj.getString("identifier"), "UTF-8");
				JSONArray jsonStateHistory = new JSONArray(HttpHelper.doGet("state_history?id='"+identifier+"'"));

				String stateHistory="";
				
				for(int j=jsonStateHistory.length()-1; j >=0 ; j--) {
					
					JSONObject jsonObjStateHistoryElement = (JSONObject) jsonStateHistory.get(j); 
					
					JSONObject historyElementObj = (JSONObject) jsonObjStateHistoryElement; 

					stateHistory=stateHistory+"\n"+" Le "+historyElementObj.getString("date")+" "+historyElementObj.getString("state")+"\n";
					
					if(j==jsonStateHistory.length()-1){
					map.put("state", " Statut: " + "\n Le "+historyElementObj.getString("date")+" "+historyElementObj.getString("state"));
					}

					
					Log.i(TAG+"stateHistory=", stateHistory);

				}
				
				
				map.put("stateHistory", "Historiques:\n" + stateHistory);				
				data.add(map);
				
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}    	
  
        listAdapter = new SimpleAdapter(this, data, R.layout.list_item_layout, new String[] {"identifier", "nameReceiver", "state", "type"}, new int[] {R.id.identifier, R.id.name, R.id.state, R.id.type});
              
    }       

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sent_options, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add:
            	IntentIntegrator integrator = new IntentIntegrator(this);
            	integrator.initiateScan(); 
            	//storeNewLetterInfo();                
                return true;
            case R.id.refresh:

            	new ProgressTask(SentListActivity.this).execute();

            	return true;
            	
           
            default:
                return super.onOptionsItemSelected(item);
        }
    }    
    
    
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        openOptionsMenu();
    }
    
    
    private void openDialog(String identifier,String nameReceiver,String addrReceiver,String state){
    	
    	
    	final Dialog d = new Dialog(this,R.style.CustomDialogTheme);
    	d.setContentView(R.layout.detailed_letter_info);
    	
    	
    	TextView identifierView = (TextView) d.findViewById(R.id.identifier);
    	TextView nameReceiverView = (TextView) d.findViewById(R.id.name);
    	TextView addrReceiverView = (TextView) d.findViewById(R.id.addrReceiver);
    	TextView stateView = (TextView) d.findViewById(R.id.state);
    	
    	identifierView.setText(identifier);
    	nameReceiverView.setText(nameReceiver);
    	addrReceiverView.setText(addrReceiver);
    	stateView.setText(state);
    	
    	d.show();
    	
    }
    
    
    @Override  
    protected void onListItemClick(ListView l, View v, int position, long id) {  
     	HashMap data=(HashMap)listAdapter.getItem(position);
    	
    		Intent wake = new Intent(this, LetterDetailActivity.class);
    		wake.putExtra("letter", data.get("identifier").toString().replace(getString(R.string.num), "").trim());
    		wake.putExtra("SENT_LIST_OR_RECEIVED_LIST", "SENT_LIST");

    		startActivity(wake);
          
          super.onListItemClick(l, v, position, id);  
    }  
    
    
    private class ProgressTask extends AsyncTask<String, Void, Boolean> {
        private ProgressDialog dialog;
    
        public ProgressTask(ListActivity activity) {
            
            dialog = new ProgressDialog(context);
        }




    protected void onPreExecute() {
    	this.dialog.setTitle("La Poste");
        this.dialog.setIcon(R.drawable.icon_mini);
        this.dialog.setMessage("Chargement en cours...");
        this.dialog.setProgressStyle(R.style.CustomDialogTheme);
        this.dialog.setCancelable(false);
        this.dialog.show();
    }

        @Override
    protected void onPostExecute(final Boolean success) {
         
            
        	 setListAdapter(listAdapter);
             
            if (dialog.isShowing()) {
            dialog.dismiss();
        }

        if (success) {
            //Toast.makeText(context, "OK", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Erreur de connexion", Toast.LENGTH_LONG).show();
        }
    }

    protected Boolean doInBackground(final String... args) {
        try{    
           
        	loadListContent();

            return true;
         } catch (Exception e){
            Log.e("tag", "error", e);
            return false;
         }
      }

 
}
   protected void onResume(){
	   if(Constants.shouldRefreshSentList){
       new ProgressTask(SentListActivity.this).execute();
       Constants.shouldRefreshSentList=false;
	   }
       super.onResume();
    }

   
   protected void onPause(){
	   
       overridePendingTransition(R.anim.push_left_out, R.anim.hold);
       super.onPause();

   }
    
    
    
}
