package com.laposte.gae.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.json.simple.JSONObject;

public class HttpHelper {

	//public static final String ADDRESS = "http://laposte-webapp.appspot.com/";
	//public static final String ADDRESS = "http://posteputain.appspot.com/";
	public static final String ADDRESS = "http://post-webapp.appspot.com/";
	//public static final String ADDRESS = "http://localhost:8888/";
	
	public static void doPost(String path, String data) {
		String str = "";
		URL url;
	    HttpURLConnection connection = null;  
	    try {
	    	
	      //Create connection
	      //url = new URL("http://laposte-webapp.appspot.com/directory");
	    	url = new URL(ADDRESS + path);
	    	connection = (HttpURLConnection)url.openConnection();
	    	connection.setRequestMethod("POST");
	      
	    	String message = URLEncoder.encode(data, "UTF-8");
	    	String content = "content=" + message;
	    	connection.setRequestProperty("Accept-Charset", "UTF-8");
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
	    	str = URLDecoder.decode(response.toString(), "UTF-8");

	    } catch (Exception e) {
	      e.printStackTrace();

	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	    System.out.println("POST DONE: " + str);
	}
	
	public static String doGet(String path) {
		URL url;
	    HttpURLConnection connection = null; 
	    String str = "";
		try {
				
			url = new URL(ADDRESS + path);
			URLConnection urlC = url.openConnection();
	        BufferedReader in = new BufferedReader(new InputStreamReader(urlC.getInputStream()));
	        
	        String inputLine;
	        while ((inputLine = in.readLine()) != null) {
	            str += URLDecoder.decode(inputLine, "UTF-8");
	        }
	        in.close();
		} catch (Exception e) {
		      e.printStackTrace();
		} finally {
			if(connection != null) {		    
				connection.disconnect(); 
			}    
		}
		System.out.println("GET to "+path+" DONE: "+str);		    		    
		return str;
	}
	
	public static void doDelete(String path) {
		URL url;
	    HttpURLConnection connection = null;  
	    try {
	    	url = new URL(ADDRESS + path);
	      
	    	connection = (HttpURLConnection)url.openConnection();
	      
	    	connection.setRequestMethod("DELETE");	      

	    	BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	      
	    	String line;
	      
	    	StringBuffer response = new StringBuffer(); 
	      
	    	while((line = rd.readLine()) != null) {
	    		response.append(line);
	    		response.append('\r');
	    	}
	    	rd.close();
	    	String str = URLDecoder.decode(response.toString(), "UTF-8");
	    	System.out.println(str);
	    	
	    } catch (Exception e) {
	    		e.printStackTrace();
	    } finally {
	    	if(connection != null) {
	    		connection.disconnect(); 	    
	    	}	    	
	    }	
	    System.out.println("DELETE DONE");
	}

}
