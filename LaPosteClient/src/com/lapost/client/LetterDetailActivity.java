package com.lapost.client;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;

public class LetterDetailActivity extends Activity {

	Context context;
	private SharedPreferences prefs;
	private String nameKey;
	private String packageName;
	TextView txtLetter;
	TextView stateView;
	TextView addrReceiverView;
	TextView nameReceiverView;
	TextView identifierView;
	TextView nameSenderView;

	TextView typeServiceText;

	TextView price;
	Map<String, String> map = new HashMap<String, String>();
	
	JSONArray jsonStateHistory;
	
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.letter_detail_form);

		context = this;

		txtLetter = (TextView) findViewById(R.id.txtLetter);
		txtLetter.setText(getIntent().getStringExtra("letter"));
		

		if (txtLetter.getText().length() < 1) {

			String x = new String();
			x = String.valueOf((int) (Math.random() * 10)) + "A"
					+ String.valueOf((int) (Math.random() * 1000))
					+ String.valueOf((int) (Math.random() * 1000))
					+ String.valueOf((int) (Math.random() * 1000))
					+ String.valueOf((int) (Math.random() * 100));
			txtLetter.setText(x);
		}

		typeServiceText = (TextView) findViewById(R.id.type);
		price = (TextView) findViewById(R.id.price);

		nameSenderView = (TextView) findViewById(R.id.nameSender);

		nameReceiverView = (TextView) findViewById(R.id.nameReceiver);
		addrReceiverView = (TextView) findViewById(R.id.addrReceiver);

		stateView = (TextView) findViewById(R.id.state);
		stateView = (TextView) findViewById(R.id.state);

		
		new ProgressTask().execute();

		Button btnSign = (Button) findViewById(R.id.btnSign);
		btnSign.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

		

				showSignPopup();
			}
		});
	
if(getIntent().getStringExtra("SENT_LIST_OR_RECEIVED_LIST").contains("SENT_LIST")){
			
	btnSign.setVisibility(View.GONE);

}

	

}
	
	
	

	private class ProgressTask extends AsyncTask<String, Void, Boolean> {
		private ProgressDialog dialog;

		public ProgressTask() {

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

			typeServiceText.setText(map.get("type"));
			price.setText(map.get("price"));
			nameSenderView.setText(map.get("nameSender"));

			nameReceiverView.setText(map.get("nameReceiver"));
			addrReceiverView.setText(map.get("addrReceiver"));
			
			stateView.setText(map.get("state")+"\n\n"+map.get("stateHistory"));
			
			Constants.shouldRefreshSentList=true;	
			Constants.shouldRefreshReceivedList=true;	

			if (dialog.isShowing()) {
				dialog.dismiss();
			}

			if (success) {
				// Toast.makeText(context, "OK", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context, "Erreur de connexion",
						Toast.LENGTH_LONG).show();
			}
		}

		protected Boolean doInBackground(final String... args) {
			try {

				String id = txtLetter.getText().toString();

				//id = "6A57014764680";

				JSONArray json = new JSONArray(HttpHelper.doGet("letter?id='"
						+ id + "'"));

				JSONObject obj = (JSONObject) json.get(0);
				Log.i("LetterActivity", obj.toString());

				try {
					map.put("type", obj.getString("type"));
				} catch (Exception e) {

					e.printStackTrace();
					map.put("type", "");

				}

				try {
					map.put("price", obj.getString("price") + " (en euros)");
				} catch (Exception e) {

					e.printStackTrace();
					map.put("price", "");
					

				}

				map.put("identifier",
						getString(R.string.num) + obj.getString("identifier"));
				map.put("nameSender", "De: " + obj.getString("nameSender"));
				map.put("nameReceiver", "A: " + obj.getString("nameReceiver"));
				map.put("addrReceiver", obj.getString("addrReceiver"));

				 jsonStateHistory = new JSONArray(
						HttpHelper.doGet("state_history?id='"
								+ obj.getString("identifier") + "'"));

				String stateHistory = "";

				for (int j = jsonStateHistory.length() - 1; j >= 0; j--) {

					JSONObject jsonObjStateHistoryElement = (JSONObject) jsonStateHistory
							.get(j);

					JSONObject historyElementObj = (JSONObject) jsonObjStateHistoryElement;

					stateHistory = stateHistory + "\n" + " Le "
							+ historyElementObj.getString("date") + " "
							+ historyElementObj.getString("state") + "\n";

					if (j ==  jsonStateHistory.length() - 1) {
						map.put("state", "Statut actuel: \n" + "\n Le "
								+ historyElementObj.getString("date") + " "
								+ historyElementObj.getString("state"));
					}

					Log.i("LetterActivity" + "stateHistory=", stateHistory);

				}
				
				Log.i("LetterActivity" + "jsonStateHistory=", jsonStateHistory.toString());

				map.put("stateHistory", "Historiques:\n" + stateHistory);				

				return true;

			} catch (Exception e) {
				Log.e("tag", "error", e);
				return false;
			}
		}

	}
	
	
	
	void showSignPopup(){
		final EditText input=new EditText(context);
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		
		new AlertDialog.Builder(context)
	    .setTitle("Mot de passe client pour l'accuse de reception")
	    .setMessage("")
	    .setView(input).setIcon(R.drawable.icon_mini)
	    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            Editable value = input.getText();
	            
	            if(input.getText().length()>0){
	        	sign();
	    		new ProgressTask().execute();
	            }

				Toast.makeText(context, "Succes d'accuse de reception", Toast.LENGTH_LONG);

	            
	        }
	    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        		            // Do nothing.
	        }
	    }).show();
		
	}
	
	void sign(){
		
		try {
    		JSONObject json = new JSONObject();
    		json.put("state", this.getString(R.string.letter_signed_status)); 
    		HttpHelper.doPut("letter?id='"+map.get("identifier").toString().replace(getString(R.string.num), "").trim()+"'", json.toString());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		
	}
	
	

	void insertNewState(String newState){
		
		try {
    		JSONObject json = new JSONObject();
    		json.put("state", newState); 
    		HttpHelper.doPut("letter?id='"+map.get("identifier").toString().replace(getString(R.string.num).trim(),"")+"'", json.toString());
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
		
		new ProgressTask().execute();

		
	}
	
	
	
	

	
	

}
