package com.laposte.gae.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.simple.JSONObject;


public class DirectoryClient {
	
	public static final String ADDRESS = "http://laposte-webapp.appspot.com/";

	public static void getClientLetters(String req) {
		URL url;
	    HttpURLConnection connection = null; 
		try {
			url = new URL(req);
			URLConnection urlC = url.openConnection();
			
	        BufferedReader in = new BufferedReader(new InputStreamReader(urlC.getInputStream()));
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) 
	            System.out.println(inputLine);
	        in.close();

		    } catch (Exception e) {
		      e.printStackTrace();

		    } finally {

		      if(connection != null) {
		        connection.disconnect(); 
		      }
		    }
		    
		    System.out.println("DONE");		    
	}

	public static void postDirectory() {
		
		URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      //url = new URL("http://laposte-webapp.appspot.com/directory");
	    	url = new URL("http://localhost:8888/directory");
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      
	      JSONObject json = new JSONObject();
	      json.put("identifier", "1234");
	      json.put("name", "John");
	      json.put("address", "La Defense");
	      
	      String content = "content=" + json.toJSONString();
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
	      System.out.println(response.toString());

	    } catch (Exception e) {
	      e.printStackTrace();

	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	    System.out.println("DONE");	
	}
	
	public static void main(String[] args) {
		//postDirectory();
		getClientLetters(ADDRESS + "directory?sender=\"Alice\"");
		getClientLetters(ADDRESS + "directory?receiver=\"John\"");
	}
	
	public static String toJsonString() {
		String text = null;
		
			JSONObject obj = new JSONObject();
			/** Encode rating values */
			obj.put("key", "value");
						
			text = obj.toString();
		
		
		return text;
	}
}