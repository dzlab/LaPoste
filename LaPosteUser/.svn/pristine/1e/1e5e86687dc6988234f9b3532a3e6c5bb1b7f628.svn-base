package com.lapost.client;

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
import org.json.JSONObject;

import android.util.Log;

public class LetterClient {

	public static final String ADDRESS = "http://glaposte123.appspot.com/";
	
	public static void getLetter() {
		URL url;
	    HttpURLConnection connection = null; 
		try {
			url = new URL(ADDRESS + "letter?id=\"1234\"");
			URLConnection urlC = url.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(urlC.getInputStream()));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) 
	            //System.out.println(inputLine);
	        in.close();

		    } catch (Exception e) {
		      e.printStackTrace();

		    } finally {

		      if(connection != null) {
		        connection.disconnect(); 
		      }
		    }
		    
		    System.out.println("LetterClient+DONE");
		    
	}

	public static void postLetter(String idLetter, String nameSender, String nameReceiver, String addressReceiver) {
		
	      JSONObject json = new JSONObject();

	    try {
		      json.put("identifier", idLetter);
		      json.put("nameSender", nameSender);
		      json.put("nameReceiver", nameReceiver);
		      json.put("addrReceiver", addressReceiver);
		      
		      
		 /*     HttpClient client = new DefaultHttpClient();
		      HttpPost post = new HttpPost(ADDRESS+"letter");
		      
		      List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); //On cr¨¦e la liste qui contiendra tous nos param¨¨tres
		      
		    //Et on y rajoute nos param¨¦tres
		    nameValuePairs.add(new BasicNameValuePair("content", json.toString()));

		    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));


		    HttpClient httpclient = new DefaultHttpClient();
		   System.out.print("postLetter*****"+httpclient.execute(post).toString()) ; //Voila, la requ¨ºte est envoy¨¦e
		      
			    		      
		      
		    /*  HttpHelper helper=new HttpHelper();
		      
		      
		      helper.sendJSONPost(ADDRESS+"letter", json);
		      
		    System.out.println("DONE");	*/
		
 }
	    
	    catch(Exception e){
	    	
	    	Log.d("", "e="+e);
	    }
	    
	    
	    URL url;
	    HttpURLConnection connection = null;  
	      StringBuffer response = new StringBuffer(); 

	    try {
	      //Create connection
	      //url = new URL("http://laposte-webapp.appspot.com/directory");
	    	url = new URL(ADDRESS + "letter");
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      
	      

	      
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
	      System.out.println("Post Response xxxxx="+response.toString());
	      

	    } catch (Exception e) {
	      e.printStackTrace();

	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	    

	      
	}

	public static void putLetter() {
		
		URL url;
	    HttpURLConnection connection = null;  
	    try {
		      JSONObject json = new JSONObject();
		      //json.put("identifier", "1234");
		      //json.put("nameSender", "Yan");
		      //json.put("nameReceiver", "John");
		      //json.put("addrReceiver", "La D¨¦fense");
		      json.put("state", "delivred");
	      //Create connection
	      //url = new URL("http://laposte-webapp.appspot.com/directory");
	    	url = new URL(ADDRESS + "letter?id=\"1234\"");
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("PUT");
	      
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
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      System.out.println("PutLetter"+response.toString());

	    } catch (Exception e) {
	      e.printStackTrace();

	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	    System.out.println("DONE");
	}

	

	
}