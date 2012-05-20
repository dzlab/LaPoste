package com.lapost.staff;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class DirectoryRequests {
	
	public static final String ADDRESS = "http://post-webapp.appspot.com/";
	
	
	public static String getClientLetters(String key, String value) {

		URL url;
	    HttpURLConnection connection = null;  
	      StringBuffer response = new StringBuffer(); 

	    try {
	      //Create connection
	      //url = new URL("http://laposte-webapp.appspot.com/directory");
	    	url = new URL(ADDRESS + "letterReq");
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      
	      JSONObject json = new JSONObject();
	      json.put(key, value);

	      
	      String content = "content=" + json.toString();
	      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");				
	      connection.setRequestProperty("Content-Length", "" + Integer.toString(content.getBytes().length));
	      connection.setRequestProperty("Content-Language", "en-US");  
				
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);

	      //Send request
	      
	      DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
	      wr.writeBytes (content);
	      wr.flush ();
	      wr.close ();

	      //Get Response	
	      BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	      String line;
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      System.out.println("Directory"+response.toString());
	      

	    } catch (Exception e) {
	      e.printStackTrace();

	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	    
	      return response.toString();


	}


	

	public static void postDirectory(String identifier,String name,String address ) {
		
	    try {
	      //Create connection
	     	      
	      JSONObject json = new JSONObject();
	      json.put("identifier", identifier);
	      json.put("name", name);
	      json.put("address", address);
	      
	      HttpPost post = new HttpPost(ADDRESS+"directory");
	      
	      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); //On cr¨¦e la liste qui contiendra tous nos param¨¨tres
	      
	    //Et on y rajoute nos param¨¦tres
	    nameValuePairs.add(new BasicNameValuePair("content", json.toString()));



	    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));


	    HttpClient httpclient = new DefaultHttpClient();
	     
	    
	    httpclient.execute(post); //Voila, la requ¨ºte est envoy¨¦e*/
	      
	      
	   /*   HttpHelper helper=new HttpHelper();
	      
	      
	      helper.sendJSONPost(ADDRESS+"directory", json);*/
	      
	    System.out.println("DONE");	
	      
	}
	    catch(Exception e){
	    	
		    System.out.println(e.toString());	

	    }
	    
	}
	

	
	public static String toJsonString() {
		String text = null;
		
			JSONObject obj = new JSONObject();
			/** Encode rating values */
			try {
				obj.put("key", "value");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
			text = obj.toString();
		
		
		return text;
	}
}