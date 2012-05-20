package com.laposte.gae.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;


public class LetterClient {

	public static String[] NAMES = new String[] {"Alice", "Bob", "Carol", "Eve", "Justin", "Matilda", "Peggy", "Victor", "Walter"};
	public static final String ADDRESS = "http://laposte-webapp.appspot.com/";
	//public static final String ADDRESS = "http://posteputain.appspot.com/";
	//public static final String ADDRESS = "http://post-webapp.appspot.com/";
	//public static final String ADDRESS = "http://localhost:8888/";
	
	public static String RandomUser() {
		int index = (int) (Math.random() * NAMES.length);
		return NAMES[index];
	}
	
	public static void getClientLetters(String key, String value) {
		System.out.println("Requesting '"+key+"' = " + value);
		URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      //url = new URL("http://laposte-webapp.appspot.com/directory");
	    	url = new URL(ADDRESS + "letterReq");
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      
	      JSONObject json = new JSONObject();
	      json.put(key, value);
	      
	      String content = "content=" + json.toJSONString();
	      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");				
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
	}
	
	public static void getLetter(String path, String id) {
		URL url;
	    HttpURLConnection connection = null; 
	    String str = "";
		try {
			if(id != null)
				url = new URL(ADDRESS + path + "?id='" + id + "'");
			else
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
		    
		    System.out.println("GET to '"+path+"' DONE: "+str);		    
	}

	public static void doDelete(String path) {
		URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      //url = new URL("http://laposte-webapp.appspot.com/directory");
	    	url = new URL(ADDRESS + path);
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("DELETE");	      

	      //Get Response	
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
	
	@SuppressWarnings("unchecked")
	public static void postLetter(String id) {
		String str = "";
		URL url;
	    HttpURLConnection connection = null;  
	    try {
		      JSONObject json = new JSONObject();
		      json.put("identifier", id);
		      //json.put("nameSender", "Yan");
		      json.put("nameSender", RandomUser());
		      json.put("nameReceiver", RandomUser());
		      json.put("addrReceiver", "La Défense");
		      
	      //Create connection
	      //url = new URL("http://laposte-webapp.appspot.com/directory");
	    	url = new URL(ADDRESS + "letter");
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      
	      String data = URLEncoder.encode(json.toJSONString(), "UTF-8");
	      String content = "content=" + data;
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

	@SuppressWarnings("unchecked")
	public static void putLetter(String id) {		
		URL url;
	    HttpURLConnection connection = null;  
	    try {
	    	String uri = ADDRESS + "letter?id='"+id+"'";
		    JSONObject json = new JSONObject();
		    json.put("state", "reçu");
	    	url = new URL(uri);
	    	connection = (HttpURLConnection)url.openConnection();
	    	connection.setRequestMethod("PUT");
	      
	    	String content = "content=" + URLEncoder.encode(json.toJSONString(), "UTF-8");
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
	      System.out.println("PUT DONE to " + uri + " : "+ response.toString());
	    } catch (Exception e) {
	      e.printStackTrace();

	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	}

	
	public static void main(String[] args) {
		
		String id = "" + ((int)(Math.random()*10000));
		//postLetter(id);
		
		//getLetter();
		//putLetter();
		//getClientLetters("nameSender", "Victor");
		//getClientLetters("nameReceiver", "Justin");
		//getClientLetters("idLetter", "5678");
		//putLetter("5450");
		//getLetter("letter", id);
		getLetter("letterReq", null);
		//getLetter("state_history", "5450");
		//getLetter("state_history", "5297");
		doDelete("letter");
		doDelete("state_history");
	}
	
}