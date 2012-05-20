package com.lapost.client;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.View;

public class LetterActivity extends Activity {
	
	Context context;
	private SharedPreferences prefs;
    private String nameKey;
	private String packageName;



	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.letter_form);
        
        context=this;
       
		

        
        final Spinner typeService = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this, R.array.services, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeService.setAdapter(adapter);
        
        final TextView txtLetter = (TextView) findViewById(R.id.txtLetter);
        txtLetter.setText(getIntent().getStringExtra("letter"));
        
        if(txtLetter.getText().length()<1){
        	
        	String x=new String();
        	x=String.valueOf((int) (Math.random() * 10))+"A"+String.valueOf((int) (Math.random() * 1000))+String.valueOf((int) (Math.random() * 1000))+String.valueOf((int) (Math.random() * 1000))+String.valueOf((int) (Math.random() * 100));
        	txtLetter.setText(x);
        }
        
        final AutoCompleteTextView name= (AutoCompleteTextView)findViewById(R.id.nameReceiver);
        final AutoCompleteTextView add= (AutoCompleteTextView)findViewById(R.id.addrReceiver);
        
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(this, R.layout.auto_item, Constants.NAMES);
        name.setAdapter(nameAdapter);
        
        
        ArrayAdapter<String> addAdapter = new ArrayAdapter<String>(this, R.layout.auto_item, Constants.ADDRESSES);
        add.setAdapter(addAdapter);
        
        
        
        /** Publish random data */
        Button btnSubmit = (Button)findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
        	@Override          
        	public void onClick(View view) {
        		

        		
        		if(name.getText().length()<1){
        		Toast.makeText(context, "Le nom du destinataire n'est pas valide", Toast.LENGTH_LONG);
        		return;
        		}
        		
        		if(add.getText().length()<1){
            		Toast.makeText(context, "L'adresse du destinataire n'est pas valide", Toast.LENGTH_LONG);
            		return;
            		}
        		

        		
        		JSONObject json = new JSONObject();
        	    try {	    	
        			json.put("identifier", ""+txtLetter.getText());
        		    json.put("nameSender", Constants.userName);
        		    json.put("nameReceiver", name.getText());
        		    json.put("addrReceiver", add.getText());
        		    
        		    Log.d("type=", typeService.getSelectedItem().toString());
        		    json.put("type", typeService.getSelectedItem().toString());

        		    
        		} catch (JSONException e) {
        			e.printStackTrace();
        		}
        		
                Constants.shouldRefreshReceivedList=true;
                Constants.shouldRefreshSentList=true;
        	      
            	String path = "letter";
            	String data = json.toString();
            	HttpHelper.doPost(path, data);
            	finish();
        	}
        });
    }
}
